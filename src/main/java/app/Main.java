package app;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        AppBuilder appBuilder = new AppBuilder();
        JFrame application = appBuilder
                .addLoginView()
                .addSignUpView()
                .addWatchlistView()
                .addFavoritesView()
                .addRateAndCommentView()
                .addRandCView()
                .addLoginUseCase()
                .addSignupUseCase()
                .addCommentUseCase()
                .build();

        application.pack();
        application.setLocationRelativeTo(null);
        application.setVisible(true);
    }
}
