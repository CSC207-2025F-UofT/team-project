package main.view;

import javax.swing.*;
import java.awt.*;

public final class UserSystemSettings {

    private static int screenWidth;
    private static int screenHeight;

    // runs when class is first loaded
    static {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        screenWidth = screenSize.width;
        screenHeight = screenSize.height;
    }

    private UserSystemSettings() {}

    public static int getScreenWidth() {
        return screenWidth;
    }

    public static int getScreenHeight() {
        return screenHeight;
    }

    public static void updateWindowSize() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        screenWidth = screenSize.width;
        screenHeight = screenSize.height;
    }
}
