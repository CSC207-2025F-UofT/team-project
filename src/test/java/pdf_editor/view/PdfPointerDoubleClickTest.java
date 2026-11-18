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
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class PdfPointerDoubleClickTest {
    @Test void pointerDoubleClickOpensExistingFormulaEditorNotDrawing() {
        DocumentEditor document = new DocumentEditor();
        MathLibrary library = new MathLibrary();
        var controller = new PdfEditorController(library, document,
                new AutoLayoutInteractor(document),
                new PdfExportInteractor((d, f) -> { }, (d, dpi) -> List.of()));
        var formula = library.addCalculation("2+3", "5");
        controller.place(formula, 75, 108, 320, 90);
        AtomicInteger edit = new AtomicInteger();
        AtomicInteger note = new AtomicInteger();
        PdfPageCanvas page = new PdfPageCanvas(controller, new PdfPageCanvas.Listener() {
            @Override public void contextMenu(MouseEvent event) { }
            @Override public void editNote() { note.incrementAndGet(); }
            @Override public void editFormula() { edit.incrementAndGet(); }
        });
        page.configurePdfDrawing(true, () -> null, () -> Color.BLACK, () -> 2.5f);
        int x = 28 + (int)Math.round(95 * page.zoom());
        int y = 28 + (int)Math.round(135 * page.zoom());
        long when = System.currentTimeMillis();
        page.dispatchEvent(new MouseEvent(page, MouseEvent.MOUSE_PRESSED, when, 0,
                x, y, 1, false, MouseEvent.BUTTON1));
        page.dispatchEvent(new MouseEvent(page, MouseEvent.MOUSE_RELEASED, when+1, 0,
                x, y, 1, false, MouseEvent.BUTTON1));
        page.dispatchEvent(new MouseEvent(page, MouseEvent.MOUSE_CLICKED, when+2, 0,
                x, y, 2, false, MouseEvent.BUTTON1));
        assertEquals(1, edit.get());
        assertEquals(0, note.get());
        assertEquals(1, controller.placements().size(), "Pointer must not create an ink object");
    }
}
