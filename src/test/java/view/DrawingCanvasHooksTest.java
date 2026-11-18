package view;

import entity.Stroke;
import interface_adapter.AnswerKind;
import org.junit.jupiter.api.Test;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

// the extra listeners the answer popup leans on
class DrawingCanvasHooksTest {
    private final DrawingCanvas canvas = new DrawingCanvas();

    DrawingCanvasHooksTest() {
        canvas.setSize(900, 500);
    }

    private void drag(int button, int... xy) {
        canvas.dispatchEvent(new MouseEvent(canvas, MouseEvent.MOUSE_PRESSED, 0, 0, xy[0], xy[1], 1, false, button));
        for (int i = 2; i < xy.length; i += 2) {
            canvas.dispatchEvent(new MouseEvent(canvas, MouseEvent.MOUSE_DRAGGED, 0, 0, xy[i], xy[i + 1], 1, false, button));
        }
        canvas.dispatchEvent(new MouseEvent(canvas, MouseEvent.MOUSE_RELEASED, 0, 0,
                xy[xy.length - 2], xy[xy.length - 1], 1, false, button));
    }

    private void write(int... xy) {
        drag(MouseEvent.BUTTON1, xy);
    }

    private Stroke last() {
        List<Stroke> all = canvas.getStrokes();
        return all.get(all.size() - 1);
    }

    @Test
    void strokeListenerGetsTheFinishedStroke() {
        List<Stroke> seen = new ArrayList<>();
        canvas.addStrokeListener(seen::add);
        write(100, 100, 150, 120);
        assertEquals(1, seen.size());
        assertSame(last(), seen.get(0));
    }

    @Test
    void pressToolAndEditsCountAsUserActions() {
        AtomicInteger actions = new AtomicInteger();
        canvas.addUserActionListener(actions::incrementAndGet);
        write(100, 100, 150, 120);
        assertEquals(1, actions.get());
        canvas.setTool(DrawingCanvas.Tool.LASSO);
        assertEquals(2, actions.get());
        canvas.setTool(DrawingCanvas.Tool.LASSO);
        assertEquals(2, actions.get(), "same tool is not an action");
        canvas.setEraserMode(DrawingCanvas.EraserMode.STROKE);
        assertEquals(3, actions.get());
        canvas.clear();
        assertEquals(4, actions.get());
    }

    @Test
    void rightDragPanIsNotAUserAction() {
        AtomicInteger actions = new AtomicInteger();
        AtomicInteger views = new AtomicInteger();
        canvas.addUserActionListener(actions::incrementAndGet);
        canvas.addViewListener(views::incrementAndGet);
        drag(MouseEvent.BUTTON3, 100, 100, 140, 130);
        assertEquals(0, actions.get());
        assertTrue(views.get() > 0);
    }

    @Test
    void deleteSelectionIsAUserAction() {
        write(100, 100, 150, 120);
        AtomicInteger actions = new AtomicInteger();
        canvas.setTool(DrawingCanvas.Tool.LASSO);
        write(80, 80, 180, 80, 180, 140, 80, 140);
        canvas.addUserActionListener(actions::incrementAndGet);
        canvas.deleteSelection();
        assertEquals(1, actions.get());
        assertTrue(canvas.isBlank());
    }

    @Test
    void undoFiresActionAfterFormulaChanges() {
        write(100, 100, 200, 150);
        assertNotNull(canvas.registerNewInk(last(), List.of(last())));
        write(205, 100, 205, 150);  // lands on the formula
        write(205, 100, 205, 150);  // and again, so undo leaves it unanswered
        List<String> order = new ArrayList<>();
        canvas.addFormulaChangeListener(id -> order.add("changed"));
        canvas.addUserActionListener(() -> order.add("action"));
        canvas.undo();
        assertEquals("action", order.get(order.size() - 1));
        assertTrue(order.contains("changed"), "undo still tells listeners, like before");
    }

