package pdf_editor.view;

import org.junit.jupiter.api.Test;
import pdf_editor.MathLibrary;
import pdf_editor.adapter.PdfEditorController;
import pdf_editor.use_case.AutoLayoutInteractor;
import pdf_editor.use_case.DocumentEditor;
import pdf_editor.use_case.PdfExportInteractor;
import view.DrawingCanvas;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

/** Regression for PDF-side visual feedback, without changing the document until release. */
class PdfToolFeedbackTest {
    private final MathLibrary library = new MathLibrary();
    private final DocumentEditor editor = new DocumentEditor();
    private final PdfEditorController controller = new PdfEditorController(library, editor,
            new AutoLayoutInteractor(editor), new PdfExportInteractor((document, path) -> { },
            (document, dpi) -> java.util.List.of()));
    private final AtomicReference<DrawingCanvas.Tool> tool = new AtomicReference<>(DrawingCanvas.Tool.PEN);
    private final PdfPageCanvas page = new PdfPageCanvas(controller, new PdfPageCanvas.Listener() {
        public void contextMenu(MouseEvent e) { }
        public void editNote() { }
        public void editFormula() { }
    });

    private void event(int kind, int x, int y) {
        int modifiers = kind == MouseEvent.MOUSE_MOVED ? 0 : MouseEvent.BUTTON1_DOWN_MASK;
        page.dispatchEvent(new MouseEvent(page, kind, System.currentTimeMillis(), modifiers,
                x, y, 1, false, kind == MouseEvent.MOUSE_MOVED ? MouseEvent.NOBUTTON : MouseEvent.BUTTON1));
    }

    @SuppressWarnings("unchecked")
    private Map<String, BufferedImage> preview() throws Exception {
        Field field = PdfPageCanvas.class.getDeclaredField("liveErasedInk");
        field.setAccessible(true);
        return (Map<String, BufferedImage>) field.get(page);
    }

    @Test void preciseEraseHasVisiblePreviewBeforeReleaseAndSingleCommit() throws Exception {
        page.configurePdfDrawing(true, tool::get, () -> Color.BLACK, () -> 4f,
                () -> DrawingCanvas.EraserMode.PRECISE, () -> 16);
        event(MouseEvent.MOUSE_PRESSED, 128, 128);
        event(MouseEvent.MOUSE_DRAGGED, 160, 128);
        event(MouseEvent.MOUSE_RELEASED, 160, 128);
        var original = controller.placements().get(0);
        byte[] before = original.material().imagePng();
        tool.set(DrawingCanvas.Tool.ERASER);
        event(MouseEvent.MOUSE_MOVED, 143, 128);
        event(MouseEvent.MOUSE_PRESSED, 143, 128);
        event(MouseEvent.MOUSE_DRAGGED, 149, 128);
        assertTrue(preview().containsKey(original.id()), "Raster preview must exist while dragging");
        assertArrayEquals(before, controller.placements().get(0).material().imagePng(),
                "Preview must not commit before mouse release");
        event(MouseEvent.MOUSE_RELEASED, 149, 128);
        assertTrue(preview().isEmpty(), "Preview must be cleared after commit");
        assertFalse(java.util.Arrays.equals(before, controller.placements().get(0).material().imagePng()));
        assertTrue(controller.undo(), "One undo must restore the finished erase gesture");
        assertArrayEquals(before, controller.placements().get(0).material().imagePng());
    }

    @Test void lassoHighlightsCandidatesWhileDragging() throws Exception {
        page.configurePdfDrawing(true, tool::get, () -> Color.BLACK, () -> 4f);
        var placed = controller.place(library.addFormula("x"), 100, 100, 100, 60);
        tool.set(DrawingCanvas.Tool.LASSO);
        event(MouseEvent.MOUSE_PRESSED, 110, 110);
        event(MouseEvent.MOUSE_DRAGGED, 260, 110);
        event(MouseEvent.MOUSE_DRAGGED, 260, 210);
        event(MouseEvent.MOUSE_DRAGGED, 110, 210);
        Field field = PdfPageCanvas.class.getDeclaredField("selectionCandidates");
        field.setAccessible(true);
        @SuppressWarnings("unchecked") var candidates = (java.util.Set<String>) field.get(page);
        assertTrue(candidates.contains(placed.id()), "PDF item should highlight before mouse release");
        event(MouseEvent.MOUSE_RELEASED, 110, 110);
        assertNotNull(controller.selected());
        assertEquals(placed.id(), controller.selected().id());
        assertTrue(candidates.isEmpty());
    }

