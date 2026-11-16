package view.canvas;

import data_access.InMemoryFunctionDataAccessObject;
import view.answer.CanvasAnswer;
import interface_adapter.MathLabViewModel;
import interface_adapter.evaluate.DefinitionCheckController;
import interface_adapter.evaluate.EvaluateController;
import interface_adapter.evaluate.EvaluatePresenter;
import interface_adapter.graph.GraphController;
import interface_adapter.graph.GraphViewModel;
import entity.LatexFunctionParser;
import org.junit.jupiter.api.Test;
import use_case.evaluate.CalculateCheckInteractor;
import use_case.evaluate.DefinitionCheckInteractor;
import use_case.graph_check.GraphCheckInteractor;
import use_case.evaluate.EvaluateInteractor;
import view.DrawingCanvas;

import javax.swing.SwingUtilities;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BooleanSupplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

// pause after "=" (or a lasso pick) -> popup with the formula -> Calculate -> answer on the canvas
class AnswerCanvasPageTest {
    private final DrawingCanvas canvas = new DrawingCanvas();
    private final SharedCanvas shared = new SharedCanvas(canvas);
    private final MathLabViewModel viewModel = new MathLabViewModel();
    private AnswerCanvasPage page;
    private final List<String> sent = new java.util.concurrent.CopyOnWriteArrayList<>();  // to PDF Studio, latex|result

    private void openPage() throws Exception {
        EvaluateController controller = new EvaluateController(new EvaluateInteractor(
                new InMemoryFunctionDataAccessObject(), new EvaluatePresenter(viewModel)));
        SwingUtilities.invokeAndWait(() -> {
            page = new AnswerCanvasPage(shared, controller, viewModel,
                    (AnswerCanvasPage.FormulaResultSink) (id, latex, result) -> sent.add(latex + "|" + result));
            page.setPopupDelay(100);
            shared.attachTo(page);
            page.setSize(900, 560);
            layout(page);
        });
    }

    // validate() is a no-op without a window, lay out by hand
    private static void layout(java.awt.Container c) {
        c.doLayout();
        for (java.awt.Component child : c.getComponents()) {
            if (child instanceof java.awt.Container) {
                layout((java.awt.Container) child);
            }
        }
    }

    // ink box (100,100)-(200,150)
    private void write() throws Exception {
        drag(100, 100, 200, 150);
    }

    // a stroke right on the formula
    private void editFormula() throws Exception {
        drag(205, 100, 205, 150);
    }

    private void drag(int... xy) throws Exception {
        SwingUtilities.invokeAndWait(() -> stroke(xy));
    }

    // EDT only
    private void stroke(int... xy) {
        canvas.dispatchEvent(new MouseEvent(canvas, MouseEvent.MOUSE_PRESSED, 0, 0, xy[0], xy[1], 1, false, MouseEvent.BUTTON1));
        for (int i = 2; i < xy.length; i += 2) {
            canvas.dispatchEvent(new MouseEvent(canvas, MouseEvent.MOUSE_DRAGGED, 0, 0, xy[i], xy[i + 1], 1, false, MouseEvent.BUTTON1));
        }
        canvas.dispatchEvent(new MouseEvent(canvas, MouseEvent.MOUSE_RELEASED, 0, 0,
                xy[xy.length - 2], xy[xy.length - 1], 1, false, MouseEvent.BUTTON1));
    }

    // a "1" then an "=" in one go, ink box (100,100)-(145,150)
    private void writeWithEquals() throws Exception {
        onEdt(() -> {
            stroke(100, 100, 100, 150);
            stroke(115, 120, 145, 120);
            stroke(115, 130, 145, 130);
        });
    }

    // lasso round a box, stays on lasso after
    private void lasso(int left, int top, int right, int bottom) throws Exception {
        onEdt(() -> canvas.setTool(DrawingCanvas.Tool.LASSO));
        drag(left, top, right, top, right, bottom, left, bottom);
    }

    private void onEdt(Runnable r) throws Exception {
        SwingUtilities.invokeAndWait(r);
    }

