package pdf_editor.adapter;

import org.junit.jupiter.api.Test;
import pdf_editor.MathLibrary;
import view.DrawingCanvas;
import workspace.domain.CanvasState;

import javax.imageio.ImageIO;
import java.awt.event.MouseEvent;
import java.io.ByteArrayInputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/** Regression tests for pen width getting lost in ink exports to Library and PDF. */
class SelectedInkArtworkTest {
    @Test void libraryInkRoundTripPreservesThinVersusThickPen() throws Exception {
        var thinCanvas = selectedStroke(2f, 0xff000000);
        var thickCanvas = selectedStroke(12f, 0xff000000);
        var library = new MathLibrary();
        var thin = library.addInk("Thin", SelectedInkArtwork.render(thinCanvas, true));
        var thick = library.addInk("Thick", SelectedInkArtwork.render(thickCanvas, true));
        var thinPng = ImageIO.read(new ByteArrayInputStream(thin.imagePng()));
        var thickPng = ImageIO.read(new ByteArrayInputStream(thick.imagePng()));

        int midX = (18 + 40) * 3;
        int middleY = 18 * 3;
        assertEquals(thinPng.getWidth(), thickPng.getWidth());
        assertTrue(alpha(thinPng, midX, middleY) > 200);
        assertTrue(alpha(thickPng, midX, middleY) > 200);
        assertEquals(0, alpha(thinPng, midX, middleY + 10));
        assertTrue(alpha(thickPng, midX, middleY + 10) > 200);
    }

    @Test void pdfDragArtworkRetainsVariableWidthAndTransparentBackground() throws Exception {
        var thin = SelectedInkArtwork.render(selectedStroke(2f, 0xff000000), false);
        var thick = SelectedInkArtwork.render(selectedStroke(12f, 0xff000000), false);
        int midX = (18 + 40) * 3, middleY = 18 * 3;
        assertEquals(0, alpha(thin, 0, 0));
        assertEquals(0, alpha(thick, 0, 0));
        assertEquals(0, alpha(thin, midX, middleY + 16));
        assertTrue(alpha(thick, midX, middleY + 16) > 200);
    }

    @Test void severalStrokesKeepIndependentWidthsInOneExport() throws Exception {
        DrawingCanvas canvas = new DrawingCanvas();
        canvas.setSize(500, 300);
        var small = new CanvasState.Ink(List.of(new CanvasState.Point(100, 100, 1),
                new CanvasState.Point(180, 100, 2)), 0xff000000, 2f);
        var large = new CanvasState.Ink(List.of(new CanvasState.Point(100, 160, 3),
                new CanvasState.Point(180, 160, 4)), 0xff000000, 12f);
        canvas.restoreCanvasState(new CanvasState(List.of(small, large), List.of(), List.of(), 1, 0, 0));
        canvas.setTool(DrawingCanvas.Tool.RECTANGLE);
        mouse(canvas, MouseEvent.MOUSE_PRESSED, 60, 60);
        mouse(canvas, MouseEvent.MOUSE_DRAGGED, 230, 195);
        mouse(canvas, MouseEvent.MOUSE_RELEASED, 230, 195);
        assertEquals(2, canvas.selectedInkSnapshot().size());
        var composite = SelectedInkArtwork.render(canvas, false);
        int x = (18 + 40) * 3;
        assertEquals(0, alpha(composite, x, 18 * 3 + 10));
        assertTrue(alpha(composite, x, (18 + 60) * 3 + 10) > 200);
    }

