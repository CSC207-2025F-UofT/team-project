package app;

import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        AppBuilder appBuilder = new AppBuilder();
        JFrame application = appBuilder
                .addLoginView()
                .addSignupView()
                .addLoggedInView()
                .addLogoutUseCase()
                .addSpendingReportView()
                .addSpendingReportUseCase()
                .addCategorizerView()
                .addCategorizerUseCase()
                .addSignupUseCase()
                .addUploadStatementView()
                .addLoginUseCase()
                .addChangePasswordUseCase()
                .addSpendingLimitsView()
                .addSpendingLimitsUseCase()
                .addUploadStatementUseCase()
                .addFilterSearchView()
                .addFilterSearchUseCase()
                .build();

        application.pack();
        application.setLocationRelativeTo(null);
        application.setVisible(true);
    }
}
