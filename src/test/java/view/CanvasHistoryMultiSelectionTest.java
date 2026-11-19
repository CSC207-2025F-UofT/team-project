package view;

import org.junit.jupiter.api.Test;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Imported History objects can be selected and manipulated together without touching formula ink. */
class CanvasHistoryMultiSelectionTest {
    @Test
    void boxSelectsTwoHistoryPicturesAndDeletesBothInOneUndoStep() {
        DrawingCanvas canvas = new DrawingCanvas();
        canvas.setSize(800, 500);
        BufferedImage art = new BufferedImage(160, 80, BufferedImage.TYPE_INT_ARGB);
        canvas.addHistoryAsset(art, "a", "1", new Point(100, 100));
        canvas.addHistoryAsset(art, "b", "2", new Point(240, 125));
        assertEquals(2, canvas.getPictureCount());
        canvas.setTool(DrawingCanvas.Tool.RECTANGLE);
        drag(canvas, 75, 75, 390, 240);
        assertEquals(2, canvas.getSelectedPictureCount(), "The box should keep all intersected History objects");
        assertTrue(canvas.hasSelection());
        canvas.deleteSelection();
        assertEquals(0, canvas.getPictureCount());
        canvas.undo();
        assertEquals(2, canvas.getPictureCount());
    }

    @Test
    void movingABoxSelectionMovesBothHistoryObjectsTogether() {
        DrawingCanvas canvas = new DrawingCanvas();
        canvas.setSize(800, 500);
        BufferedImage art = new BufferedImage(160, 80, BufferedImage.TYPE_INT_ARGB);
        canvas.addHistoryAsset(art, "a", "1", new Point(100, 100));
        canvas.addHistoryAsset(art, "b", "2", new Point(240, 125));
        canvas.setTool(DrawingCanvas.Tool.RECTANGLE);
        drag(canvas, 75, 75, 390, 240);
        double first = canvas.exportCanvasState().assets().get(0).x();
        double second = canvas.exportCanvasState().assets().get(1).x();
        // Blank space between two selected items still belongs to their shared bounds.
        drag(canvas, 195, 115, 215, 135);
        assertEquals(first + 20, canvas.exportCanvasState().assets().get(0).x(), 0.01);
        assertEquals(second + 20, canvas.exportCanvasState().assets().get(1).x(), 0.01);
        assertEquals(2, canvas.getSelectedPictureCount());
    }

    @Test
    void freeformLassoKeepsEveryEnclosedHistoryObject() {
        DrawingCanvas canvas = new DrawingCanvas();
        canvas.setSize(800, 500);
        BufferedImage art = new BufferedImage(120, 60, BufferedImage.TYPE_INT_ARGB);
        canvas.addHistoryAsset(art, "a", "1", new Point(100, 100));
        canvas.addHistoryAsset(art, "b", "2", new Point(240, 125));
        canvas.setTool(DrawingCanvas.Tool.LASSO);
        long now = System.currentTimeMillis();
        int[][] points = {{75, 75}, {350, 75}, {350, 240}, {75, 240}, {75, 75}};
        canvas.dispatchEvent(new MouseEvent(canvas, MouseEvent.MOUSE_PRESSED, now, 0,
                points[0][0], points[0][1], 1, false, MouseEvent.BUTTON1));
        for (int i = 1; i < points.length; i++) {
            canvas.dispatchEvent(new MouseEvent(canvas, MouseEvent.MOUSE_DRAGGED, now, 0,
                    points[i][0], points[i][1], 0, false, MouseEvent.BUTTON1));
        }
        canvas.dispatchEvent(new MouseEvent(canvas, MouseEvent.MOUSE_RELEASED, now, 0,
                75, 75, 1, false, MouseEvent.BUTTON1));
        assertEquals(2, canvas.getSelectedPictureCount());
    }


    @Test
    void groupResizeChangesBothImportedAssetsAndUndoRestoresThem() throws Exception {
        DrawingCanvas canvas = new DrawingCanvas();
        canvas.setSize(900, 600);
        BufferedImage art = new BufferedImage(160, 80, BufferedImage.TYPE_INT_ARGB);
        canvas.addHistoryAsset(art, "a", "1", new Point(100, 100));
        canvas.addHistoryAsset(art, "b", "2", new Point(240, 125));
        canvas.setTool(DrawingCanvas.Tool.RECTANGLE);
        drag(canvas, 75, 75, 390, 240);
        assertEquals(2, canvas.getSelectedPictureCount());
        double width1 = canvas.exportCanvasState().assets().get(0).width();
        double width2 = canvas.exportCanvasState().assets().get(1).width();
        var boundsMethod = DrawingCanvas.class.getDeclaredMethod("selectionBounds");
        boundsMethod.setAccessible(true);
        java.awt.Rectangle bounds = (java.awt.Rectangle) boundsMethod.invoke(canvas);
        drag(canvas, bounds.x + bounds.width, bounds.y + bounds.height,
                bounds.x + bounds.width + 35, bounds.y + bounds.height + 25);
        double resized1 = canvas.exportCanvasState().assets().get(0).width();
        double resized2 = canvas.exportCanvasState().assets().get(1).width();
        assertTrue(resized1 > width1 && resized2 > width2);
        assertEquals(resized1 / width1, resized2 / width2, 0.0001,
                "One group resize factor should apply to each imported asset");
        canvas.undo();
        assertEquals(width1, canvas.exportCanvasState().assets().get(0).width(), 0.001);
        assertEquals(width2, canvas.exportCanvasState().assets().get(1).width(), 0.001);
    }

    private static void drag(DrawingCanvas canvas, int x0, int y0, int x1, int y1) {
        long now = System.currentTimeMillis();
        canvas.dispatchEvent(new MouseEvent(canvas, MouseEvent.MOUSE_PRESSED, now, 0,
                x0, y0, 1, false, MouseEvent.BUTTON1));
        canvas.dispatchEvent(new MouseEvent(canvas, MouseEvent.MOUSE_DRAGGED, now, 0,
                x1, y1, 0, false, MouseEvent.BUTTON1));
        canvas.dispatchEvent(new MouseEvent(canvas, MouseEvent.MOUSE_RELEASED, now, 0,
                x1, y1, 1, false, MouseEvent.BUTTON1));
    }
}
