package pdf_editor.view;

import interface_adapter.history.CanvasHistorySelection;
import org.junit.jupiter.api.Test;
import view.DrawingCanvas;

import javax.swing.TransferHandler;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import static org.junit.jupiter.api.Assertions.*;

class CanvasHistoryToPdfTransferTest {
    private static BufferedImage image(int w, int h) {
        BufferedImage result = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        result.setRGB(2, 2, 0xffaabbcc);
        return result;
    }

    @Test void historyPictureIsEligibleForBoundaryCopyButCanStillMoveWithinCanvas() {
        DrawingCanvas canvas = new DrawingCanvas();
        canvas.setSize(600, 400);
        canvas.setTool(DrawingCanvas.Tool.RECTANGLE);
        canvas.addHistoryAsset(image(160, 80), "x+1", "2", new Point(80, 80));
        assertTrue(canvas.canCopySelectedHistoryAt(new Point(120, 110)));
        assertFalse(canvas.canCopySelectedInkAt(new Point(120, 110)));
        CanvasPdfDragController.install(canvas);
        assertNotNull(canvas.getTransferHandler());
        // Inside Canvas, the pre-existing selection-move logic still owns the gesture.
        mouse(canvas, MouseEvent.MOUSE_PRESSED, 120, 110);
        mouse(canvas, MouseEvent.MOUSE_DRAGGED, 165, 125);
        mouse(canvas, MouseEvent.MOUSE_RELEASED, 165, 125);
        assertTrue(canvas.canCopySelectedHistoryAt(new Point(165, 125)));
        assertEquals(1, canvas.getPictureCount());
        assertEquals("x+1", canvas.selectedHistoryFormula());
        assertEquals("2", canvas.selectedHistoryResult());
        assertTrue(CanvasPdfDragController.shouldTransfer(new Point(165, 125), new Point(601, 125), 600));
    }

    @Test void historyTransferKeepsStructuredFormulaAndOriginalGraphPixels() throws Exception {
        CanvasHistorySelection payload = new CanvasHistorySelection(java.util.List.of(
                new CanvasHistorySelection.Asset(image(160, 80), "x+1", "2", 30, 50, 80, 40),
                new CanvasHistorySelection.Asset(image(320, 180), "", "", 180, 50, 320, 180)));
        var transfer = new CanvasPdfDragController.HistoryTransferable(payload);
        assertTrue(transfer.isDataFlavorSupported(CanvasPdfDragController.HISTORY_SELECTION_FLAVOR));
        assertFalse(transfer.isDataFlavorSupported(java.awt.datatransfer.DataFlavor.imageFlavor));
        assertEquals(payload, transfer.getTransferData(CanvasPdfDragController.HISTORY_SELECTION_FLAVOR));
        assertEquals(0xffaabbcc, payload.assets().get(1).image().getRGB(2, 2));
    }

    @Test void pdfPageAcceptsStructuredCanvasHistoryFlavor() {
        pdf_editor.MathLibrary library = new pdf_editor.MathLibrary();
        pdf_editor.use_case.DocumentEditor doc = new pdf_editor.use_case.DocumentEditor();
        pdf_editor.adapter.PdfEditorController controller = new pdf_editor.adapter.PdfEditorController(
                library, doc, new pdf_editor.use_case.AutoLayoutInteractor(doc),
                new pdf_editor.use_case.PdfExportInteractor((d, f) -> { }, (d, dpi) -> java.util.List.of()));
        PdfPageCanvas page = new PdfPageCanvas(controller, new PdfPageCanvas.Listener() {
            @Override public void contextMenu(MouseEvent e) { }
            @Override public void editNote() { }
            @Override public void editFormula() { }
        });
        CanvasHistorySelection selection = new CanvasHistorySelection(java.util.List.of(
                new CanvasHistorySelection.Asset(image(150, 60), "x+1", "2", 15, 25, 100, 40)));
        TransferHandler.TransferSupport support = new TransferHandler.TransferSupport(
                page, new CanvasPdfDragController.HistoryTransferable(selection));
        assertTrue(page.getTransferHandler().canImport(support));
    }

    @Test void inkTransferAndBoundaryRuleAreStillSupported() {
        assertFalse(CanvasPdfDragController.shouldTransfer(new Point(100, 100), new Point(540, 140), 600));
        assertTrue(CanvasPdfDragController.shouldTransfer(new Point(100, 100), new Point(600, 140), 600));
        assertFalse(CanvasPdfDragController.shouldTransfer(new Point(100, 100), new Point(100, 103), 600));
    }

    private static void mouse(DrawingCanvas canvas, int id, int x, int y) {
        int button = id == MouseEvent.MOUSE_DRAGGED ? MouseEvent.NOBUTTON : MouseEvent.BUTTON1;
        MouseEvent e = new MouseEvent(canvas, id, System.currentTimeMillis(),
                0, x, y, 1, false, button);
        if (id == MouseEvent.MOUSE_DRAGGED) {
            for (var l : canvas.getMouseMotionListeners()) l.mouseDragged(e);
        } else if (id == MouseEvent.MOUSE_PRESSED) {
            for (var l : canvas.getMouseListeners()) l.mousePressed(e);
        } else {
            for (var l : canvas.getMouseListeners()) l.mouseReleased(e);
        }
    }
}