    @Test
    void panAndZoomFireViewListener() {
        AtomicInteger views = new AtomicInteger();
        canvas.addViewListener(views::incrementAndGet);
        canvas.panBy(10, 0);
        canvas.zoomAt(100, 100, 2);
        canvas.resetView();
        assertEquals(3, views.get());
    }

    @Test
    void worldToScreenFollowsPanAndZoom() {
        canvas.panBy(30, 20);
        canvas.zoomAt(30, 20, 2);  // world (0,0) sits at (30,20)
        Rectangle2D r = canvas.worldToScreen(new Rectangle2D.Double(10, 10, 5, 5));
        assertEquals(50, r.getX(), 1e-9);
        assertEquals(40, r.getY(), 1e-9);
        assertEquals(10, r.getWidth(), 1e-9);
    }

    @Test
    void registerNewInkGroupsTouchingStrokesOnly() {
        write(100, 100, 130, 140);
        Stroke a = last();
        write(140, 100, 170, 140);  // right next to it
        Stroke b = last();
        write(500, 400, 530, 440);  // far off
        Stroke far = last();
        FormulaCapture capture = canvas.registerNewInk(b, List.of(a, b, far));
        assertNotNull(capture);
        assertEquals(2, canvas.getFormulas().stream()
                .filter(f -> f.getId() == capture.getId()).findFirst().orElseThrow().getStrokes().size());
        assertTrue(canvas.isBound(a) && canvas.isBound(b) && canvas.isBound(far));
        assertEquals(2, canvas.getFormulas().size(), "far ink is its own formula");
        Rectangle box = canvas.formulaBounds(capture.getId());
        assertEquals(new Rectangle(100, 100, 70, 40), box);
    }

    private int strokesIn(FormulaCapture capture) {
        return canvas.getFormulas().stream()
                .filter(f -> f.getId() == capture.getId()).findFirst().orElseThrow().getStrokes().size();
    }

    @Test
    void registerNewInkKeepsTrailingEqualsWithItsFormula() {
        List<Stroke> session = new ArrayList<>();
        int[][][] ink = {
            {{110, 200}, {110, 260}},              // 1
            {{200, 230}, {240, 230}},              // +
            {{220, 210}, {220, 250}},
            {{270, 200}, {270, 260}},              // 1
            {{300, 222}, {340, 222}},              // =
            {{300, 240}, {340, 240}},              // last stroke is flat
        };
        for (int[][] s : ink) {
            write(s[0][0], s[0][1], s[1][0], s[1][1]);
            session.add(last());
        }
        assertEquals(6, strokesIn(canvas.registerNewInk(last(), session)));
    }

    @Test
    void registerNewInkKeepsSpacedSymbolsTogether() {
        List<Stroke> session = new ArrayList<>();
        int[][][] ink = {
            {{100, 200}, {140, 260}},              // 2-ish, 60 tall
            {{210, 230}, {250, 230}},              // + about 1.2 chars away
            {{230, 210}, {230, 250}},
            {{320, 200}, {350, 260}},              // 3-ish
        };
        for (int[][] s : ink) {
            write(s[0][0], s[0][1], s[1][0], s[1][1]);
            session.add(last());
        }
        assertEquals(4, strokesIn(canvas.registerNewInk(last(), session)));
        assertEquals(1, canvas.getFormulas().size());
    }

    @Test
    void registerNewInkLeavesTheNextLineAlone() {
        write(100, 100, 160, 160);                 // line 1, 60 tall
        Stroke top = last();
        write(100, 220, 160, 280);                 // line 2, a line gap below
        Stroke bottom = last();
        FormulaCapture capture = canvas.registerNewInk(bottom, List.of(top, bottom));
        assertEquals(1, strokesIn(capture));
        assertEquals(2, canvas.getFormulas().size());
    }

