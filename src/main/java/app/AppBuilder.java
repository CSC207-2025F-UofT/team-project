package app;

import data_access.UserDataAccessObject;
import entity.UserFactory;
import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.login.LoginViewModel;
import interface_adapter.logout.LogoutController;
import interface_adapter.logout.LogoutPresenter;
import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupPresenter;
import interface_adapter.signup.SignupViewModel;
import use_case.login.LoginInteractor;
import use_case.logout.LogoutInteractor;
import use_case.signup.SignupInteractor;
import view.LoggedInView;
import view.LoginView;
import view.SignupView;
import view.ViewManager;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import java.awt.CardLayout;

/**
 * Wires the app together (Builder pattern).
 * To add your feature: write addXView() and addXUseCase() methods, then call them in Main.
 * Rule: add views before the use cases that use them.
 */
public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    private final ViewManagerModel viewManagerModel = new ViewManagerModel();
    private final UserDataAccessObject userDataAccessObject;
    private final UserFactory userFactory = new UserFactory();

    private SignupViewModel signupViewModel;
    private LoginViewModel loginViewModel;
    private LoggedInViewModel loggedInViewModel;
    private SignupView signupView;
    private LoginView loginView;
    private LoggedInView loggedInView;

    public AppBuilder(UserDataAccessObject userDataAccessObject) {
        this.userDataAccessObject = userDataAccessObject;
        cardPanel.setLayout(cardLayout);
        new ViewManager(cardPanel, cardLayout, viewManagerModel);
    }

    public AppBuilder addLoginView() {
        loginViewModel = new LoginViewModel();
        loginView = new LoginView(loginViewModel);
        cardPanel.add(loginView, loginView.getViewName());
        return this;
    }

    public AppBuilder addSignupView() {
        signupViewModel = new SignupViewModel();
        signupView = new SignupView(signupViewModel);
        cardPanel.add(signupView, signupView.getViewName());
        return this;
    }

    public AppBuilder addLoggedInView() {
        loggedInViewModel = new LoggedInViewModel();
        loggedInView = new LoggedInView(loggedInViewModel);
        cardPanel.add(loggedInView, loggedInView.getViewName());
        return this;
    }

    public AppBuilder addSignupUseCase() {
        SignupPresenter presenter = new SignupPresenter(viewManagerModel, signupViewModel, loginViewModel);
        SignupInteractor interactor = new SignupInteractor(userDataAccessObject, presenter, userFactory);
        signupView.setSignupController(new SignupController(interactor));
        return this;
    }

    public AppBuilder addLoginUseCase() {
        LoginPresenter presenter = new LoginPresenter(viewManagerModel, loginViewModel,
                loggedInViewModel, signupViewModel);
        LoginInteractor interactor = new LoginInteractor(userDataAccessObject, presenter);
        loginView.setLoginController(new LoginController(interactor));
        return this;
    }

    public AppBuilder addLogoutUseCase() {
        LogoutPresenter presenter = new LogoutPresenter(viewManagerModel, loggedInViewModel, loginViewModel);
        LogoutInteractor interactor = new LogoutInteractor(userDataAccessObject, presenter);
        loggedInView.setLogoutController(new LogoutController(interactor));
        return this;
    }

    public JFrame build() {
        JFrame application = new JFrame("Time Tracker");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        application.add(cardPanel);

        viewManagerModel.setState(loginView.getViewName());
        viewManagerModel.firePropertyChanged();
        return application;
    }
}
