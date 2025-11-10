import auth.data.*;
import auth.interface_adapters.controllers.*;
import auth.use_case.login.*;
import auth.use_case.signup.*;
import ui.LoginView;
import ui.SignUpView;

import javax.sql.DataSource;
import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // set up user database
            DataSource dataSource = DataSourceFactory.sqlite("app.db");
            SchemaInitializer.ensureSchema(dataSource);
            JdbcUserRepository repo = new JdbcUserRepository(dataSource);

            // Create interactors
            SignUpInteractor signUpInteractor = new SignUpInteractor(repo);
            LoginInteractor loginInteractor = new LoginInteractor(repo);

            // Controllers
            SignUpController signUpController = new SignUpController(signUpInteractor);
            LoginController loginController = new LoginController(loginInteractor);

            // Screen switching
            final JFrame[] currentFrame = new JFrame[1];

            Runnable showLogin = () -> {
                currentFrame[0] = new LoginView(loginController, () -> {
                    currentFrame[0].dispose();
                    showSignUp.run();
                });
                currentFrame[0].setVisible(true);
            };

            Runnable showSignUp = () -> {
                currentFrame[0] = new SignUpView(signUpController, () -> {
                    currentFrame[0].dispose();
                    showLogin.run();
                });
                currentFrame[0].setVisible(true);
            };

            // Start on Login screen
            showLogin.run();
        });
    }
}