    private AnswerPopup popup() throws Exception {
        AtomicReference<AnswerPopup> seen = new AtomicReference<>();
        onEdt(() -> seen.set(page.visiblePopup()));
        return seen.get();
    }

    private void waitPopup(String latex) throws Exception {
        waitFor(() -> page.visiblePopup() != null && latex.equals(page.visiblePopup().getLatex()), "popup with " + latex);
    }

    private void clickCalculate() throws Exception {
        onEdt(() -> page.visiblePopup().clickCalculate());
    }

    // wait up to 5s for bg work, check on EDT
    private void waitFor(BooleanSupplier condition, String what) throws Exception {
        AtomicBoolean met = new AtomicBoolean();
        for (int i = 0; i < 250 && !met.get(); i++) {
            Thread.sleep(20);
            onEdt(() -> met.set(condition.getAsBoolean()));
        }
        assertTrue(met.get(), "timed out waiting for " + what);
    }

    private List<CanvasAnswer> answers() throws Exception {
        AtomicReference<List<CanvasAnswer>> seen = new AtomicReference<>();
        onEdt(() -> seen.set(shared.getAnswers()));
        return seen.get();
    }

    private boolean answerIs(String text) {
        List<CanvasAnswer> all = canvas.getAnswers();
        return all.size() == 1 && text.equals(all.get(0).getText());
    }

    private boolean errorShown(String text) {
        String shown = page.notices().shownError();
        return shown != null && shown.contains(text);
    }

    @Test
    void popupShowsFormulaAndWaitsForCalculate() throws Exception {
        shared.setFormulaRecognizer(image -> "5\\times7=");
        openPage();
        write();
        assertNull(popup(), "not before the pause");

        waitPopup("5\\times7=");
        AnswerPopup p = popup();
        assertTrue(p.isTypeset());
        assertTrue(p.hasCalculate());
        Rectangle box = p.getBounds();
        assertFalse(p.visibleBounds().intersects(new Rectangle(100, 100, 101, 51)), "does not cover the formula");  // glass margin is see-through
        assertTrue(new Rectangle(0, 0, p.getParent().getWidth(), p.getParent().getHeight()).contains(box), "stays in view");
        Thread.sleep(300);
        assertTrue(answers().isEmpty(), "nothing until Calculate");
    }

    @Test
    void calculateClickWritesTheAnswer() throws Exception {
        shared.setFormulaRecognizer(image -> "\\frac{1}{2}+2^{3}");
        openPage();
        write();
        Thread.sleep(300);
        assertNull(popup(), "no = yet");
        lasso(80, 80, 220, 170);
        waitPopup("\\frac{1}{2}+2^{3}");
        AtomicReference<String> working = new AtomicReference<>();
        onEdt(() -> {
            page.visiblePopup().clickCalculate();
            working.set(page.notices().shownProgress());  // answer can't land before this
        });
        assertEquals("Calculating \\frac{1}{2}+2^{3}…", working.get());
        assertNull(popup(), "popup goes when clicked");

        waitFor(() -> answerIs("= 8.5"), "the answer");
        CanvasAnswer a = answers().get(0);
        assertFalse(a.isError());
        assertTrue(a.getX() > 200, "right of the ink");
        assertNull(page.notices().shownError());
        waitFor(() -> page.notices().shownProgress() == null, "the Calculating card to go");
    }

    @Test
    void trailingEqualsAnswerGoesRight() throws Exception {
        shared.setFormulaRecognizer(image -> "1+1=");
        openPage();
        write();
        waitPopup("1+1=");
        clickCalculate();

        waitFor(() -> answerIs("2"), "the answer");
        assertTrue(answers().get(0).getX() > 200);
    }

    @Test
    void equationIsSolvedBelow() throws Exception {
        shared.setFormulaRecognizer(image -> "2x+1=7");
        openPage();
        write();
        waitPopup("2x+1=7");
        clickCalculate();

        waitFor(() -> answerIs("x = 3"), "the solution");
        assertTrue(answers().get(0).getY() > 150, "below the ink");
    }

