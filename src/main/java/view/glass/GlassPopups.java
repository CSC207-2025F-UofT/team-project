package view.glass;

import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JRootPane;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.Component;
import java.awt.Window;

/**
 * Keeps rounded glass menus free of a square backing on macOS.
 *
 * <p>The Aqua look and feel replaces Swing's popup factory with one that ignores
 * {@link JPopupMenu#setLightWeightPopupEnabled(boolean)} and always opens a native window with a
 * translucent grey background and a rectangular shadow. That grey square shows behind every
 * rounded menu. Menus marked here get Swing's normal lightweight popup instead (drawn inside the
 * app's own window, so their transparent corners stay transparent); all other popups such as
 * combo boxes and tooltips keep the platform behaviour.
 */
public final class GlassPopups {
    private static final String KEY = "glass.lightweightPopup";

    private GlassPopups() { }

    /** Call from a glass menu's constructor; safe to call repeatedly and in headless tests. */
    public static void useTransparentPopup(JPopupMenu menu) {
        menu.putClientProperty(KEY, Boolean.TRUE);
        menu.setLightWeightPopupEnabled(true);
        install();
    }

    static synchronized void install() {
        PopupFactory current = PopupFactory.getSharedInstance();
        if (!(current instanceof Factory)) PopupFactory.setSharedInstance(new Factory(current));
    }

    private static boolean marked(Component contents) {
        return contents instanceof JComponent c && Boolean.TRUE.equals(c.getClientProperty(KEY));
    }

    private static final class Factory extends PopupFactory {
        private final PopupFactory platform;

        Factory(PopupFactory platform) { this.platform = platform; }

        @Override public Popup getPopup(Component owner, Component contents, int x, int y) {
            if (!marked(contents)) return platform.getPopup(owner, contents, x, y);
            // Swing's own factory: lightweight when the menu fits inside the window.
            Popup popup = super.getPopup(owner, contents, x, y);
            return new Popup() {
                @Override public void show() {
                    popup.show();
                    clearWindowBacking(owner, contents);
                }
                @Override public void hide() { popup.hide(); }
            };
        }
    }

    /** A menu that did not fit inside the window still needs a separate window: make it see-through. */
    private static void clearWindowBacking(Component owner, Component contents) {
        Window popupWindow = SwingUtilities.getWindowAncestor(contents);
        if (popupWindow == null || popupWindow == SwingUtilities.getWindowAncestor(owner)) return;
        try {
            popupWindow.setBackground(new Color(0, 0, 0, 0));
        } catch (UnsupportedOperationException | IllegalArgumentException unsupported) {
            return;  // no per-pixel translucency: the plain window is the best available
        }
        if (popupWindow instanceof RootPaneContainer container) {
            JRootPane root = container.getRootPane();
            root.setOpaque(false);
            if (container.getContentPane() instanceof JComponent content) content.setOpaque(false);
        }
    }
}
