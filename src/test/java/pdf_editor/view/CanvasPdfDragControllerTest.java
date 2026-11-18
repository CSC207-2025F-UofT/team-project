package pdf_editor.view;

import org.junit.jupiter.api.Test;
import view.DrawingCanvas;
import java.awt.Point;
import java.awt.event.MouseEvent;
import static org.junit.jupiter.api.Assertions.*;

/** Crossing the workspace boundary is the only gesture that starts native PDF transfer. */
class CanvasPdfDragControllerTest {
    @Test void movementWithinCanvasNeverStartsPdfTransfer() {
        Point start = new Point(100, 100);
        assertFalse(CanvasPdfDragController.shouldTransfer(start, new Point(500, 150), 600));
        assertFalse(CanvasPdfDragController.shouldTransfer(start, new Point(100, 300), 600));
        assertFalse(CanvasPdfDragController.shouldTransfer(start, new Point(102, 103), 600));
    }
    @Test void transferBeginsOnlyAcrossRightBoundary() {
        Point start = new Point(100, 100);
        assertFalse(CanvasPdfDragController.shouldTransfer(start, new Point(599, 150), 600));
        assertTrue(CanvasPdfDragController.shouldTransfer(start, new Point(600, 150), 600));
        assertTrue(CanvasPdfDragController.shouldTransfer(start, new Point(635, 150), 600));
        assertFalse(CanvasPdfDragController.shouldTransfer(null, new Point(635, 150), 600));
    }
    @Test void selectedInkCanStillMoveInsideCanvasAfterDragSupportInstalled() {
        DrawingCanvas canvas = new DrawingCanvas();
        canvas.setSize(600, 400);
        mouse(canvas, MouseEvent.MOUSE_PRESSED, 75, 75);
        mouse(canvas, MouseEvent.MOUSE_DRAGGED, 95, 95);
        mouse(canvas, MouseEvent.MOUSE_RELEASED, 95, 95);
        canvas.setTool(DrawingCanvas.Tool.RECTANGLE);
        mouse(canvas, MouseEvent.MOUSE_PRESSED, 55, 55);
        mouse(canvas, MouseEvent.MOUSE_DRAGGED, 120, 120);
        mouse(canvas, MouseEvent.MOUSE_RELEASED, 120, 120);
        assertEquals(1, canvas.selectedInkSnapshot().size());
        double before = canvas.selectedInkSnapshot().get(0).getPoints().get(0).getX();
        CanvasPdfDragController.install(canvas);
        mouse(canvas, MouseEvent.MOUSE_PRESSED, 85, 85);
        mouse(canvas, MouseEvent.MOUSE_DRAGGED, 120, 85);
        mouse(canvas, MouseEvent.MOUSE_RELEASED, 120, 85);
        double after = canvas.selectedInkSnapshot().get(0).getPoints().get(0).getX();
        assertEquals(before + 35, after, 0.001, "Normal Canvas move must survive PDF drag installation");
    }
    private static void mouse(DrawingCanvas canvas, int id, int x, int y) {
        int button = id == MouseEvent.MOUSE_DRAGGED ? MouseEvent.NOBUTTON : MouseEvent.BUTTON1;
        MouseEvent event = new MouseEvent(canvas, id, System.currentTimeMillis(),
                0, x, y, 1, false, button);
        if (id == MouseEvent.MOUSE_DRAGGED) {
            for (var l : canvas.getMouseMotionListeners()) l.mouseDragged(event);
        } else if (id == MouseEvent.MOUSE_PRESSED) {
            for (var l : canvas.getMouseListeners()) l.mousePressed(event);
        } else {
            for (var l : canvas.getMouseListeners()) l.mouseReleased(event);
        }
    }
}