    @Test
    void penOrToolChangeClosesThePopup() throws Exception {
        shared.setFormulaRecognizer(image -> "1+1=");
        openPage();
        write();
        waitPopup("1+1=");

        drag(500, 400, 520, 420);  // writes somewhere else
        assertNull(popup());
        waitPopup("1+1=");          // the new ink gets its own
        onEdt(() -> canvas.setTool(DrawingCanvas.Tool.ERASER));
        assertNull(popup());
        Thread.sleep(300);
        assertNull(popup());
        assertTrue(answers().isEmpty());
    }

    @Test
    void panAndZoomKeepThePopup() throws Exception {
        shared.setFormulaRecognizer(image -> "1+1=");
        openPage();
        write();
        waitPopup("1+1=");
        Point before = popup().getLocation();

        onEdt(() -> canvas.panBy(30, -20));  // up, so it stays below the ink
        assertNotNull(popup());
        Point after = popup().getLocation();
        assertEquals(before.x + 30, after.x);
        assertEquals(before.y - 20, after.y);
        onEdt(() -> canvas.zoomAt(450, 250, 1.5));
        assertNotNull(popup());
    }

    @Test
    void editingAnAnsweredFormulaPopsUpAgain() throws Exception {
        AtomicReference<String> latex = new AtomicReference<>("1+1=");
        shared.setFormulaRecognizer(image -> latex.get());
        openPage();
        write();
        waitPopup("1+1=");
        clickCalculate();
        waitFor(() -> answerIs("2"), "the first answer");

        latex.set("1+2=");
        editFormula();
        assertTrue(answers().isEmpty(), "old answer gone at once");
        waitPopup("1+2=");
        Thread.sleep(300);
        assertTrue(answers().isEmpty(), "no auto recalc");
        clickCalculate();
        waitFor(() -> answerIs("3"), "the new answer");
    }

    @Test
    void lateRecognitionAfterAnotherActionIsDropped() throws Exception {
        CountDownLatch release = new CountDownLatch(1);
        AtomicInteger calls = new AtomicInteger();
        shared.setFormulaRecognizer(image -> {
            calls.incrementAndGet();
            release.await();
            return "1+1=";
        });
        openPage();
        write();
        waitFor(() -> calls.get() == 1, "recognition to start");
        onEdt(() -> canvas.setTool(DrawingCanvas.Tool.LASSO));
        release.countDown();
        Thread.sleep(300);
        assertNull(popup(), "stale popup never shows");
    }

    @Test
    void recognitionProblemShowsReasonWithoutCalculate() throws Exception {
        openPage();
        writeWithEquals();
        waitFor(() -> errorShown("Try writing it a bit clearer"), "the reason");
        assertNull(popup(), "legacy error popup must not appear");
        assertTrue(answers().isEmpty());
    }

    @Test
    void anErrorFromTheModelShowsPlainWords() throws Exception {
        shared.setFormulaRecognizer(image -> {
            throw new UnsatisfiedLinkError("no native lib");
        });
        openPage();
        writeWithEquals();
        waitFor(() -> errorShown("Try writing it a bit clearer"), "plain words, no guts");
        assertNull(popup(), "legacy error popup must not appear");
    }

    @Test
    void uncalculableOnlyShowsTheReason() throws Exception {
        shared.setFormulaRecognizer(image -> "\\int");
        openPage();
        write();
        lasso(80, 80, 220, 170);
        waitPopup("\\int");
        clickCalculate();

        waitFor(() -> errorShown("Integrals not supported"), "the reason");
        assertTrue(answers().isEmpty(), "no red ?");
    }

    @Test
    void lineBreakGivesAWarningAndNoAnswer() throws Exception {
        shared.setFormulaRecognizer(image -> "x+1=3\\\\x=2");
        openPage();
        write();
        waitPopup("x+1=3\\\\x=2");
        clickCalculate();
        waitFor(() -> errorShown("Not a calculation"), "the warning");
        assertTrue(answers().isEmpty());
    }

    @Test
    void functionDefinitionOnlyShowsAMessage() throws Exception {
        shared.setFormulaRecognizer(image -> "f(x)=x^{2}");
        openPage();
        write();
        waitPopup("f(x)=x^{2}");
        clickCalculate();

        waitFor(this::definedShown, "the message");
        assertTrue(answers().isEmpty());
    }

