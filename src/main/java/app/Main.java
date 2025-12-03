package app;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        AppBuilder appBuilder = new AppBuilder();
        JFrame application = appBuilder
                .addLoginView()
                .addSignupView()
                .addLoggedInView()
                .addUploadStatementView()
                .addTransactionCategorizerView()    // Must be added before accessing
                .addSignupUseCase()
                .addLoginUseCase()
                .addChangePasswordUseCase()
                .addUploadStatementUseCase()
                .addTransactionCategorizerUseCase()
                .build();

        application.pack();
        application.setLocationRelativeTo(null);
        application.setVisible(true);

        // Show Transaction Categorizer immediately for testing
        appBuilder.viewManagerModel.setState(appBuilder.getTransactionCategorizerView().getViewName());
        appBuilder.viewManagerModel.firePropertyChange();
    }
}
