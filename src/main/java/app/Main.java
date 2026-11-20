package app;

import view.FontManager;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        FontManager.loadFonts();

        AppBuilder appBuilder = new AppBuilder();
        JFrame application = appBuilder
                .addTimerSettingsView()
                .addHomeView()
                .addFSView()
                .addUpdateTimerSettingsUseCase()
                .addOpenSettingsUseCase()
                .addStartTimerUseCase()
                .addCancelTimerUseCase()
                .addSwitchViewUseCase()
                .build();

        application.setLocationRelativeTo(null);
        application.setVisible(true);
    }
}