    // A f(x)=x^2+2x (50 tall), B f(2)= (30 tall) far away
    private void defineThenCall() {
        shared.setFormulaRecognizer(image -> image.getHeight() < 80 ? "f(2)=" : "f(x)=x^{2}+2x");
    }

    private void autoDefine() throws Exception {
        DefinitionCheckController check = new DefinitionCheckController(new DefinitionCheckInteractor());
        onEdt(() -> page.setDefinitionCheck(check::isDefinition));
    }

    private boolean definedShown() {
        String done = page.notices().lastSuccess();
        return page.notices().shownError() == null && done != null && done.startsWith("Defined");
    }

    @Test
    void definitionIsSavedWithoutCalculate() throws Exception {
        defineThenCall();
        openPage();
        autoDefine();
        write();
        waitFor(this::definedShown, "the Defined message");
        assertNull(popup(), "nothing to click");

        drag(400, 300, 420, 330);
        waitPopup("f(2)=");
        clickCalculate();
        waitFor(() -> answerIs("8"), "f(2)");
    }

    @Test
    void definitionWrittenRightBeforeTheCallIsSavedFirst() throws Exception {
        defineThenCall();
        openPage();
        autoDefine();
        onEdt(() -> page.setPopupDelay(400));
        write();                                     // f(x)=...
        drag(400, 300, 420, 330);                    // f(2)= right after, no pause
        waitPopup("f(2)=");
        clickCalculate();
        waitFor(() -> answerIs("8"), "f(2)");
        Thread.sleep(400);
        assertNull(popup(), "f(x) line doesn't pop up after");
    }

    @Test
    void badDefinitionStillGetsThePopup() throws Exception {
        shared.setFormulaRecognizer(image -> "f(x)=x+y");
        openPage();
        autoDefine();
        write();
        waitPopup("f(x)=x+y");
        assertFalse(errorShown("f(x)"), "no error till Calculate");
        clickCalculate();
        waitFor(() -> errorShown("f(x) can only use x, not y"), "the error");
    }

    @Test
    void autoSavedDefinitionGoesToPdfStudio() throws Exception {
        shared.setFormulaRecognizer(image -> "f(x)=x^{2}+2x");
        openPage();
        autoDefine();
        write();
        waitFor(this::definedShown, "the Defined message");
        waitFor(() -> sent.equals(List.of("f(x)=x^{2}+2x|")), "sent once, no answer");
    }

    @Test
    void errorAfterCalculateStillGoesToPdfStudio() throws Exception {
        shared.setFormulaRecognizer(image -> "x+1=");
        openPage();
        write();
        waitPopup("x+1=");
        clickCalculate();
        waitFor(() -> page.notices().shownError() != null, "the error");
        waitFor(() -> sent.equals(List.of("x+1=|")), "sent w/ no answer");
    }

    @Test
    void halfWrittenInkNeverGoesToPdfStudio() throws Exception {
        shared.setFormulaRecognizer(image -> "1+");
        openPage();
        autoDefine();
        write();
        Thread.sleep(400);
        assertNull(popup());
        assertTrue(sent.isEmpty());
    }

    @Test
    void definitionWithNoBodyYetWaitsQuietly() throws Exception {
        shared.setFormulaRecognizer(image -> "f(x)=");
        openPage();
        autoDefine();
        write();
        waitPopup("f(x)=");
        assertNull(page.notices().shownError());
        assertNull(page.notices().lastSuccess());
    }

    // which notice cards are really sitting in the window
    private static List<String> cardsIn(javax.swing.JRootPane root) {
        List<String> names = new java.util.ArrayList<>();
        for (java.awt.Component c : root.getLayeredPane().getComponents()) {
            if (c.getName() != null && c.getName().startsWith("glass-notice-")) {
                names.add(c.getName());
            }
        }
        return names;
    }

