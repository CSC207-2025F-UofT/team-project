package pdf_editor.view;

import org.junit.jupiter.api.Test;
import javax.swing.JPopupMenu;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicPopupMenuUI;
import java.lang.reflect.Constructor;
import static org.junit.jupiter.api.Assertions.*;

class CanvasSelectionPopupSurfaceTest {
    @Test void selectionMenuHasNoOpaqueNativeRectangle() throws Exception {
        Class<?> type = Class.forName("pdf_editor.view.CanvasLibrarySelectionMenu$GlassSelectionMenu");
        Constructor<?> constructor = type.getDeclaredConstructor();
        constructor.setAccessible(true);
        JPopupMenu menu = (JPopupMenu) constructor.newInstance();
        assertFalse(menu.isOpaque());
        assertEquals(0, menu.getBackground().getAlpha());
        assertInstanceOf(BasicPopupMenuUI.class, menu.getUI());
        assertFalse(((JPanel) menu.getComponent(0)).isOpaque());
    }
}
