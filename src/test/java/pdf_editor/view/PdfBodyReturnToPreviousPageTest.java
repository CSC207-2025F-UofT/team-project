package pdf_editor.view;

import org.junit.jupiter.api.Test;
import pdf_editor.MathLibrary;
import pdf_editor.adapter.PdfBodyFlowLayout;
import pdf_editor.adapter.PdfEditorController;
import pdf_editor.domain.PdfDocument;
import pdf_editor.use_case.AutoLayoutInteractor;
import pdf_editor.use_case.DocumentEditor;
import pdf_editor.use_case.PdfExportInteractor;

import javax.swing.SwingUtilities;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class PdfBodyReturnToPreviousPageTest {
    @Test void userMayReturnAndEditFirstPageWithoutForcedJumpToSecondPage() throws Exception {
        AtomicInteger forcedOverflow = new AtomicInteger();
        SwingUtilities.invokeAndWait(() -> {
            DocumentEditor editor = new DocumentEditor();
            var body=List.of(new PdfDocument.NoteSpan("Long paragraph. ".repeat(180),
                    false,false,false,16));
            editor.updateFlowingBody(0,PdfBodyFlowLayout.paginate(body));
            assertTrue(editor.pageCount()>=2);
            var controller = new PdfEditorController(new MathLibrary(),editor,
                    new AutoLayoutInteractor(editor),
                    new PdfExportInteractor((doc,path)->{},(doc,dpi)->List.of()));
            var canvas=new PdfPageCanvas(controller,new PdfPageCanvas.Listener() {
                public void contextMenu(MouseEvent event) { }
                public void editNote() { }
                public void editFormula() { }
                public void bodyOverflow() { forcedOverflow.incrementAndGet(); }
            });
            canvas.setSize(canvas.getPreferredSize());
            canvas.editBodyInlineAt(80,100);
            canvas.finishInlineEdit();
            controller.switchPage(1);
            canvas.editBodyInlineAt(80,100);
            canvas.finishInlineEdit();
            controller.switchPage(0);
            canvas.editBodyInlineAt(80,100);
            assertTrue(canvas.isEditingNote());
            assertEquals(0,controller.currentPage());
            canvas.finishInlineEdit();
        });
        SwingUtilities.invokeAndWait(() -> {}); // drain deferred document callbacks
        assertEquals(0,forcedOverflow.get(),"Opening a filled page must not force an automatic page switch");
    }
}
