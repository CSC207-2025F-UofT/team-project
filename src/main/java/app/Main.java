package app;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import data_access.FileAdminDataAccessObject;
import data_access.FileAdopterDataAccessObject;
import data_access.Sha256PasswordHasher;
import interface_adapter.ViewManagerModel;
import interface_adapter.adopter_login.AdopterLoginController;
import interface_adapter.adopter_login.AdopterLoginPresenter;
import interface_adapter.adopter_login.AdopterLoginViewModel;
import interface_adapter.adopter_signup.AdopterSignupController;
import interface_adapter.adopter_signup.AdopterSignupPresenter;
import interface_adapter.adopter_signup.AdopterSignupViewModel;
import interface_adapter.admin_login.AdminLoginController;
import interface_adapter.admin_login.AdminLoginPresenter;
import interface_adapter.admin_login.AdminLoginViewModel;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.navigation.NavigationController;
import interface_adapter.role_select.RoleSelectViewModel;
import use_case.PasswordHasher;
import use_case.adopter_login.AdopterLoginInteractor;
import use_case.adopter_signup.AdopterSignupInteractor;
import use_case.admin_login.AdminLoginInteractor;
import view.AdminLoginView;
import view.AdopterLoginView;
import view.AdopterSignupView;
import view.LoggedInView;
import view.RoleSelectView;
import view.ViewManager;

/**
 * Builds all the objects and connects them. This is the only class that
 * knows about every layer.
 */
public final class Main {
    private static final String ADOPTERS_FILE = "adopters.csv";
    private static final String ADMIN_FILE = "admin.txt";
    /** Used only the first time, to create admin.txt. Change it before the demo. */
    private static final String DEFAULT_ADMIN_PASSWORD = "admin123";

    private Main() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::buildAndShow);
    }

    private static void buildAndShow() {
        // Outer layer: storage + hashing
        final PasswordHasher hasher = new Sha256PasswordHasher();
        final FileAdopterDataAccessObject adopterDao = new FileAdopterDataAccessObject(ADOPTERS_FILE);
        final FileAdminDataAccessObject adminDao =
                new FileAdminDataAccessObject(ADMIN_FILE, DEFAULT_ADMIN_PASSWORD, hasher);

        // View models
        final ViewManagerModel viewManagerModel = new ViewManagerModel();
        final AdopterLoginViewModel loginViewModel = new AdopterLoginViewModel();
        final AdopterSignupViewModel signupViewModel = new AdopterSignupViewModel();
        final AdminLoginViewModel adminLoginViewModel = new AdminLoginViewModel();
        final LoggedInViewModel loggedInViewModel = new LoggedInViewModel();
        final NavigationController navigationController = new NavigationController(viewManagerModel);

        // Use cases: presenter -> interactor -> controller
        final AdopterSignupController signupController = new AdopterSignupController(
                new AdopterSignupInteractor(adopterDao,
                        new AdopterSignupPresenter(viewManagerModel, signupViewModel, loginViewModel),
                        hasher));
        final AdopterLoginController loginController = new AdopterLoginController(
                new AdopterLoginInteractor(adopterDao,
                        new AdopterLoginPresenter(viewManagerModel, loginViewModel, loggedInViewModel),
                        hasher));
        final AdminLoginController adminLoginController = new AdminLoginController(
                new AdminLoginInteractor(adminDao,
                        new AdminLoginPresenter(viewManagerModel, adminLoginViewModel, loggedInViewModel),
                        hasher));

        // Views
        final CardLayout cardLayout = new CardLayout();
        final JPanel cards = new JPanel(cardLayout);
        cards.add(new RoleSelectView(navigationController), RoleSelectViewModel.VIEW_NAME);
        cards.add(new AdopterLoginView(loginViewModel, loginController, navigationController),
                AdopterLoginViewModel.VIEW_NAME);
        cards.add(new AdopterSignupView(signupViewModel, signupController, navigationController),
                AdopterSignupViewModel.VIEW_NAME);
        cards.add(new AdminLoginView(adminLoginViewModel, adminLoginController, navigationController),
                AdminLoginViewModel.VIEW_NAME);
        cards.add(new LoggedInView(loggedInViewModel), LoggedInViewModel.VIEW_NAME);
        new ViewManager(cards, cardLayout, viewManagerModel);

        final JFrame frame = new JFrame("Pet Adoption");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(cards);
        frame.setSize(420, 360);
        frame.setLocationRelativeTo(null);
        viewManagerModel.switchTo(RoleSelectViewModel.VIEW_NAME);
        frame.setVisible(true);
    }
}
