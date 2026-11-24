package view.settings;

import java.awt.Window;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.util.prefs.Preferences;
import javax.swing.JComponent;
import java.awt.Component;
import java.awt.Container;

public final class ThemeManager {

    private static boolean darkMode;

    private ThemeManager() {
        // Utility class: no instances needed.
    }

    public static boolean isDarkMode() {
        return darkMode;
    }

    public static void setDarkMode(boolean enabled) {
        MetalLookAndFeel.setCurrentTheme(
                enabled ? new DarkTheme() : new DefaultMetalTheme());

        try {
            UIManager.setLookAndFeel(new MetalLookAndFeel());
        } catch (UnsupportedLookAndFeelException exception) {
            throw new IllegalStateException(
                    "Unable to change the theme.", exception);
        }

        darkMode = enabled;

        // Semantic colors: brighter on dark backgrounds.
        UIManager.put("App.error",
                new ColorUIResource(enabled ? 0xFF9B9B : 0xB3261E));

        UIManager.put("App.success",
                new ColorUIResource(enabled ? 0x8CE0AC : 0x176B37));

        for (Window window : Window.getWindows()) {
            SwingUtilities.updateComponentTreeUI(window);

            // Restore semantic colors after Swing finishes refreshing.
            refreshMessageColors(window);

            window.repaint();
        }

        try {
            Preferences.userNodeForPackage(ThemeManager.class)
                    .putBoolean("darkMode", enabled);
        } catch (SecurityException ignored) {
            // The theme still works for this session.
        }
    }

    private static final class DarkTheme extends DefaultMetalTheme {

        private ColorUIResource color(int rgb) {
            return new ColorUIResource(rgb);
        }

        @Override
        public String getName() {
            return "Pet App Dark";
        }

        @Override
        protected ColorUIResource getPrimary1() {
            return color(0x6D8EBB);
        }

        @Override
        protected ColorUIResource getPrimary2() {
            return color(0x43638D);
        }

        @Override
        protected ColorUIResource getPrimary3() {
            return color(0x344C6B);
        }

        @Override
        protected ColorUIResource getSecondary1() {
            return color(0x7E8797);
        }

        @Override
        protected ColorUIResource getSecondary2() {
            return color(0x525C6B);
        }

        @Override
        protected ColorUIResource getSecondary3() {
            return color(0x292F3A);
        }

        @Override
        public ColorUIResource getControlTextColor() {
            return color(0xF1F3F7);
        }

        @Override
        public ColorUIResource getSystemTextColor() {
            return getControlTextColor();
        }

        @Override
        public ColorUIResource getUserTextColor() {
            return getControlTextColor();
        }

        @Override
        public ColorUIResource getMenuForeground() {
            return getControlTextColor();
        }

        @Override
        public ColorUIResource getWindowBackground() {
            return color(0x1E232C);
        }

        @Override
        public ColorUIResource getHighlightedTextColor() {
            return color(0xFFFFFF);
        }

        @Override
        public ColorUIResource getInactiveControlTextColor() {
            return color(0xA5ADBB);
        }

        @Override
        public ColorUIResource getInactiveSystemTextColor() {
            return getInactiveControlTextColor();
        }

        @Override
        public ColorUIResource getMenuDisabledForeground() {
            return getInactiveControlTextColor();
        }

        @Override
        public ColorUIResource getFocusColor() {
            return color(0xA8CAFF);
        }
    }
    public static void initialize() {
        boolean savedDarkMode = false;

        try {
            savedDarkMode = Preferences.userNodeForPackage(ThemeManager.class)
                    .getBoolean("darkMode", false);
        } catch (SecurityException ignored) {
            // Use light mode if preferences cannot be accessed.
        }

        setDarkMode(savedDarkMode);
    }

    public static void setMessageColor(
            JComponent component, String colorKey) {

        component.putClientProperty("messageColorKey", colorKey);
        component.setForeground(UIManager.getColor(colorKey));
    }

    // In case the message stay red/green when user switch mode
    private static void refreshMessageColors(Component component) {
        if (component instanceof JComponent) {
            JComponent swingComponent = (JComponent) component;

            Object colorKey =
                    swingComponent.getClientProperty("messageColorKey");

            if (colorKey instanceof String) {
                swingComponent.setForeground(
                        UIManager.getColor((String) colorKey));
            }
        }

        if (component instanceof Container) {
            Container container = (Container) component;

            for (Component child : container.getComponents()) {
                refreshMessageColors(child);
            }
        }
    }
}