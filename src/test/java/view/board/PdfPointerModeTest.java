package view.board;

import org.junit.jupiter.api.Test;
import pdf_editor.MathLibrary;
import pdf_editor.adapter.PdfEditorController;
import pdf_editor.use_case.AutoLayoutInteractor;
import pdf_editor.use_case.DocumentEditor;
import pdf_editor.use_case.PdfExportInteractor;
import view.DrawingCanvas;

import javax.swing.JPanel;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

class PdfPointerModeTest {
    @Test void pointerDisablesPdfInkAndMarqueeButRetainsNativeSelection() throws Exception {
        DocumentEditor document = new DocumentEditor();
        var controller = new PdfEditorController(new MathLibrary(), document,
                new AutoLayoutInteractor(document),
                new PdfExportInteractor((doc, target) -> { }, (doc, dpi) -> List.of()));
        PdfPanel pdf = new PdfPanel(controller);
        DrawingCanvas canvas = new DrawingCanvas();
        FloatingToolbar toolbar = new FloatingToolbar(canvas, new JPanel(), pdf);
        Method choosePointer = FloatingToolbar.class.getDeclaredMethod("choosePointer");
        choosePointer.setAccessible(true);
        choosePointer.invoke(toolbar);
        Field toolGetter = pdf.editor().pageView().getClass().getDeclaredField("drawingTool");
        toolGetter.setAccessible(true);
        @SuppressWarnings("unchecked")
        Supplier<DrawingCanvas.Tool> active = (Supplier<DrawingCanvas.Tool>) toolGetter.get(pdf.editor().pageView());
        assertNull(active.get(), "PDF Pointer needs normal Swing click/double-click handling");
        assertEquals(DrawingCanvas.Tool.LASSO, canvas.getTool(),
                "Shared DrawingCanvas remains in its existing selection mode");
        assertTrue(pdf.editor().pageView().isPdfDrawingEnabled());
        canvas.setTool(DrawingCanvas.Tool.PEN);
        assertEquals(DrawingCanvas.Tool.PEN, active.get(), "Switching back resumes drawing");
    }
}
