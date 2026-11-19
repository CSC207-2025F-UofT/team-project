package pdf_editor.adapter;

import org.junit.jupiter.api.Test;
import pdf_editor.MathLibrary;
import pdf_editor.domain.PdfDocument;
import pdf_editor.use_case.AutoLayoutInteractor;
import pdf_editor.use_case.DocumentEditor;
import pdf_editor.use_case.PdfExportInteractor;

import java.util.Set;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/** Selection is transient; group mutations and image layer edits must remain atomic/undoable. */
class PdfMultiSelectionAndLayoutTest {
    private final MathLibrary library = new MathLibrary();
    private final DocumentEditor editor = new DocumentEditor();
    private final PdfEditorController controller = new PdfEditorController(library, editor,
            new AutoLayoutInteractor(editor), new PdfExportInteractor((d,p)->{}, (d,dpi)->List.of()));

    @Test void deleteTwoSelectedPlacementsIsOneUndoAndDoesNotDeleteLibraryMaterials() {
        var first = controller.place(library.addFormula("x"), 50, 70, 90, 45);
        var second = controller.place(library.addFormula("y"), 170, 70, 90, 45);
        controller.setSelection(Set.of(first.id(), second.id()), false);
        assertTrue(controller.deleteSelected());
        assertEquals(0, controller.placements().size());
        assertTrue(controller.selectedIds().isEmpty());
        assertEquals(2, library.items().size(), "PDF deletion must not remove source materials");
        assertTrue(controller.undo());
        assertEquals(2, controller.placements().size(), "One Undo restores both placements");
        assertTrue(controller.redo());
        assertTrue(controller.placements().isEmpty());
    }

    @Test void selectionCanToggleAddAndClearWithoutEnteringDocumentUndo() {
        var a = controller.place(library.addFormula("x"), 50, 50, 80, 45);
        var b = controller.place(library.addFormula("y"), 200, 50, 80, 45);
        controller.select(a.id());
        controller.toggleSelection(b.id());
        assertEquals(Set.of(a.id(), b.id()), controller.selectedIds());
        controller.toggleSelection(a.id());
        assertEquals(Set.of(b.id()), controller.selectedIds());
        controller.clearSelection();
        assertTrue(controller.selectedIds().isEmpty());
        controller.selectAll();
        assertEquals(2, controller.selectedIds().size());
        controller.addPage();
        assertTrue(controller.selectedIds().isEmpty(), "Switching to a new page clears the selection");
    }

    @Test void multipleImagesCanMoveBehindTextAndUndoWithOriginalBoundsAndOrder() {
        var note = controller.addNoteAt(70, 70);
        var first = controller.place(library.addFormula("x"), 90, 90, 100, 60);
        var second = controller.place(library.addFormula("y"), 250, 90, 100, 60);
        assertEquals(note.id(), controller.placements().get(0).id());
        controller.setSelection(Set.of(first.id(), second.id()), false);
        assertTrue(controller.layoutSelectedMaterials(false));
        assertEquals(List.of(first.id(), second.id(), note.id()),
                controller.placements().stream().map(PdfDocument.Placement::id).toList());
        assertEquals(90f, editor.find(0, first.id()).x());
        assertTrue(controller.undo());
        assertEquals(note.id(), controller.placements().get(0).id());
        controller.setSelection(Set.of(first.id(), second.id()), false);
        assertFalse(controller.layoutSelectedMaterials(true), "Already in front of the text");
        controller.layoutSelectedMaterials(false);
        controller.layoutSelectedMaterials(true);
        assertEquals(note.id(), controller.placements().get(0).id());
    }
}
