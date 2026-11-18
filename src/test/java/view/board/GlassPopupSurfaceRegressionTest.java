package view.board;

import org.junit.jupiter.api.Test;
import javax.swing.plaf.basic.BasicPopupMenuUI;
import static org.junit.jupiter.api.Assertions.*;

/** Menus must not paint the native opaque rectangular popup behind rounded glass corners. */
class GlassPopupSurfaceRegressionTest {
    @Test void saveMenuUsesTransparentGlassPopupSurface() {
        GlassSaveMenu menu = new GlassSaveMenu(
                new SaveMenuState("canvas", SaveMenuState.Phase.MODIFIED), () -> {}, () -> {});
        assertFalse(menu.isOpaque());
        assertEquals(0, menu.getBackground().getAlpha());
        assertTrue(menu.isLightWeightPopupEnabled());
        assertInstanceOf(BasicPopupMenuUI.class, menu.getUI());
        assertFalse(((javax.swing.JPanel) menu.getComponent(0)).isOpaque());
    }
}
