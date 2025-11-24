package app;

import data_access.FileUserDataAccessObject;
import entity.UserFactory;
import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.ChangePasswordController;
import interface_adapter.logged_in.ChangePasswordPresenter;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.login.LoginViewModel;
import interface_adapter.logout.LogoutController;
import interface_adapter.logout.LogoutPresenter;
import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupPresenter;
import interface_adapter.signup.SignupViewModel;
import use_case.change_password.ChangePasswordInputBoundary;
import use_case.change_password.ChangePasswordInteractor;
import use_case.change_password.ChangePasswordOutputBoundary;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInteractor;
import use_case.login.LoginOutputBoundary;
import use_case.logout.LogoutInputBoundary;
import use_case.logout.LogoutInteractor;
import use_case.logout.LogoutOutputBoundary;
import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInteractor;
import use_case.signup.SignupOutputBoundary;
import view.LoggedInView;
import view.LoginView;
import view.SignupView;
import view.ViewManager;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();

    // Data Access Objects and Factories
    private FileUserDataAccessObject userDataAccessObject;
    private UserFactory userFactory;

    // View Models
    private ViewManagerModel viewManagerModel;
    private LoginViewModel loginViewModel;
    private SignupViewModel signupViewModel;
    private LoggedInViewModel loggedInViewModel;

    // Views
    private LoginView loginView;
    private SignupView signupView;
    private LoggedInView loggedInView; // <-- LoggedInView must be an accessible field

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);

        // Initialize Models
        viewManagerModel = new ViewManagerModel();
        loginViewModel = new LoginViewModel();
        signupViewModel = new SignupViewModel();
        loggedInViewModel = new LoggedInViewModel();

        // Initialize DAO and Factory
        userDataAccessObject = new FileUserDataAccessObject("./users.csv", new UserFactory());
        userFactory = new UserFactory();

        // Initialize ViewManager
        new ViewManager(cardPanel, cardLayout, viewManagerModel);
    }

    // --- VIEW BUILDERS ---

    public AppBuilder addLoginView() {
        // Dependencies for Login
        final LoginOutputBoundary loginOutputBoundary = new LoginPresenter(viewManagerModel,
                loggedInViewModel, loginViewModel);
        final LoginInputBoundary loginInteractor =
                new LoginInteractor(userDataAccessObject, loginOutputBoundary);

        final LoginController loginController =
                new LoginController(loginInteractor, viewManagerModel, signupViewModel);


        // View Creation
        this.loginView = new LoginView(loginViewModel);
        this.loginView.setLoginController(loginController);
        cardPanel.add(loginView, loginView.getViewName());
        return this;
    }

    public AppBuilder addSignupView() {
        // Dependencies for Signup
        final SignupOutputBoundary signupOutputBoundary = new SignupPresenter(viewManagerModel,
                signupViewModel, loginViewModel);
        final SignupInputBoundary signupInteractor = new SignupInteractor(userDataAccessObject, signupOutputBoundary, userFactory);
        final SignupController signupController = new SignupController(signupInteractor);

        // View Creation
        this.signupView = new SignupView(signupViewModel);
        this.signupView.setSignupController(signupController);
        cardPanel.add(signupView, signupView.getViewName());
        return this;
    }

    // --- NEW METHOD FOR LOGGEDIN VIEW ---
    public AppBuilder addLoggedInView() {
        // LoggedInView is instantiated without the JFrame here.
        this.loggedInView = new LoggedInView(loggedInViewModel);

        // Add the view to the CardPanel
        cardPanel.add(loggedInView, loggedInView.getViewName());

        return this;
    }

    // --- USE CASE BUILDERS ---

    public AppBuilder addSignupUseCase() {
        return this;
    }

    public AppBuilder addLoginUseCase() {
        return this;
    }

    public AppBuilder addChangePasswordUseCase() {
        final ChangePasswordOutputBoundary changePasswordOutputBoundary = new ChangePasswordPresenter(viewManagerModel,
                loggedInViewModel);

        final ChangePasswordInputBoundary changePasswordInteractor =
                new ChangePasswordInteractor(userDataAccessObject, changePasswordOutputBoundary, userFactory);

        ChangePasswordController changePasswordController = new ChangePasswordController(changePasswordInteractor);

        // Pass controller to the LoggedInView field
        loggedInView.setChangePasswordController(changePasswordController);
        return this;
    }

    public AppBuilder addLogoutUseCase() {
        final LogoutOutputBoundary logoutOutputBoundary = new LogoutPresenter(viewManagerModel,
                loggedInViewModel, loginViewModel);

        final LogoutInputBoundary logoutInteractor =
                new LogoutInteractor(userDataAccessObject, logoutOutputBoundary);

        final LogoutController logoutController = new LogoutController(logoutInteractor);

        // Pass controller to the LoggedInView field
        loggedInView.setLogoutController(logoutController);
        return this;
    }

    // --- MODIFIED BUILD METHOD ---
    public JFrame build() {
        final JFrame application = new JFrame("User Login Example");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // CRITICAL STEP: Inject the new JFrame into LoggedInView now that it exists.
        if (loggedInView != null) {
            loggedInView.setApplicationFrame(application);
        }

        application.add(cardPanel);

        // Set the initial view to Signup
        viewManagerModel.setState(signupView.getViewName());
        viewManagerModel.firePropertyChange();

        return application;
    }


}