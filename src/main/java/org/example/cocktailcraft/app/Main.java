package org.example.cocktailcraft.app;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.example.cocktailcraft.view.language.Language;
import org.example.cocktailcraft.view.language.LanguageSelection;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::start);
    }

    private static void start() {
        Language language = LanguageSelection.choose(null);
        if (language == null) {
            return;
        }

        JFrame application = new AppBuilder(language)
                .addSearchView()
                .addSearchUseCase()
                .addClickUseCase()
                .addFavouriteUseCase()
                .addCustomizeUseCase(() -> null)
                .addMainPageView()
                .build();

        application.pack();
        application.setLocationRelativeTo(null);
        application.setVisible(true);
    }
}