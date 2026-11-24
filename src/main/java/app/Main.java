package app;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::showApplication);
    }

    private static void showApplication() {
        AppBuilder appBuilder = new AppBuilder();
        JFrame application = appBuilder
                .addLoginView()
                .addSignupView()
                .addLoggedInView()
                .addRecordTransactionView()
                .addViewBudgetProgressView()
                .addSetBudgetView()
                .addSignupUseCase()
                .addLoginUseCase()
                .addChangePasswordUseCase()
                .addLogoutUseCase()
                .addGoogleLoginUseCase()
                .addRecordTransactionUseCase()
                .addViewBudgetProgressUseCase()
                .addSetBudgetUseCase()
                .addAdoptPetView()
                .addInteractWithPetView()
                .addInteractWithPetUseCase()
                .addGetPetPictureUseCase()
                .addAdoptPetUseCase()
                .build();

        application.pack();
        application.setLocationRelativeTo(null);
        application.setVisible(true);
    }
}