    @Test
    void errorCardReallyLandsInTheWindow() throws Exception {
        shared.setFormulaRecognizer(image -> "x+1=");
        openPage();
        javax.swing.JRootPane root = new javax.swing.JRootPane();
        onEdt(() -> {
            root.setSize(900, 600);
            root.getLayeredPane().setSize(900, 600);
            root.getContentPane().add(page);
        });
        write();
        waitPopup("x+1=");
        clickCalculate();
        waitFor(() -> page.notices().shownError() != null, "the error");
        Thread.sleep(300);
        AtomicReference<List<String>> cards = new AtomicReference<>();
        onEdt(() -> cards.set(cardsIn(root)));
        assertTrue(cards.get().contains("glass-notice-error"), "cards: " + cards.get());
    }

    @Test
    void errorCardStaysTillTheFormulaIsFixed() throws Exception {
        AtomicReference<String> latex = new AtomicReference<>("x+1=");
        shared.setFormulaRecognizer(image -> latex.get());
        openPage();
        write();
        waitPopup("x+1=");
        clickCalculate();
        waitFor(() -> page.notices().shownError() != null, "the error");
        Thread.sleep(300);
        assertNotNull(page.notices().shownError(), "doesn't time out");

        latex.set("1+1=");
        editFormula();
        waitFor(() -> page.notices().shownError() == null, "card gone once they fix it");
        waitPopup("1+1=");
        clickCalculate();
        waitFor(() -> answerIs("2"), "the answer");
        assertNull(page.notices().shownError());
    }

    @Test
    void answerAfterAnErrorClearsTheCard() throws Exception {
        shared.setFormulaRecognizer(image -> image.getHeight() < 80 ? "1+1=" : "x+1=");
        openPage();
        write();
        waitPopup("x+1=");
        clickCalculate();
        waitFor(() -> page.notices().shownError() != null, "the error");

        drag(400, 300, 420, 330);
        waitPopup("1+1=");
        clickCalculate();
        waitFor(() -> page.notices().shownError() == null, "stale error gone");
    }

    @Test
    void undoToAnsweredStateDoesNotPopUp() throws Exception {
        AtomicInteger calls = new AtomicInteger();
        shared.setFormulaRecognizer(image -> {
            calls.incrementAndGet();
            return "1+1=";
        });
        openPage();
        write();
        waitPopup("1+1=");
        clickCalculate();
        waitFor(() -> answerIs("2"), "the answer");

        editFormula();
        onEdt(shared::undo);
        Thread.sleep(400);
        assertEquals(1, calls.get());
        assertNull(popup());
        assertTrue(answerIs("2"));
    }

    @Test
    void undoWithUnansweredInkDoesNotPopUp() throws Exception {
        AtomicInteger calls = new AtomicInteger();
        shared.setFormulaRecognizer(image -> {
            calls.incrementAndGet();
            return "1+1=";
        });
        openPage();
        write();
        waitPopup("1+1=");
        editFormula();
        onEdt(shared::undo);
        Thread.sleep(400);
        assertNull(popup());
        assertEquals(1, calls.get());
    }

    @Test
    void graphPageInFrontNeverRecognises() throws Exception {
        AtomicInteger calls = new AtomicInteger();
        shared.setFormulaRecognizer(image -> {
            calls.incrementAndGet();
            return "1+1=";
        });
        openPage();
        CanvasPage[] graph = new CanvasPage[1];
        onEdt(() -> {
            graph[0] = new CanvasPage(shared) { };
            shared.attachTo(graph[0]);
        });
        write();
        Thread.sleep(400);
        assertEquals(0, calls.get());
        assertNull(popup());

        onEdt(() -> shared.attachTo(page));
        Thread.sleep(300);
        assertEquals(0, calls.get(), "coming back does not pop up by itself");
        assertNull(popup());
    }

    @Test
    void switchingPageClosesThePopup() throws Exception {
        shared.setFormulaRecognizer(image -> "1+1=");
        openPage();
        write();
        waitPopup("1+1=");
        onEdt(() -> shared.attachTo(new CanvasPage(shared) { }));
        assertNull(popup());
    }