    @Test
    void registerNewInkKeepsLimitsOnASum() {
        List<Stroke> session = new ArrayList<>();
        int[][] ink = {
            {140, 55, 140, 85},                              // 1 over Σ
            {160, 55, 180, 85},                              // 0
            {220, 100, 100, 100, 160, 160, 100, 220, 220, 220},  // Σ
            {240, 130, 240, 190},                            // body, 60 tall
            {280, 130, 280, 190},
            {120, 250, 120, 280},                            // n under Σ, half a char down
            {150, 260, 170, 260},                            // =
            {180, 250, 200, 280},                            // 0
        };
        for (int[] s : ink) {
            write(s);
            session.add(last());
        }
        assertEquals(8, strokesIn(canvas.registerNewInk(session.get(4), session)));
        assertEquals(1, canvas.getFormulas().size());
    }

    @Test
    void registerNewInkReturnsNullForGoneStroke() {
        write(100, 100, 130, 140);
        Stroke a = last();
        canvas.clear();
        assertNull(canvas.registerNewInk(a, List.of(a)));
        assertNull(canvas.formulaBounds(12345));
    }

    // one stroke bound + answered, 40 tall at x
    private Stroke answeredAt(int x) {
        write(x, 100, x, 140);
        Stroke s = last();
        FormulaCapture c = canvas.registerNewInk(s, List.of(s));
        assertTrue(canvas.showAnswer(c.getId(), c.getRevision(), AnswerKind.AFTER_EQUALS, "1", false));
        return s;
    }

    private void lasso(int left, int top, int right, int bottom) {
        canvas.setTool(DrawingCanvas.Tool.LASSO);
        write(left, top, right, top, right, bottom, left, bottom);
    }

    @Test
    void lassoAddingMissedInkToAnAnsweredFormulaRebuildsIt() {
        Stroke a = answeredAt(100);
        write(100, 200, 130, 230);                 // missed bit, stays loose
        Stroke missed = last();
        lasso(80, 80, 160, 250);
        FormulaCapture capture = canvas.registerSelection();
        assertNotNull(capture, "user fixed the grouping, read it again");
        assertEquals(2, strokesIn(capture));
        assertTrue(canvas.isBound(a) && canvas.isBound(missed));
        assertTrue(canvas.getAnswers().isEmpty(), "old answer was for the wrong ink");
    }

    @Test
    void lassoOverTwoAnsweredFormulasLeavesThem() {
        answeredAt(100);
        answeredAt(400);
        lasso(80, 80, 600, 200);
        assertNull(canvas.registerSelection());
        assertEquals(2, canvas.getAnswers().size());
        assertEquals(2, canvas.getFormulas().size());
    }

    @Test
    void limitsWrittenAfterThePauseJoinTheirFormula() {
        List<Stroke> body = new ArrayList<>();
        int[][] lim = {{100, 100, 100, 140}, {115, 110, 115, 140}, {130, 110, 130, 140, 145, 112, 160, 140}};
        for (int[] s : lim) {
            write(s);
            body.add(last());
        }
        assertNotNull(canvas.registerNewInk(last(), body));
        List<Stroke> later = new ArrayList<>();
        int[][] limit = {
            {98, 150, 110, 162}, {110, 150, 98, 162},            // x right under the l
            {114, 156, 134, 156}, {128, 152, 134, 156, 128, 160}, // arrow, flat bits
            {140, 150, 140, 162, 148, 162, 148, 150, 140, 150},   // 0
        };
        for (int[] s : limit) {
            write(s);
            later.add(last());
            assertTrue(!canvas.isBound(last()), "waits for the pause");
        }
        FormulaCapture capture = canvas.registerNewInk(last(), later);
        assertEquals(8, strokesIn(capture));
        assertEquals(1, canvas.getFormulas().size());
    }

