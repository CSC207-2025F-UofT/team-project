package app;

public class Main {
    public static void main(String[] args) {

        AppBuilder appBuilder = new AppBuilder();
        JFrame application = appBuilder
                .addLoginView()
                .addSignupView()
                .addLoggedInView()
                .addUploadStatementView()
                .addSpendingReportView()
                .addSignupUseCase()
                .addLoginUseCase()
                .addChangePasswordUseCase()
                .addUploadStatementUseCase()
                .addSpendingReportUseCase()
                .build();
    }
}