    @Test
    void secondFormulaGetsItsOwnPopup() throws Exception {
        AtomicReference<BufferedImage> seen = new AtomicReference<>();
        shared.setFormulaRecognizer(image -> {
            seen.set(image);
            return "1+1=";
        });
        openPage();
        write();                                     // A: 50 tall
        waitPopup("1+1=");
        clickCalculate();
        waitFor(() -> answerIs("2"), "A's answer");

        drag(400, 300, 420, 330);                    // B: 30 tall, far away
        waitFor(() -> page.visiblePopup() != null && seen.get().getHeight() == 30 + 1 + 2 * 18, "B's popup");
        assertTrue(popup().getX() > 300, "popup sits by B");
        clickCalculate();
        waitFor(() -> canvas.getAnswers().size() == 2, "both answers");
    }

    @Test
    void formulaWrittenBeforeThePauseIsOfferedNext() throws Exception {
        shared.setFormulaRecognizer(image -> "1+1=");
        openPage();
        onEdt(() -> page.setPopupDelay(400));
        write();                                     // A
        drag(400, 300, 420, 330);                    // B right after, no pause in between
        waitFor(() -> page.visiblePopup() != null, "B's popup");
        assertTrue(popup().getX() > 300, "newest one first");
        clickCalculate();
        waitFor(() -> canvas.getAnswers().size() == 1, "B's answer");

        waitFor(() -> page.visiblePopup() != null, "A's popup");
        assertTrue(popup().getX() < 300, "popup sits by A");
        clickCalculate();
        waitFor(() -> canvas.getAnswers().size() == 2, "both answers");
    }

    @Test
    void noEqualsNoPopup() throws Exception {
        AtomicInteger calls = new AtomicInteger();
        shared.setFormulaRecognizer(image -> {
            calls.incrementAndGet();
            return "1+1";
        });
        openPage();
        write();
        waitFor(() -> calls.get() == 1, "the read");
        Thread.sleep(300);
        assertNull(popup());
    }

    @Test
    void writingTheEqualsBringsThePopup() throws Exception {
        AtomicReference<String> latex = new AtomicReference<>("1+1");
        shared.setFormulaRecognizer(image -> latex.get());
        openPage();
        write();
        Thread.sleep(300);
        assertNull(popup(), "still writing");

        latex.set("1+1=");
        editFormula();
        waitPopup("1+1=");
    }

    @Test
    void problemWithNoEqualsInTheInkStaysQuiet() throws Exception {
        openPage();
        write();
        Thread.sleep(400);
        assertNull(popup());
    }

    @Test
    void lassoPickJoinsFarApartInk() throws Exception {
        AtomicReference<BufferedImage> seen = new AtomicReference<>();
        shared.setFormulaRecognizer(image -> {
            seen.set(image);
            return "1+1";
        });
        openPage();
        write();                       // (100,100)-(200,150)
        drag(600, 100, 700, 150);      // way past the join gap
        Thread.sleep(300);
        assertNull(popup());
        assertEquals(2, shared.getFormulas().size(), "two bits on their own");

        lasso(80, 80, 720, 170);
        waitPopup("1+1");
        assertEquals(1, shared.getFormulas().size(), "one formula now");
        assertTrue(seen.get().getWidth() > 600, "read as one, gap and all");
        clickCalculate();
        waitFor(() -> answerIs("= 2"), "the answer");
        assertTrue(answers().get(0).getX() > 700, "right of the whole pick");
    }

    @Test
    void lassoOnAnAnsweredFormulaKeepsTheAnswer() throws Exception {
        AtomicInteger calls = new AtomicInteger();
        shared.setFormulaRecognizer(image -> {
            calls.incrementAndGet();
            return "1+1=";
        });
        openPage();
        write();
        waitPopup("1+1=");
        clickCalculate();
        waitFor(() -> answerIs("2"), "the answer");

        lasso(80, 80, 220, 170);
        Thread.sleep(400);
        assertNull(popup());
        assertEquals(1, calls.get(), "not read again");
        assertTrue(answerIs("2"));
    }

    // mouse comes to rest at (x, y), no button down
    private void hover(int x, int y) throws Exception {
        onEdt(() -> canvas.dispatchEvent(new MouseEvent(canvas, MouseEvent.MOUSE_MOVED, 0, 0, x, y, 0, false)));
    }