    @Test
    void lineWrittenUnderAFormulaLaterDoesntSwallowIt() {
        List<Stroke> sum = new ArrayList<>();
        int[][] ink = {
            {251, 200, 200, 200, 229, 232, 200, 264, 251, 264},     // Σ 64
            {261, 222, 261, 246}, {261, 230, 271, 222, 281, 246},    // n
            {317, 214, 323, 212, 331, 220, 317, 254, 339, 254},      // 2, 40
        };
        for (int[] s : ink) {
            write(s);
            sum.add(last());
        }
        assertNotNull(canvas.registerNewInk(last(), sum));
        List<Stroke> line = new ArrayList<>();
        int[][] next = {{205, 290, 210, 285, 210, 325}, {222, 305, 242, 305}, {232, 295, 232, 315}, {250, 290, 255, 285, 255, 325}};
        for (int[] s : next) {
            write(s);                              // full size 1+1, 26 under the Σ
            line.add(last());
        }
        assertEquals(4, strokesIn(canvas.registerNewInk(last(), line)));
        assertEquals(2, canvas.getFormulas().size());
    }

    @Test
    void bothLimitsAfterThePauseComeBackAsOneCurrentFormula() {
        List<Stroke> sum = new ArrayList<>();
        int[][] ink = {
            {251, 200, 200, 200, 229, 232, 200, 264, 251, 264},     // Σ 64
            {261, 222, 261, 246}, {261, 230, 271, 222, 281, 246},    // n
            {317, 214, 323, 212, 331, 220, 317, 254, 339, 254},      // 2, 40
        };
        for (int[] s : ink) {
            write(s);
            sum.add(last());
        }
        assertNotNull(canvas.registerNewInk(last(), sum));
        List<Stroke> later = new ArrayList<>();
        int[][] limits = {
            {237, 160, 223, 160, 222, 173, 236, 178, 230, 190, 221, 188},  // 5 over Σ, loose
            {205, 274, 205, 286, 210, 274, 215, 286},                     // n=0 under it, loose
            {219, 278, 229, 278}, {219, 283, 229, 283},
            {233, 274, 233, 294, 241, 294, 241, 274, 233, 274},
        };
        for (int[] s : limits) {
            write(s);
            later.add(last());
            assertTrue(!canvas.isBound(last()), "both limits wait for the pause");
        }
        FormulaCapture capture = canvas.registerNewInk(last(), later);
        assertTrue(canvas.isCurrent(capture.getId(), capture.getRevision()), "not a formula that got merged again");
        assertEquals(9, strokesIn(capture));
    }

    @Test
    void equalsAfterAFractionStaysInOnePiece() {
        List<Stroke> session = new ArrayList<>();
        int[][] ink = {
            {200, 176, 220, 200}, {220, 176, 200, 200},              // x, x-height
            {225, 184, 245, 184}, {235, 174, 235, 194},              // +
            {257, 168, 265, 160, 265, 200},                          // 1
            {195, 205, 285, 205},                                    // bar
            {230, 220, 236, 212, 250, 218, 230, 252, 252, 252},      // 2
            {300, 207, 322, 207}, {300, 216, 322, 216},              // = at the bar, lower bar last
        };
        for (int[] s : ink) {
            write(s);
            session.add(last());
        }
        assertEquals(9, strokesIn(canvas.registerNewInk(last(), session)));
        assertEquals(1, canvas.getFormulas().size());
    }

    // Σ 120 tall + "n+1" 60 tall
    private static final int[][] SUM = {
        {220, 100, 100, 100, 160, 160, 100, 220, 220, 220},
        {240, 130, 240, 190, 280, 130, 280, 190},
        {300, 160, 340, 160}, {320, 140, 320, 180},
        {360, 130, 360, 190},
    };

    private List<Stroke> writeAll(int[][] ink) {
        List<Stroke> out = new ArrayList<>();
        for (int[] s : ink) {
            write(s);
            out.add(last());
        }
        return out;
    }

    // body + more ink in one go, no pause; more ink should stay its own formula
    private void assertStaysApartFromSum(int[][] more) {
        List<Stroke> session = writeAll(SUM);
        session.addAll(writeAll(more));
        canvas.registerNewInk(last(), session);
        assertEquals(2, canvas.getFormulas().size());
        Stroke sigma = session.get(0);
        assertEquals(SUM.length, canvas.getFormulas().stream()
                .filter(f -> f.getStrokes().contains(sigma)).findFirst().orElseThrow().getStrokes().size());
    }

