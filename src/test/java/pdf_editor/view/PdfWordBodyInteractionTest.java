package pdf_editor.view;

import org.junit.jupiter.api.Test;
import pdf_editor.MathLibrary;
import pdf_editor.adapter.PdfEditorController;
import pdf_editor.use_case.*;
import javax.swing.*;
import java.awt.event.MouseEvent;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class PdfWordBodyInteractionTest {
    @Test void typingAndPaginationUseThePageBodyNotALargeSelectionBlockingTextBox() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            var editor = new DocumentEditor();
            var controller = new PdfEditorController(new MathLibrary(),editor,
                    new AutoLayoutInteractor(editor),new PdfExportInteractor((d,p)->{},(d,dpi)->List.of()));
            var page = new PdfPageCanvas(controller,new PdfPageCanvas.Listener() {
                public void contextMenu(MouseEvent e) {}
                public void editNote() {}
                public void editFormula() {}
            });
            page.setSize(page.getPreferredSize());
            page.editBodyInline();
            assertEquals(0, controller.placements().size());
            InlineNoteEditor body = java.util.Arrays.stream(page.getComponents())
                    .filter(c -> c instanceof InlineNoteEditor).map(c -> (InlineNoteEditor)c)
                    .findFirst().orElseThrow();
            assertNull(body.getBorder(),"No rectangular Text Box frame");
            body.setText("Paragraph\n".repeat(100));
            page.finishInlineEdit();
            assertTrue(controller.pageCount()>=2);
            assertTrue(controller.placements().isEmpty());
            assertTrue(controller.undo());
            assertTrue(controller.body().isEmpty());
        });
    }
}
