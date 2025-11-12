import auth.data.*;
import auth.interface_adapters.controllers.*;
import auth.use_case.login.*;
import auth.use_case.signup.*;
import ui.LoginView;
import ui.SignUpView;
import ui.DashboardFrame;

import javax.sql.DataSource;
import javax.swing.*;


public class Main {

    private static DataSource dataSource;
    private static JdbcUserRepository userRepository;

    private static SignUpController signUpController;
    private static LoginController loginController;

    private static JFrame currentFrame;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Setup database
            dataSource = DataSourceFactory.sqlite("app.db");
            SchemaInitializer.ensureSchema(dataSource);
            userRepository = new JdbcUserRepository(dataSource);

            // Create interactors
            SignUpInteractor signUpInteractor = new SignUpInteractor(userRepository);
            LoginInteractor loginInteractor = new LoginInteractor(userRepository);

            // Create controllers
            signUpController = new SignUpController(signUpInteractor);
            loginController = new LoginController(loginInteractor);

            // Start application on the login screen
            showLoginView();
        });
    }

    /** Displays the login window */
    private static void showLoginView() {
        // Close any open frame first
        if (currentFrame != null) currentFrame.dispose();

        LoginView loginView = new LoginView(
                loginController,
                Main::showSignUpView, // callback to switch to sign up
                Main::showDashboardView   // callback to go to dashboard after login
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

    /** Displays the dashboard (JavaFX embedded in Swing) */
    private static void showDashboardView() {
        if (currentFrame != null) currentFrame.dispose();

        currentFrame = new DashboardFrame();  // JavaFX Stocks UI inside Swing frame
        currentFrame.setVisible(true);
    }

}