    private void writeWithEqualsAt(int x, int y) throws Exception {
        onEdt(() -> {
            stroke(x, y, x, y + 50);
            stroke(x + 15, y + 20, x + 45, y + 20);
            stroke(x + 15, y + 30, x + 45, y + 30);
        });
    }

    @Test
    void hoverWaitsOutABusyMoment() throws Exception {
        shared.setFormulaRecognizer(image -> image.getHeight() < 60 ? "x" : "1+1=");
        openPage();
        writeWithEquals();
        waitPopup("1+1=");
        onEdt(() -> canvas.setTool(DrawingCanvas.Tool.ERASER));
        waitFor(() -> page.visiblePopup() == null, "popup closed");

        onEdt(() -> {
            canvas.setTool(DrawingCanvas.Tool.PEN);
            page.setPopupDelay(700);
        });
        drag(400, 300, 600, 300);   // some other ink, its pause is still running
        hover(130, 125);            // rest on A's "=" and don't move again
        waitPopup("1+1=");
        assertTrue(popup().getX() < 300, "popup sits by A");
    }

    @Test
    void hoverTakesOverAnotherFormulasPopup() throws Exception {
        shared.setFormulaRecognizer(image -> "1+1=");
        openPage();
        writeWithEquals();                  // A
        waitPopup("1+1=");
        writeWithEqualsAt(400, 300);        // B
        waitFor(() -> page.visiblePopup() != null && page.visiblePopup().getX() > 300, "B's popup");

        hover(130, 125);                    // A's "="
        waitFor(() -> page.visiblePopup() != null && page.visiblePopup().getX() < 300, "A's popup");
    }

    // y=2x+3x^2 roughly as handwritten, image coords x 0.6
    private void writeQuadratic() throws Exception {
        int[][] strokes = {
            {103, 118, 103, 170, 125, 225}, {160, 118, 150, 190, 110, 280, 57, 340},
            {190, 185, 245, 185}, {190, 217, 265, 217},
            {313, 148, 365, 150, 380, 190, 375, 230, 330, 270, 300, 293, 405, 283},
            {425, 200, 445, 197, 470, 230, 500, 265, 530, 280}, {515, 198, 475, 240, 452, 290},
            {595, 207, 690, 207}, {657, 153, 657, 293},
            {752, 140, 830, 145, 845, 185, 800, 205, 845, 235, 830, 265, 720, 272},
            {868, 205, 905, 212, 940, 240, 978, 282}, {970, 208, 925, 230, 885, 270},
            {1000, 118, 1020, 125, 1015, 140, 988, 152, 1045, 152},
        };
        for (int[] s : strokes) {
            int[] xy = new int[s.length];
            for (int i = 0; i < s.length; i++) {
                xy[i] = (int) Math.round(s[i] * 0.6);
            }
            drag(xy);
            Thread.sleep(40);
        }
    }

    @Test
    void hoverAfterClickingThePopupAway() throws Exception {
        shared.setFormulaRecognizer(image -> image.getWidth() > 200 ? "y=2x+3x^{2}" : ".");
        openPage();
        writeQuadratic();
        waitPopup("y=2x+3x^{2}");
        drag(500, 450, 500, 450);           // click empty canvas w/ the pen
        waitFor(() -> page.visiblePopup() == null, "popup closed");
        Thread.sleep(400);

        hover(130, 120);                    // on the "="
        waitPopup("y=2x+3x^{2}");
    }

    @Test
    void savedDefinitionStillPopsUpOnHover() throws Exception {
        shared.setFormulaRecognizer(image -> image.getWidth() > 200 ? "f(x)=2x+3x^{2}" : ".");
        openPage();
        autoDefine();
        writeQuadratic();
        waitFor(this::definedShown, "the Defined message");
        assertNull(popup());
        Thread.sleep(400);

        hover(130, 120);                    // on the "=", Graph lives in this popup
        waitPopup("f(x)=2x+3x^{2}");
    }

