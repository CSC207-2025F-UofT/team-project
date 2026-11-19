package view.board.doc;

import org.junit.jupiter.api.Test;
import pdf_editor.MathLibrary;
import pdf_editor.adapter.PdfEditorController;
import pdf_editor.use_case.AutoLayoutInteractor;
import pdf_editor.use_case.DocumentEditor;
import pdf_editor.use_case.PdfExportInteractor;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/** Library rows and the scrolling pane must not paint opaque white rectangles. */
class LibraryTransparentSurfaceTest {
    @Test void libraryRowsAndViewportAreTransparent() {
        DocumentEditor editor = new DocumentEditor();
        MathLibrary library = new MathLibrary();
        library.addFormula("x^2");
        PdfEditorController controller = new PdfEditorController(library, editor,
                new AutoLayoutInteractor(editor),
                new PdfExportInteractor((doc,path)->{}, (doc,dpi)->List.of()));
        DocEditorPanel panel = new DocEditorPanel(controller);
        try {
            List<JList<?>> lists = new ArrayList<>();
            findLists(panel, lists);
            assertEquals(1, lists.size());
            JList<?> list = lists.get(0);
            assertFalse(list.isOpaque());
            JScrollPane scroll = (JScrollPane) SwingUtilities.getAncestorOfClass(JScrollPane.class, list);
            assertNotNull(scroll);
            assertFalse(scroll.isOpaque());
            assertFalse(scroll.getViewport().isOpaque());
            @SuppressWarnings("unchecked")
            JList<MathLibrary.Material> materials = (JList<MathLibrary.Material>) list;
            Component row = materials.getCellRenderer().getListCellRendererComponent(
                    materials, library.items().get(0), 0, false, false);
            assertFalse(((JComponent)row).isOpaque());
            Component selectedRow = materials.getCellRenderer().getListCellRendererComponent(
                    materials, library.items().get(0), 0, true, false);
            assertFalse(((JComponent)selectedRow).isOpaque());
        } finally {panel.close();}
    }
    private static void findLists(Container root, List<JList<?>> found) {
        for (Component c : root.getComponents()) {
            if (c instanceof JList<?> list) found.add(list);
            if (c instanceof Container child) findLists(child, found);
        }
    }
}