    @Test void cursorIconsAreNonEmptyAndToolSpecific() {
        for (var t : new DrawingCanvas.Tool[]{DrawingCanvas.Tool.PEN, DrawingCanvas.Tool.ERASER,
                DrawingCanvas.Tool.LASSO, DrawingCanvas.Tool.RECTANGLE}) {
            BufferedImage img = PdfToolCursors.imageFor(t);
            assertNotNull(img);
            assertEquals(32, img.getWidth());
            assertTrue(hasVisiblePixels(img));
        }
        assertNotEquals(PdfToolCursors.imageFor(DrawingCanvas.Tool.PEN).getRGB(23, 23),
                PdfToolCursors.imageFor(DrawingCanvas.Tool.ERASER).getRGB(23, 23));
    }

    @Test void lassoFillIsVisibleAboveOpaquePdfPageWhileDragging() {
        page.configurePdfDrawing(true, tool::get, () -> Color.BLACK, () -> 4f);
        tool.set(DrawingCanvas.Tool.LASSO);
        page.setSize(700, 800);
        event(MouseEvent.MOUSE_PRESSED, 70, 70);
        event(MouseEvent.MOUSE_DRAGGED, 300, 70);
        event(MouseEvent.MOUSE_DRAGGED, 300, 300);
        event(MouseEvent.MOUSE_DRAGGED, 70, 300);
        BufferedImage frame = new BufferedImage(700, 800, BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics2D g = frame.createGraphics();
        try { page.paint(g); } finally { g.dispose(); }
        Color inside = new Color(frame.getRGB(150, 170), true);
        assertTrue(inside.getBlue() > inside.getRed() + 10,
                "Freeform lasso must show translucent blue selection above the white PDF page");
    }

    @Test void pdfCursorHotspotsMatchTheEditingLocation() {
        assertEquals(new java.awt.Point(9, 23), PdfToolCursors.hotspotFor(DrawingCanvas.Tool.PEN));
        assertEquals(new java.awt.Point(16, 16), PdfToolCursors.hotspotFor(DrawingCanvas.Tool.ERASER));
        assertEquals(new java.awt.Point(16, 16), PdfToolCursors.hotspotFor(DrawingCanvas.Tool.LASSO));
        // The eraser ring surrounds the actual erasing hotspot, not an offset decoration.
        BufferedImage eraser = PdfToolCursors.imageFor(DrawingCanvas.Tool.ERASER);
        assertTrue((eraser.getRGB(16, 8) >>> 24) > 0);
        assertTrue((eraser.getRGB(16, 24) >>> 24) > 0);
    }


    @Test void penAndLassoReuseToolbarMonochromeGlyphs() {
        for (DrawingCanvas.Tool tool : new DrawingCanvas.Tool[]{DrawingCanvas.Tool.PEN,
                DrawingCanvas.Tool.LASSO, DrawingCanvas.Tool.RECTANGLE, DrawingCanvas.Tool.ERASER}) {
            BufferedImage icon = PdfToolCursors.imageFor(tool);
            assertTrue(hasVisiblePixels(icon));
            for (int y = 0; y < icon.getHeight(); y++) for (int x = 0; x < icon.getWidth(); x++) {
                Color pixel = new Color(icon.getRGB(x, y), true);
                if (pixel.getAlpha() == 0) continue;
                assertTrue(Math.abs(pixel.getRed() - pixel.getGreen()) <= 5, "Cursor must not use blue tint");
                // The original toolbar uses neutral near-black (slight RGB differences are permitted).
                assertTrue(Math.abs(pixel.getRed() - pixel.getBlue()) <= 12,
                        "Cursor must keep the toolbar's neutral monochrome style");
            }
        }
    }


    private static boolean hasVisiblePixels(BufferedImage img) {
        for (int y = 0; y < img.getHeight(); y++) for (int x = 0; x < img.getWidth(); x++)
            if ((img.getRGB(x, y) >>> 24) != 0) return true;
        return false;
    }
}
