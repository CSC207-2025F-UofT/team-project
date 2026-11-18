package pdf_editor.view;

import org.junit.jupiter.api.Test;
import pdf_editor.MathLibrary;
import pdf_editor.adapter.PdfEditorController;
import pdf_editor.use_case.AutoLayoutInteractor;
import pdf_editor.use_case.DocumentEditor;
import pdf_editor.use_case.PdfExportInteractor;
import view.DrawingCanvas;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.io.ByteArrayInputStream;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

/** Regression: the same toolbar selects tools in both workspaces, without a PDF-mode switch. */
class PdfAutoToolRoutingTest {
    private final MathLibrary library = new MathLibrary();
    private final DocumentEditor editor = new DocumentEditor();
    private final PdfEditorController controller = new PdfEditorController(library, editor,
            new AutoLayoutInteractor(editor), new PdfExportInteractor((document, path) -> {},
            (document, dpi) -> java.util.List.of()));
    private final PdfPageCanvas page = new PdfPageCanvas(controller, new PdfPageCanvas.Listener() {
        public void contextMenu(MouseEvent event) { }
        public void editNote() { }
        public void editFormula() { }
    });
    private final AtomicReference<DrawingCanvas.Tool> tool = new AtomicReference<>(DrawingCanvas.Tool.PEN);

    private void active() {
        page.configurePdfDrawing(true, tool::get, () -> Color.BLACK, () -> 4f,
                () -> DrawingCanvas.EraserMode.PRECISE, () -> 16);
    }

    private void event(int type, int x, int y) {
        page.dispatchEvent(new MouseEvent(page, type, System.currentTimeMillis(),
                MouseEvent.BUTTON1_DOWN_MASK, x, y, 1, false, MouseEvent.BUTTON1));
    }

    @Test void penDrawsAndPreciseEraserOnlyRemovesPixelsWithUndo() throws Exception {
        active();
        event(MouseEvent.MOUSE_PRESSED, 128, 128);
        event(MouseEvent.MOUSE_DRAGGED, 160, 128);
        event(MouseEvent.MOUSE_RELEASED, 160, 128);
        assertEquals(1, controller.placements().size());
        var ink = controller.placements().get(0);
        byte[] initialPng = ink.material().imagePng();
        tool.set(DrawingCanvas.Tool.ERASER);
        event(MouseEvent.MOUSE_PRESSED, 143, 128);
        event(MouseEvent.MOUSE_RELEASED, 143, 128);
        assertEquals(ink.id(), controller.placements().get(0).id(), "Precise erase preserves placement ID");
        var erased = ImageIO.read(new ByteArrayInputStream(controller.placements().get(0).material().imagePng()));
        var original = ImageIO.read(new ByteArrayInputStream(initialPng));
        assertTrue(opaqueCount(erased) < opaqueCount(original));
        assertTrue(controller.undo());
        assertArrayEquals(initialPng, controller.placements().get(0).material().imagePng());
    }

    @Test void selectionToolMovesExistingPdfPlacement() {
        active();
        var placed = controller.place(library.addFormula("x"), 100, 100, 180, 70);
        tool.set(DrawingCanvas.Tool.LASSO);
        event(MouseEvent.MOUSE_PRESSED, 145, 145);
        event(MouseEvent.MOUSE_DRAGGED, 165, 160);
        event(MouseEvent.MOUSE_RELEASED, 165, 160);
        assertEquals(120, editor.find(0, placed.id()).x(), .001);
        assertEquals(115, editor.find(0, placed.id()).y(), .001);
    }

    @Test void boxSelectsAnItemByEnclosingItsCentre() {
        active();
        var placed = controller.place(library.addFormula("x"), 100, 100, 100, 60);
        controller.clearSelection();
        tool.set(DrawingCanvas.Tool.RECTANGLE);
        // All events begin outside the object, and the rectangle encloses its centre.
        event(MouseEvent.MOUSE_PRESSED, 98, 98);
        event(MouseEvent.MOUSE_DRAGGED, 245, 185);
        event(MouseEvent.MOUSE_RELEASED, 245, 185);
        assertNotNull(controller.selected());
        assertEquals(placed.id(), controller.selected().id());
    }

    @Test void freeformLassoMovesMultiplePdfItemsAsOneUndoStep() {
        active();
        var first = controller.place(library.addFormula("x"), 100, 100, 80, 60);
        var second = controller.place(library.addFormula("y"), 220, 100, 80, 60);
        controller.clearSelection();
        tool.set(DrawingCanvas.Tool.LASSO);
        event(MouseEvent.MOUSE_PRESSED, 110, 110);
        event(MouseEvent.MOUSE_DRAGGED, 350, 110);
        event(MouseEvent.MOUSE_DRAGGED, 350, 220);
        event(MouseEvent.MOUSE_DRAGGED, 110, 220);
        event(MouseEvent.MOUSE_RELEASED, 110, 110);
        event(MouseEvent.MOUSE_PRESSED, 140, 140);
        event(MouseEvent.MOUSE_DRAGGED, 160, 155);
        event(MouseEvent.MOUSE_RELEASED, 160, 155);
        assertEquals(120, editor.find(0, first.id()).x(), .001);
        assertEquals(240, editor.find(0, second.id()).x(), .001);
        assertTrue(controller.undo());
        assertEquals(100, editor.find(0, first.id()).x(), .001);
        assertEquals(220, editor.find(0, second.id()).x(), .001);
    }

    @Test void canvasDefaultsToPreciseEraser() {
        assertEquals(DrawingCanvas.EraserMode.PRECISE, new DrawingCanvas().getEraserMode());
    }

    private static int opaqueCount(java.awt.image.BufferedImage image) {
        int count = 0;
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                if ((image.getRGB(x, y) >>> 24) != 0) count++;
            }
        }
        return count;
    }
}
