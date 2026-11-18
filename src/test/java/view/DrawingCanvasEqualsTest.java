package view;

import entity.Stroke;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.event.MouseEvent;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

// "=" handling on the canvas: capture w/o it, lone = joining, moves, readable size
class DrawingCanvasEqualsTest {
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

    // bind everything written as of now, like the pause does
    private FormulaCapture bindAll() {
        List<Stroke> all = canvas.getStrokes();
        return canvas.registerNewInk(all.get(all.size() - 1), all);
    }

    // same, the last n strokes as this round
    private FormulaCapture bindLast(int n) {
        List<Stroke> all = canvas.getStrokes();
        return canvas.registerNewInk(all.get(all.size() - 1), all.subList(all.size() - n, all.size()));
    }

    // "11" 16 tall at x 100..110, y 100..116, bound
    private void shrunkOneOne() {
        drag(100, 100, 100, 116);
        drag(110, 100, 110, 116);
        bindAll();
    }

    // strokes in the formula holding the first "1"
    private int firstFormulaSize() {
        return canvas.getFormulas().stream().filter(f -> f.getStrokes().contains(canvas.getStrokes().get(0)))
                .findFirst().orElseThrow().getStrokes().size();
    }

    @Test
    void normalEqualsAfterAShrunkFormulaJoinsIt() {
        shrunkOneOne();
        drag(135, 101, 165, 101);
        drag(135, 115, 165, 115);
        bindLast(2);
        assertEquals(1, canvas.getFormulas().size());
        assertEquals(4, firstFormulaSize());
    }

    @Test
    void equalsOnAnotherRowStaysApart() {
        shrunkOneOne();
        drag(135, 201, 165, 201);
        drag(135, 215, 165, 215);
        bindLast(2);
        assertEquals(2, firstFormulaSize());
    }

    @Test
    void farAwayEqualsStaysApart() {
        shrunkOneOne();
        drag(300, 101, 330, 101);
        drag(300, 115, 330, 115);
        bindLast(2);
        assertEquals(2, firstFormulaSize());
    }

    @Test
    void formulaAndFarEqualsInOneGoJoin() {
        drag(100, 100, 100, 116);
        drag(110, 100, 110, 116);
        drag(135, 101, 165, 101);
        drag(135, 115, 165, 115);
        bindAll();
        assertEquals(1, canvas.getFormulas().size());
    }

    @Test
    void captureCarriesTheInkWithoutTheTrailingEquals() {
        drag(100, 100, 100, 150);
        drag(115, 120, 145, 120);
        drag(115, 130, 145, 130);
        assertNotNull(bindAll().getWithoutEquals());
    }

    @Test
    void noTrailingPairNoSecondImage() {
        drag(100, 100, 100, 150);
        drag(115, 120, 145, 120);
        assertNull(bindAll().getWithoutEquals());
    }
}
