package app;

import data_access.FileUserDataAccessObject;
import entity.UserFactory;
import interface_adapter.ViewManagerModel;
import interface_adapter.blank.BlankViewModel;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.login.LoginViewModel;
import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupPresenter;
import interface_adapter.signup.SignupViewModel;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInteractor;
import use_case.login.LoginOutputBoundary;
import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInteractor;
import use_case.signup.SignupOutputBoundary;
import view.BlankView;
import view.LoginView;
import view.SignupView;
import view.ViewManager;
import view.BaseView;

import javax.swing.*;
import java.awt.*;

public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();

    // Core app components
    private final UserFactory userFactory = new UserFactory();
    private final ViewManagerModel viewManagerModel = new ViewManagerModel();
    private final ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

    // DAO version using local file storage
    private final FileUserDataAccessObject userDataAccessObject =
            new FileUserDataAccessObject("users.csv", userFactory);

    // Views & ViewModels
    private LoginViewModel loginViewModel;
    private LoginView loginView;
    private BlankViewModel blankViewModel;
    private BlankView blankView;
    private SignupViewModel signupViewModel;
    private SignupView signupView;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    // === View registrations ===
    public AppBuilder addLoginView() {
        loginViewModel = new LoginViewModel();
        loginView = new LoginView(loginViewModel, viewManagerModel);
        cardPanel.add(loginView, loginView.getViewName());
        return this;
    }

    public AppBuilder addBlankView() {
        blankViewModel = new BlankViewModel();
        blankView = new BlankView(blankViewModel);
        cardPanel.add(blankView, blankView.getViewName());
        return this;
    }

    // New method for signup view
    public AppBuilder addSignupView() {
        signupViewModel = new SignupViewModel();
        signupView = new SignupView(signupViewModel, viewManagerModel);
        cardPanel.add(signupView, signupView.getViewName());
        return this;
    }

    // New method for signup use case
    public AppBuilder addSignupUseCase() {
        final SignupOutputBoundary signupOutputBoundary = new SignupPresenter(
                viewManagerModel,
                signupViewModel,
                loginViewModel
        );

        final SignupInputBoundary signupInteractor = new SignupInteractor(
                userDataAccessObject,
                signupOutputBoundary,
                userFactory
        );

        final SignupController signupController = new SignupController(signupInteractor);

        signupView.setSignupController(signupController);

        return this;
    }

    public AppBuilder addBaseView() {
        BaseView baseView = new BaseView();
        cardPanel.add(baseView, baseView.getViewName());
        return this;
    };

    // === Use case wiring ===
    public AppBuilder addLoginUseCase() {
        final LoginOutputBoundary loginOutputBoundary =
                new LoginPresenter(viewManagerModel, blankViewModel, loginViewModel);

        final LoginInputBoundary loginInteractor =
                new LoginInteractor(userDataAccessObject, loginOutputBoundary);

        LoginController loginController = new LoginController(loginInteractor);
        loginView.setLoginController(loginController);
        return this;
    }

    // === Build the final JFrame ===
    public JFrame build() {
        final JFrame application = new JFrame("User Login Example");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(cardPanel);

        // Start on the BaseView
        viewManagerModel.setState("BaseView");
        viewManagerModel.firePropertyChange();

        return application;
    }
}
