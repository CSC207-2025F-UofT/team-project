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
import java.util.List;
import java.util.Set;
import javax.swing.SwingUtilities;

import static org.junit.jupiter.api.Assertions.*;

/** Exercise the actual Swing input route, not just the controller's Set mutators. */
class PdfPageMultiSelectTest {
    private final MathLibrary library = new MathLibrary();
    private final DocumentEditor editor = new DocumentEditor();
    private final PdfEditorController controller = new PdfEditorController(library, editor,
            new AutoLayoutInteractor(editor), new PdfExportInteractor((d,p)->{}, (d,dpi)->List.of()));
    private final PdfPageCanvas page = new PdfPageCanvas(controller, new PdfPageCanvas.Listener() {
        @Override public void contextMenu(MouseEvent e) { }
        @Override public void editNote() { }
        @Override public void editFormula() { }
    });
    private void event(int type, int x, int y) {
        page.dispatchEvent(new MouseEvent(page, type, System.currentTimeMillis(),
                MouseEvent.BUTTON1_DOWN_MASK, x, y, 1, false, MouseEvent.BUTTON1));
    }

    @Test void boxSelectAndMoveTwoHistoryObjectsTogetherWithOneUndo() {
        var a = controller.place(library.addFormula("a"), 100, 120, 100, 60);
        var b = controller.place(library.addFormula("b"), 280, 120, 100, 60);
        page.configurePdfDrawing(true, () -> DrawingCanvas.Tool.RECTANGLE, () -> Color.BLACK, () -> 3f);
        event(MouseEvent.MOUSE_PRESSED, 65, 65);
        event(MouseEvent.MOUSE_DRAGGED, 440, 300);
        event(MouseEvent.MOUSE_RELEASED, 440, 300);
        assertEquals(Set.of(a.id(), b.id()), controller.selectedIds());
        event(MouseEvent.MOUSE_PRESSED, 28+135, 28+145);
        event(MouseEvent.MOUSE_DRAGGED, 28+175, 28+165);
        event(MouseEvent.MOUSE_RELEASED, 28+175, 28+165);
        assertEquals(140f, editor.find(0, a.id()).x());
        assertEquals(320f, editor.find(0, b.id()).x());
        assertTrue(controller.undo());
        assertEquals(100f, editor.find(0, a.id()).x());
        assertEquals(280f, editor.find(0, b.id()).x());
    }

    @Test void escapeAndBlankClickClearAllBlueSelection() {
        var a = controller.place(library.addFormula("x"), 100, 100, 110, 55);
        var b = controller.place(library.addFormula("y"), 250, 100, 110, 55);
        controller.setSelection(Set.of(a.id(), b.id()), false);
        event(MouseEvent.MOUSE_PRESSED, 52, 52);
        assertTrue(controller.selectedIds().isEmpty());
        controller.setSelection(Set.of(a.id(), b.id()), false);
        page.clearPageSelection();
        assertTrue(controller.selectedIds().isEmpty());
    }
}
