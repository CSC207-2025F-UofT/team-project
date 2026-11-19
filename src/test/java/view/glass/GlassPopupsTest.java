package view.glass;

import org.junit.jupiter.api.Test;

import javax.swing.JPopupMenu;
import javax.swing.PopupFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Glass menus must bypass the macOS factory that paints a square grey window behind them. */
class GlassPopupsTest {
    @Test
    void markingAMenuInstallsTheGlassFactoryOnce() {
        JPopupMenu menu = new JPopupMenu();
        GlassPopups.useTransparentPopup(menu);
        PopupFactory installed = PopupFactory.getSharedInstance();
        GlassPopups.useTransparentPopup(new JPopupMenu());

        assertTrue(menu.isLightWeightPopupEnabled());
        assertEquals("view.glass.GlassPopups$Factory", installed.getClass().getName());
        assertEquals(installed, PopupFactory.getSharedInstance(), "factory must not wrap itself again");
    }
}
