
package app;

import javax.swing.*;

public class AppPreview {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AppBuilder builder = new AppBuilder();

            builder.addSignUpCase();
            builder.addLoginView();
            builder.addLoginUseCase();
            builder.addSearchUseCase();
            builder.addMovieDetailUseCase();
            builder.addWatchlistUseCases();

            JFrame application = builder.build();
            application.setVisible(true);
        });
    }
}