    @Test
    void lowerLimitHalfACharUnderASumAfterThePauseJoinsIt() {
        List<Stroke> sum = writeAll(SUM);
        assertNotNull(canvas.registerNewInk(last(), sum));
        List<Stroke> later = writeAll(new int[][]{
            {120, 250, 120, 280, 140, 250},                    // n, 30 under the Σ
            {150, 260, 170, 260}, {150, 270, 170, 270},        // =
            {180, 250, 200, 280, 180, 280},                    // 0
        });
        assertEquals(9, strokesIn(canvas.registerNewInk(last(), later)));
        assertEquals(1, canvas.getFormulas().size());
    }

    @Test
    void fullSizeLineUnderASumStaysApart() {
        assertStaysApartFromSum(new int[][]{{120, 260, 120, 320}, {160, 260, 160, 320}});
    }

    @Test
    void lonelyDashUnderASumStaysApart() {
        assertStaysApartFromSum(new int[][]{{120, 250, 200, 250}});  // next line's "-"
    }

    @Test
    void smallInkOffToTheSideOfASumStaysApart() {
        assertStaysApartFromSum(new int[][]{{500, 240, 500, 270}});
    }

    // d over a bar over dx, both d's full size, nothing beside it to tie the rows
    private static final int[][] DDX = {
        {85, 40, 86, 100, 87, 140, 91, 152},                    // d stem
        {75, 105, 60, 125, 52, 145, 60, 150, 75, 135, 80, 110},  // d bowl
        {27, 170, 131, 170},                                    // bar
        {70, 180, 70, 230, 69, 290},                            // d stem
        {60, 235, 37, 240, 34, 260, 35, 280, 50, 272, 62, 250},  // d bowl
        {77, 275, 95, 252, 117, 246},                           // x
        {90, 247, 100, 257, 107, 274},
    };

    // (x^3/2 - x^2) = after the d/dx
    private static final int[][] REST = {
        {177, 97, 170, 150, 171, 210, 180, 250, 189, 260},      // (
        {225, 117, 240, 112, 262, 147}, {272, 112, 255, 130, 239, 147},  // x
        {280, 77, 297, 80, 290, 90, 300, 97, 292, 105, 280, 104},  // 3
        {216, 168, 305, 169},                                   // bar
        {242, 202, 275, 200, 260, 225, 242, 237, 287, 240},      // 2
        {336, 165, 360, 165},                                   // -
        {392, 155, 405, 150, 427, 194}, {430, 156, 415, 170, 400, 189},  // x
        {435, 134, 450, 135, 442, 142, 452, 144},               // 2
        {464, 89, 472, 140, 472, 180, 460, 225, 433, 257},      // )
        {493, 164, 513, 164}, {493, 185, 513, 185},             // =
    };

    @Test
    void fractionWrittenAloneStaysOneFormula() {
        List<Stroke> ddx = writeAll(DDX);
        assertEquals(DDX.length, strokesIn(canvas.registerNewInk(last(), ddx)));
        assertEquals(1, canvas.getFormulas().size());
    }

    @Test
    void pauseAfterTheFractionKeepsItInTheFormula() {
        List<Stroke> ddx = writeAll(DDX);
        canvas.registerNewInk(last(), ddx);
        List<Stroke> rest = writeAll(REST);
        canvas.registerNewInk(last(), rest);
        assertEquals(DDX.length + REST.length, canvas.getFormulas().stream()
                .filter(f -> f.getStrokes().contains(last())).findFirst().orElseThrow().getStrokes().size());
        assertEquals(1, canvas.getFormulas().size());
    }

    @Test
    void dashUnderAFormulaWithNothingUnderItIsNoFraction() {
        List<Stroke> one = writeAll(new int[][]{{100, 100, 100, 140}});  // 1
        canvas.registerNewInk(last(), one);
        writeAll(new int[][]{{80, 160, 120, 160}});                    // next line's "-", nothing under it
        canvas.registerNewInk(last(), List.of(last()));
        assertEquals(2, canvas.getFormulas().size());
    }
}
