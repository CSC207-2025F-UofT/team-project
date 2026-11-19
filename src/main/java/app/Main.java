package app;

import data_access.FileUserDataAccessObject;
import data_access.UserDataAccessObject;
import entity.UserFactory;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Entry point. Swap storage by changing the one line below
 * (e.g. new InMemoryUserDataAccessObject() for a throwaway session).
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UserDataAccessObject userDataAccess = new FileUserDataAccessObject("users.json", new UserFactory());

            JFrame application = new AppBuilder(userDataAccess)
                    .addLoginView()
                    .addSignupView()
                    .addLoggedInView()
                    .addSignupUseCase()
                    .addLoginUseCase()
                    .addLogoutUseCase()
                    .build();

            application.pack();
            application.setLocationRelativeTo(null);
            application.setVisible(true);
        });
    }
}
