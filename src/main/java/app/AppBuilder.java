package app;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import ViewManager.ViewManager;
import ViewManager.ViewManagerModel;
import data_access.SupabaseCognitiveDataAccessObject;
import data_access.SupabaseUserDataAccessObject;
import interface_adapter.Loggedin.LoggedinViewModel;
import interface_adapter.Loggedin.get_dataController;
import interface_adapter.Loggedin.get_dataPresenter;
import interface_adapter.Login.LoginController;
import interface_adapter.Login.LoginPresenter;
import interface_adapter.Login.LoginViewModel;
import interface_adapter.Signup.SignupController;
import interface_adapter.Signup.SignupPresenter;
import interface_adapter.Signup.SignupViewModel;
import use_case.get_data.get_dataInputBoundary;
import use_case.get_data.get_dataInteractor;
import use_case.get_data.get_dataOutputBoundary;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInteractor;
import use_case.login.LoginOutputBoundary;
import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInteractor;
import use_case.signup.SignupOutputBoundary;
import view.LoggedinView;
import view.LoginView;
import view.SignupView;

/**
 * Builds the application: views, view models, use cases and data access.
 */
public class AppBuilder {

    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();

    private final ViewManagerModel viewManagerModel = new ViewManagerModel();
    private final ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

    // To run without the network, swap this for InMemoryUserDataAccessObject
    // (it implements both the signup and login data-access interfaces).
    private final SupabaseUserDataAccessObject userDataAccessObject = new SupabaseUserDataAccessObject();
    private final SupabaseCognitiveDataAccessObject cognitiveDataAccessObject =
            new SupabaseCognitiveDataAccessObject();

    private SignupView signupView;
    private SignupViewModel signupViewModel;
    private LoginView loginView;
    private LoginViewModel loginViewModel;
    private LoggedinView loggedinView;
    private LoggedinViewModel loggedinViewModel;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    public AppBuilder addSignupView() {
        signupViewModel = new SignupViewModel();
        signupView = new SignupView(signupViewModel);
        cardPanel.add(signupView, SignupViewModel.VIEW_NAME);
        return this;
    }

    public AppBuilder addLoginView() {
        loginViewModel = new LoginViewModel();
        loginView = new LoginView(loginViewModel);
        cardPanel.add(loginView, LoginViewModel.VIEW_NAME);
        return this;
    }

    public AppBuilder addLoggedinView() {
        loggedinViewModel = new LoggedinViewModel();
        loggedinView = new LoggedinView(loggedinViewModel);
        cardPanel.add(loggedinView, LoggedinViewModel.VIEW_NAME);
        return this;
    }

    public AppBuilder addSignupUseCase() {
        final SignupOutputBoundary presenter =
                new SignupPresenter(viewManagerModel, signupViewModel, loginViewModel);
        final SignupInputBoundary interactor = new SignupInteractor(userDataAccessObject, presenter);
        signupView.setSignupController(new SignupController(interactor));
        return this;
    }

    public AppBuilder addLoginUseCase() {
        final LoginOutputBoundary presenter =
                new LoginPresenter(viewManagerModel, loginViewModel, loggedinViewModel);
        final LoginInputBoundary interactor = new LoginInteractor(userDataAccessObject, presenter);
        loginView.setLoginController(new LoginController(interactor));
        return this;
    }

    public AppBuilder addGetDataUseCase() {
        final get_dataOutputBoundary presenter = new get_dataPresenter(loggedinViewModel);
        final get_dataInputBoundary interactor = new get_dataInteractor(cognitiveDataAccessObject, presenter);
        loggedinView.setGetDataController(new get_dataController(interactor));
        return this;
    }

    /**
     * Builds the application window, starting on the signup view.
     * @return the application window
     */
    public JFrame build() {
        final JFrame application = new JFrame("Cognitive Tracker");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        application.add(cardPanel);

        viewManagerModel.setState(SignupViewModel.VIEW_NAME);
        viewManagerModel.notifyViewChanged();

        return application;
    }
}