    @Test
    void hoverDoesntWaitForAStrayDotsPause() throws Exception {
        AtomicInteger dotReads = new AtomicInteger();
        shared.setFormulaRecognizer(image -> {
            if (image.getWidth() > 200) {
                return "y=2x+3x^{2}";
            }
            dotReads.incrementAndGet();
            return "\\cdot";
        });
        openPage();
        writeQuadratic();
        waitPopup("y=2x+3x^{2}");
        onEdt(() -> page.setPopupDelay(3000));
        drag(500, 450, 500, 450);           // click the popup away, leaves a dot

        hover(130, 120);
        Thread.sleep(800);                  // way before the dot's pause is up
        AnswerPopup p = popup();
        assertNotNull(p, "popup right away");
        assertEquals("y=2x+3x^{2}", p.getLatex());
        assertEquals(0, dotReads.get());
    }

    @Test
    void hoverWaitsWhenNewInkLandsOnThatFormula() throws Exception {
        shared.setFormulaRecognizer(image -> "1+1=");
        openPage();
        writeWithEquals();
        waitPopup("1+1=");
        onEdt(() -> page.setPopupDelay(1500));
        drag(150, 100, 150, 150);           // another stroke right on it

        hover(130, 125);
        Thread.sleep(700);
        assertNull(popup(), "not the old read, the ink is changing");
        waitPopup("1+1=");
    }

    @Test
    void hoverWorksWithTheLasso() throws Exception {
        shared.setFormulaRecognizer(image -> "1+1=");
        openPage();
        writeWithEquals();
        waitPopup("1+1=");
        onEdt(() -> canvas.setTool(DrawingCanvas.Tool.LASSO));
        waitFor(() -> page.visiblePopup() == null, "popup closed");

        hover(130, 125);
        waitPopup("1+1=");
    }

    @Test
    void draggingThePickAwayCancelsThePopup() throws Exception {
        AtomicInteger calls = new AtomicInteger();
        shared.setFormulaRecognizer(image -> {
            calls.incrementAndGet();
            return "1+1";
        });
        openPage();
        onEdt(() -> page.setPopupDelay(400));
        write();
        waitFor(() -> calls.get() == 1, "the read");
        lasso(80, 80, 220, 170);
        drag(150, 120, 300, 300);      // moves it before the pause is up
        Thread.sleep(600);
        assertNull(popup());
        assertEquals(1, calls.get(), "never read as a pick");
    }

    // board page: real graph + calculate checks, graphing itself never runs
    private void openBoard() throws Exception {
        InMemoryFunctionDataAccessObject functions = new InMemoryFunctionDataAccessObject();
        EvaluateController controller = new EvaluateController(new EvaluateInteractor(
                functions, new EvaluatePresenter(viewModel)));
        GraphCheckInteractor graphCheck = new GraphCheckInteractor(new LatexFunctionParser());
        CalculateCheckInteractor calcCheck = new CalculateCheckInteractor(functions);
        SwingUtilities.invokeAndWait(() -> {
            page = new AnswerCanvasPage(shared, controller, viewModel, (id, latex, result) -> { },
                    new GraphController(input -> { }), new GraphViewModel(), graphCheck::canGraph,
                    java.awt.Color.WHITE);
            page.setCalculateCheck(calcCheck::canCalculate);
            page.setPopupDelay(100);
            shared.attachTo(page);
            page.setSize(900, 560);
            layout(page);
        });
    }

    private void boardPopupFor(String latex) throws Exception {
        shared.setFormulaRecognizer(image -> latex);
        openBoard();
        write();
        waitPopup(latex);
    }

    @Test
    void sumOnlyGetsCalculate() throws Exception {
        boardPopupFor("1+1=");
        assertTrue(popup().hasCalculate());
        assertFalse(popup().hasGraph());
    }

    @Test
    void lineOnlyGetsGraph() throws Exception {
        boardPopupFor("y=3x");
        assertFalse(popup().hasCalculate());
        assertTrue(popup().hasGraph());
    }

    @Test
    void flatLineGetsBoth() throws Exception {
        boardPopupFor("y=2+3");
        assertTrue(popup().hasCalculate());
        assertTrue(popup().hasGraph());
    }
}
