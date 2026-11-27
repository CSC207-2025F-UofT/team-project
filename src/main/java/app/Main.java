package app;

import javax.swing.JFrame;
import java.awt.*;

/**
 * Starts Sweater Weather: builds every screen with AppBuilder and opens the window.
 */

public class Main {
    public static void main(String[] args) {
        AppBuilder appBuilder = new AppBuilder();
        JFrame application = appBuilder
                .addAddClothingView()
                .addGenerateOutfitView()
                .addSaveOutfitView()
                .addSetStyleView()
                .addViewClosetView()
                .build();
        application.pack();
        application.setMinimumSize(new Dimension(760, 800));
        application.setLocationRelativeTo(null);
        application.setVisible(true);

    }
}
