package view;

import entity.Stroke;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

// "=" handling on the canvas: capture w/o it, lone = joining, moves, readable size
class DrawingCanvasEqualsTest {
    private final DrawingCanvas canvas = new DrawingCanvas();
    private final List<Long> moved = new ArrayList<>();
    private final List<Long> changed = new ArrayList<>();

    @BeforeEach
    void setUp() {
        canvas.setSize(900, 500);
        canvas.addFormulaMoveListener(moved::add);
        canvas.addFormulaChangeListener(changed::add);
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

    // "1 =" at x 100..145, y 100..150, bound; its id
    private long writeAndBindOneEquals() {
        drag(100, 100, 100, 150);
        drag(115, 120, 145, 120);
        drag(115, 130, 145, 130);
        return bindAll().getId();
    }

    private void boxSelectIt() {
        canvas.setTool(DrawingCanvas.Tool.RECTANGLE);
        drag(80, 80, 220, 170);
    }

    @Test
    void movingAWholeFormulaTellsMoveListeners() {
        long id = writeAndBindOneEquals();
        long rev = canvas.getFormulas().get(0).getRevision();
        boxSelectIt();
        drag(130, 125, 230, 225);
        assertEquals(List.of(id), moved);
        assertEquals(rev, canvas.getFormulas().get(0).getRevision());
        assertTrue(changed.isEmpty());
    }

    @Test
    void resizingAWholeFormulaTellsMoveListeners() {
        long id = writeAndBindOneEquals();
        boxSelectIt();
        drag(153, 158, 183, 188);  // bottom-right handle: ink box + 8 pad
        assertEquals(List.of(id), moved);
        assertTrue(canvas.getFormulas().get(0).getCharHeight() > 50, "it got bigger");
    }

    @Test
    void undoAfterAMoveTellsNoOne() {
        writeAndBindOneEquals();
        boxSelectIt();
        drag(130, 125, 230, 225);
        moved.clear();
        canvas.undo();
        assertTrue(moved.isEmpty());
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
