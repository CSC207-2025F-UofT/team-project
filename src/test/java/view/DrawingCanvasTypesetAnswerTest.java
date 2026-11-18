package view;

import interface_adapter.AnswerKind;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.answer.CanvasAnswer;

import java.awt.event.MouseEvent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DrawingCanvasTypesetAnswerTest {
    private final DrawingCanvas canvas = new DrawingCanvas();

    @BeforeEach
    void setUp() {
        canvas.setSize(900, 500);
    }

    private void drag(int... xy) {
        send(MouseEvent.MOUSE_PRESSED, xy[0], xy[1]);
        for (int i = 2; i < xy.length; i += 2) {
            send(MouseEvent.MOUSE_DRAGGED, xy[i], xy[i + 1]);
        }
        send(MouseEvent.MOUSE_RELEASED, xy[xy.length - 2], xy[xy.length - 1]);
    }

    private void send(int id, int x, int y) {
        canvas.dispatchEvent(new MouseEvent(canvas, id, 0, 0, x, y, 1, false, MouseEvent.BUTTON1));
    }

    // "1=" 40 tall, x 100..170, y 100..140, "=" middle at y 120
    private FormulaCapture writeOneEquals() {
        drag(100, 100, 100, 140);
        drag(130, 115, 170, 115);
        drag(130, 125, 170, 125);
        return canvas.takeFormulasToCalculate().get(0);
    }

    @Test
    void fractionGoesRightOfEqualsWithItsBarOnTheEquals() {
        FormulaCapture c = writeOneEquals();
        assertTrue(canvas.showTypesetAnswer(c.getId(), c.getRevision(), AnswerKind.AFTER_EQUALS, "1/3",
                "\\frac{1}{3}"));
        CanvasAnswer a = canvas.getAnswers().get(0);
        assertTrue(a.isTypeset());
        assertEquals("1/3", a.getText());
        assertTrue(a.getX() > 170, "x " + a.getX());
        assertTrue(a.getY() < 100 && a.getY() + a.getHeight() > 140, "taller than the 1, spans it");
        double centre = a.getY() + a.getHeight() / 2;
        assertEquals(120, centre, 12);
    }

    @Test
    void staleRevisionDrawsNothing() {
        FormulaCapture c = writeOneEquals();
        assertFalse(canvas.showTypesetAnswer(c.getId(), c.getRevision() + 1, AnswerKind.AFTER_EQUALS, "2", "2"));
        assertTrue(canvas.getAnswers().isEmpty());
    }

    @Test
    void savedAndReopenedStaysTypeset() throws Exception {
        FormulaCapture c = writeOneEquals();
        canvas.showTypesetAnswer(c.getId(), c.getRevision(), AnswerKind.AFTER_EQUALS, "1/3", "\\frac{1}{3}");
        DrawingCanvas reopened = new DrawingCanvas();
        reopened.restoreCanvasState(canvas.exportCanvasState());
        CanvasAnswer a = reopened.getAnswers().get(0);
        assertEquals("\\frac{1}{3}", a.getLatex());
        assertEquals(canvas.getAnswers().get(0).getBox(), a.getBox());
    }

    @Test
    void answerUsesThePenTheFormulaWasWrittenWith() throws Exception {
        canvas.setPenWidth(9);
        FormulaCapture c = writeOneEquals();
        canvas.setPenWidth(2);  // switched after writing, answer still matches the formula
        canvas.showTypesetAnswer(c.getId(), c.getRevision(), AnswerKind.AFTER_EQUALS, "2", "2");
        assertEquals(9, canvas.getAnswers().get(0).getPenWidth());
        DrawingCanvas reopened = new DrawingCanvas();
        reopened.restoreCanvasState(canvas.exportCanvasState());
        assertEquals(9, reopened.getAnswers().get(0).getPenWidth());
        assertEquals(canvas.getAnswers().get(0).getBox(), reopened.getAnswers().get(0).getBox());
    }

    @Test
    void plainAnswersGetThePenToo() {
        canvas.setPenWidth(7);
        FormulaCapture c = writeOneEquals();
        canvas.showAnswer(c.getId(), c.getRevision(), AnswerKind.AFTER_EQUALS, "2", false);
        assertEquals(7, canvas.getAnswers().get(0).getPenWidth());
    }

    @Test
    void eraserTakesItOff() {
        FormulaCapture c = writeOneEquals();
        canvas.showTypesetAnswer(c.getId(), c.getRevision(), AnswerKind.AFTER_EQUALS, "2", "2");
        CanvasAnswer a = canvas.getAnswers().get(0);
        canvas.setTool(DrawingCanvas.Tool.ERASER);
        int y = (int) (a.getY() + a.getHeight() / 2);
        drag((int) a.getX() - 5, y, (int) (a.getX() + a.getWidth() + 5), y);
        assertTrue(canvas.getAnswers().isEmpty());
    }
}
