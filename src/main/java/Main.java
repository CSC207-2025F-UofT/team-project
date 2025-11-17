import controllers.DashboardController;
import controllers.LoginController;
import controllers.SignUpController;
import data.DataSourceFactory;
import data.RegisteredExpenseRepository;
import data.RegisteredUserRepository;
import data.TableInitializer;
import ui.LoginView;
import ui.SignUpView;
import ui.DashboardView;
import use_case.login.LoginInteractor;
import use_case.signup.SignUpInteractor;

import javax.sql.DataSource;
import javax.swing.*;

public class Main {

    private static DataSource dataSource;
    private static RegisteredUserRepository userRepository;
    private static RegisteredExpenseRepository expenseRepository;

    private static SignUpController signUpController;
    private static LoginController loginController;
    private static DashboardController dashboardController;

    private static JFrame currentFrame;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Setup database
            dataSource = DataSourceFactory.sqlite("sqllite.db");
            TableInitializer.ensureSchema(dataSource);
            userRepository = new RegisteredUserRepository(dataSource);
            expenseRepository = new RegisteredExpenseRepository(dataSource);

            // Create interactors
            SignUpInteractor signUpInteractor = new SignUpInteractor(userRepository);
            LoginInteractor loginInteractor = new LoginInteractor(userRepository);

            // Create controllers
            signUpController = new SignUpController(signUpInteractor);
            loginController = new LoginController(loginInteractor);
            dashboardController = new DashboardController();

            // Start application on the login screen
            showLoginView();
        });
    }

    /** Displays the login window */
    private static void showLoginView() {
        if (currentFrame != null) currentFrame.dispose();

        LoginView loginView = new LoginView(
                loginController,
                Main::showSignUpView,         // Runnable
                Main::showDashboardView       // Consumer<String>
        );

        currentFrame = loginView;
        loginView.setVisible(true);
    }

    /** Displays the sign-up window */
    private static void showSignUpView() {
        if (currentFrame != null) currentFrame.dispose();

        SignUpView signUpView = new SignUpView(
                signUpController,
                Main::showLoginView // callback to switch back
        );

        currentFrame = signUpView;
        signUpView.setVisible(true);
    }

    private static void showDashboardView(String username) {
        if (currentFrame != null) currentFrame.dispose();

        DashboardView dashboardView = new DashboardView(
                dashboardController,
                Main::showLoginView,   // callback to login screen
                username,              // show welcome message
                expenseRepository
        );

        currentFrame = dashboardView;
        dashboardView.setVisible(true);
    }
}
