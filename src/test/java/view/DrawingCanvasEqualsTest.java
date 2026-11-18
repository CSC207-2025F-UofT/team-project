package view;

import entity.Stroke;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.event.MouseEvent;
import java.util.List;

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
