package pdf_editor.view;

import org.junit.jupiter.api.Test;
import pdf_editor.MathLibrary;
import pdf_editor.adapter.PdfEditorController;
import pdf_editor.domain.PdfDocument;
import pdf_editor.use_case.*;
import view.board.doc.DocEditorPanel;
import javax.swing.text.StyleConstants;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class WordDocumentTypingTest {
    private PdfEditorController controller() {
        var editor=new DocumentEditor();
        return new PdfEditorController(new MathLibrary(),editor,new AutoLayoutInteractor(editor),
                new PdfExportInteractor((d,f)->{},(d,dpi)->List.of()));
    }
    @Test void defaultDocumentBodyIsBorderlessAndNotASelectableFloatingTextBox() {
        var controller=controller();var view=new DocEditorPanel(controller);
        try {
            view.typeOnPage();
            assertTrue(controller.placements().isEmpty(), "Body is not a floating note placement");
            assertEquals(72f, PdfDocument.BODY_LEFT);
            assertEquals(451f, PdfDocument.BODY_WIDTH);
            var editors=java.util.Arrays.stream(view.pageView().getComponents())
                    .filter(c -> c instanceof InlineNoteEditor).toList();
            assertEquals(1, editors.size(), "One borderless body editor");
            assertNull(((InlineNoteEditor)editors.get(0)).getBorder());
            view.typeOnPage();
            assertEquals(1,java.util.Arrays.stream(view.pageView().getComponents())
                    .filter(c -> c instanceof InlineNoteEditor).count());
            view.pageView().finishInlineEdit();
            assertEquals(0, controller.placements().size());
        } finally { view.close(); }
    }
    @Test void fontFamilyIsPreservedThroughNoteSpans() {
        var original=new PdfDocument.NoteSpan("hello",true,false,false,16,"Georgia");
        var changed=NoteFormatting.toggle(List.of(original),"hello",16,NoteFormatting.Style.ITALIC);
        assertEquals("Georgia",changed.get(0).fontFamily());
        assertTrue(changed.get(0).italic());
        assertEquals("Georgia",NoteFormatting.resize(changed,"hello",16,24).get(0).fontFamily());
    }
    @Test void newNotesDefaultToTimesNewRoman() {
        var editor=new DocumentEditor();var note=editor.addNote(0,"Test",72,72);
        var pane=pdf_editor.adapter.NoteTextDocumentFactory.createPane(note);
        assertEquals("Times New Roman",StyleConstants.getFontFamily(
                pane.getStyledDocument().getCharacterElement(0).getAttributes()));
    }
}
