package app;

import data_access.FileUserDataAccessObject;
import data_access.JsonFileLandmarkDataAccessObject;
import data_access.*;
import entity.UserFactory;
import interface_adapter.ViewManagerModel;
import interface_adapter.browselandmarks.BrowseLandmarksController;
import interface_adapter.browselandmarks.BrowseLandmarksPresenter;
import interface_adapter.browselandmarks.BrowseLandmarksViewModel;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.login.LoginViewModel;
import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupPresenter;
import interface_adapter.signup.SignupViewModel;
import use_case.browselandmarks.BrowseLandmarksInteractor;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInteractor;
import use_case.login.LoginOutputBoundary;
import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInteractor;
import use_case.signup.SignupOutputBoundary;
import view.BrowseLandmarksView;
import view.LoginView;
import view.SignupView;
import view.ViewManager;

import interface_adapter.homescreen.HomescreenController;
import interface_adapter.homescreen.HomescreenPresenter;
import use_case.homescreen.HomescreenInputBoundary;
import use_case.homescreen.HomescreenInteractor;
import use_case.homescreen.HomescreenOutputBoundary;


import interface_adapter.homescreen.HomescreenViewModel;
import view.HomescreenView;

import javax.swing.*;
import java.awt.*;

public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();

    // Core app components
    private final UserFactory userFactory = new UserFactory();
    private final ViewManagerModel viewManagerModel = new ViewManagerModel();
    private final ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

    // DAO version using local csv file storage
//    private final UserDataAccessInterface userDataAccessObject =
//            new FileUserDataAccessObject("users.csv", userFactory);

    // DAO version using local json file storage
    private final UserDataAccessInterface userDataAccessObject =
            new JSONFileUserDataAccessObject("users.json");

    // Views & ViewModels
    private LoginViewModel loginViewModel;
    private LoginView loginView;
    private BrowseLandmarksViewModel browseLandmarksViewModel;
    private BrowseLandmarksView browseLandmarksView;
    private SignupViewModel signupViewModel;
    private SignupView signupView;
    private HomescreenViewModel homescreenViewModel;
    private HomescreenView homescreenView;

    private BrowseLandmarksPresenter browseLandmarksPresenter;
    private JsonFileLandmarkDataAccessObject landmarkDAO;
    private BrowseLandmarksInteractor browseLandmarksInteractor;
    private BrowseLandmarksController browseLandmarksController;

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

    public AppBuilder addHomescreenView() {
        homescreenViewModel = new HomescreenViewModel();
        homescreenView = new HomescreenView(homescreenViewModel, viewManagerModel);  // Keep this
        cardPanel.add(homescreenView, homescreenView.getViewName());
        return this;
    }

    public AppBuilder addBrowseLandmarksView() {
        browseLandmarksViewModel = new BrowseLandmarksViewModel();
        browseLandmarksPresenter = new BrowseLandmarksPresenter(browseLandmarksViewModel);
        landmarkDAO = new JsonFileLandmarkDataAccessObject("minimal_landmarks.json");
        browseLandmarksInteractor = new BrowseLandmarksInteractor(landmarkDAO, browseLandmarksPresenter);
        browseLandmarksController =  new BrowseLandmarksController(browseLandmarksInteractor);
        browseLandmarksView = new BrowseLandmarksView(browseLandmarksViewModel, browseLandmarksController, viewManagerModel);
        cardPanel.add(browseLandmarksView, browseLandmarksView.getViewName());
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
    // new method for homescreen use case
    public AppBuilder addHomescreenUseCase() {
        final HomescreenOutputBoundary homescreenOutputBoundary =
                new HomescreenPresenter(homescreenViewModel, viewManagerModel, browseLandmarksViewModel);

        final HomescreenInputBoundary homescreenInteractor =
                new HomescreenInteractor(homescreenOutputBoundary);

        final HomescreenController homescreenController =
                new HomescreenController(homescreenInteractor);

        homescreenView.setHomescreenController(homescreenController);

        return this;
    }

    // === Use case wiring ===
    public AppBuilder addLoginUseCase() {
        final LoginOutputBoundary loginOutputBoundary =
                new LoginPresenter(viewManagerModel, homescreenViewModel, loginViewModel);

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

        // Start on the login view
        viewManagerModel.setState(loginView.getViewName());
        viewManagerModel.firePropertyChange();

        return application;
    }
}