    @Test void installedPdfDragHandlerExportsTheCorrectWidth() throws Exception {
        DrawingCanvas canvas = selectedStroke(12f, 0xff000000);
        pdf_editor.view.CanvasPdfDragController.install(canvas);
        var handler = canvas.getTransferHandler();
        // The actual Swing TransferHandler uses the rendering adapter at drag start.
        var method = handler.getClass().getDeclaredMethod("createTransferable", javax.swing.JComponent.class);
        method.setAccessible(true);
        var transferable = (java.awt.datatransfer.Transferable) method.invoke(handler, canvas);
        assertTrue(transferable.isDataFlavorSupported(java.awt.datatransfer.DataFlavor.imageFlavor));
        var image = (java.awt.image.BufferedImage) transferable.getTransferData(
                java.awt.datatransfer.DataFlavor.imageFlavor);
        assertTrue(alpha(image, (18 + 40) * 3, 18 * 3 + 10) > 200);
    }

    @Test void originalInkColorSurvivesLibraryRendering() throws Exception {
        var canvas = selectedStroke(8f, 0xffff2222);
        var rendered = SelectedInkArtwork.render(canvas, true);
        int rgb = rendered.getRGB((18 + 40) * 3, 18 * 3);
        assertEquals(0xff, (rgb >>> 16) & 0xff);
        assertEquals(0x22, (rgb >>> 8) & 0xff);
        assertEquals(0x22, rgb & 0xff);
    }

    @Test void answerIsIncludedForLibraryButNotForRawPdfInkSelection() throws Exception {
        DrawingCanvas canvas = new DrawingCanvas();
        canvas.setSize(500, 300);
        var ink = new CanvasState.Ink(List.of(new CanvasState.Point(100, 100, 1),
                new CanvasState.Point(180, 100, 2)), 0xff000000, 6f);
        var formula = new CanvasState.Formula(List.of(0), "7", false,
                230, 90, 20, 24, 16f, 0xff000000);
        canvas.restoreCanvasState(new CanvasState(List.of(ink), List.of(formula), List.of(), 1, 0, 0));
        canvas.setTool(DrawingCanvas.Tool.RECTANGLE);
        mouse(canvas, MouseEvent.MOUSE_PRESSED, 60, 60);
        mouse(canvas, MouseEvent.MOUSE_DRAGGED, 200, 150);
        mouse(canvas, MouseEvent.MOUSE_RELEASED, 200, 150);
        assertEquals(1, canvas.selectedInkSnapshot().size());
        assertTrue(SelectedInkArtwork.render(canvas, true).getWidth()
                > SelectedInkArtwork.render(canvas, false).getWidth());
    }

    private static DrawingCanvas selectedStroke(float width, int argb) throws Exception {
        var canvas = new DrawingCanvas();
        canvas.setSize(500, 300);
        var ink = new CanvasState.Ink(List.of(new CanvasState.Point(100, 100, 1),
                new CanvasState.Point(180, 100, 2)), argb, width);
        canvas.restoreCanvasState(new CanvasState(List.of(ink), List.of(), List.of(), 1, 0, 0));
        canvas.setTool(DrawingCanvas.Tool.RECTANGLE);
        mouse(canvas, MouseEvent.MOUSE_PRESSED, 60, 60);
        mouse(canvas, MouseEvent.MOUSE_DRAGGED, 230, 140);
        mouse(canvas, MouseEvent.MOUSE_RELEASED, 230, 140);
        assertEquals(1, canvas.selectedInkSnapshot().size());
        return canvas;
    }

    private static void mouse(DrawingCanvas canvas, int id, int x, int y) {
        var event = new MouseEvent(canvas, id, 1000L, 0, x, y, 1, false,
                id == MouseEvent.MOUSE_DRAGGED ? MouseEvent.NOBUTTON : MouseEvent.BUTTON1);
        if (id == MouseEvent.MOUSE_DRAGGED) {
            for (var listener : canvas.getMouseMotionListeners()) listener.mouseDragged(event);
        } else if (id == MouseEvent.MOUSE_PRESSED) {
            for (var listener : canvas.getMouseListeners()) listener.mousePressed(event);
        } else {
            for (var listener : canvas.getMouseListeners()) listener.mouseReleased(event);
        }
    }

    private static int alpha(java.awt.image.BufferedImage image, int x, int y) {
        return image.getRGB(x, y) >>> 24;
    }
}
