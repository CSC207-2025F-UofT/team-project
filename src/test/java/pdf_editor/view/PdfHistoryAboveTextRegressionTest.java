package pdf_editor.view;

import history.domain.HistoryEntry;
import interface_adapter.history.HistoryItem;
import org.junit.jupiter.api.Test;
import pdf_editor.MathLibrary;
import pdf_editor.adapter.PdfEditorController;
import pdf_editor.use_case.AutoLayoutInteractor;
import pdf_editor.use_case.DocumentEditor;
import pdf_editor.use_case.PdfExportInteractor;
import view.board.doc.DocEditorPanel;

import java.awt.event.MouseEvent;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/** History/PDF text default layer and hit-testing regressions. */
class PdfHistoryAboveTextRegressionTest {
    private final MathLibrary library = new MathLibrary();
    private final DocumentEditor editor = new DocumentEditor();
    private final PdfEditorController controller = new PdfEditorController(library, editor,
            new AutoLayoutInteractor(editor),
            new PdfExportInteractor((d, file) -> {}, (d, dpi) -> List.of()));

    @Test void textInsertedAfterHistoryDefaultsBelowItAndCanStillBeArranged() {
        var history = controller.place(library.addCalculation("2+3", "5"), 85, 105, 260, 85);
        var note = controller.addNoteAt(70, 70);
        assertEquals(note.id(), controller.placements().get(0).id(), "Text is behind History by default");
        assertEquals(history.id(), controller.placements().get(1).id());
        controller.selectAt(110, 130);
        assertEquals(history.id(), controller.selected().id(), "History must receive the overlapping click");

        // Explicit user ordering is still supported; Undo must restore the default order.
        controller.select(note.id());
        controller.arrange(2);
        controller.selectAt(110, 130);
        assertEquals(note.id(), controller.selected().id());
        assertTrue(controller.undo());
        controller.selectAt(110, 130);
        assertEquals(history.id(), controller.selected().id());
    }

    @Test void historyAddedAfterTextAlsoAppearsAboveText() {
        var note = controller.addNoteAt(70, 70);
        var history = controller.place(library.addFormula("x+1"), 85, 105, 260, 85);
        assertEquals(note.id(), controller.placements().get(0).id());
        assertEquals(history.id(), controller.placements().get(1).id());
        controller.selectAt(110, 130);
        assertEquals(history.id(), controller.selected().id());
    }

    @Test void importingHistoryExitsLiveTextEditorSoPointerCanReachIt() {
        DocEditorPanel panel = new DocEditorPanel(controller);
        try {
            controller.addNoteAt(70, 70);
            PdfPageCanvas page = panel.pageView();
            assertTrue(page.editNoteInline());
            assertTrue(page.isEditingNote());

            panel.addHistoryItem(new HistoryItem(new HistoryEntry("history-1",
                    HistoryEntry.Kind.CALCULATION, "Calculation", "x+1", "2",
                    null, 0, 0, Instant.EPOCH)));

            assertFalse(page.isEditingNote(), "An inline text component must not cover an imported History object");
            assertEquals(2, controller.placements().size());
            assertFalse(controller.selected().isNote());
            String historyId = controller.selected().id();
            controller.clearSelection();
            page.dispatchEvent(new MouseEvent(page, MouseEvent.MOUSE_PRESSED,
                    System.currentTimeMillis(), MouseEvent.BUTTON1_DOWN_MASK,
                    28 + 105, 28 + 125, 1, false, MouseEvent.BUTTON1));
            assertNotNull(controller.selected());
            assertEquals(historyId, controller.selected().id(), "Pointer must hit the History object, not the text box");
        } finally {
            panel.close();
        }
    }
    @Test void fullPageTextEditorAllowsClicksOnHigherHistoryButStillEditsBlankText() {
        var history = controller.place(library.addFormula("y=x^2"), 120, 250, 190, 85);
        var note = controller.addNoteAt(72, 72);
        controller.beginTransform();
        controller.transform(72, 72, 451, 690);

        PdfPageCanvas page = new PdfPageCanvas(controller, new PdfPageCanvas.Listener() {
            public void contextMenu(MouseEvent event) { }
            public void editNote() { }
            public void editFormula() { }
        });
        page.setSize(page.getPreferredSize());
        assertTrue(page.editNoteInline());
        page.doLayout();
        assertEquals(page, javax.swing.SwingUtilities.getDeepestComponentAt(page, 28 + 130, 28 + 260),
                "History overlap must pass through the live JTextPane to the PDF page");
        assertTrue(javax.swing.SwingUtilities.getDeepestComponentAt(page, 28 + 360, 28 + 400)
                instanceof InlineNoteEditor, "Empty text area must remain editable");
        controller.select(note.id());
        page.dispatchEvent(new MouseEvent(page, MouseEvent.MOUSE_PRESSED,
                System.currentTimeMillis(), MouseEvent.BUTTON1_DOWN_MASK,
                28 + 130, 28 + 260, 1, false, MouseEvent.BUTTON1));
        assertNotNull(controller.selected());
        assertEquals(history.id(), controller.selected().id());
        assertFalse(page.isEditingNote());
    }
}
