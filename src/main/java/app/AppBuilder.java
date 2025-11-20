package app;

import data_access.FileUserDataAccessObject;
import data_access.JsonLandmarkDataAccessObject;
import data_access.LandmarkDataAccessInterface;
import data_access.UserDataAccessInterface;
import entity.UserFactory;
import interface_adapter.ViewManagerModel;
import interface_adapter.browselandmarks.BrowseLandmarksController;
import interface_adapter.browselandmarks.BrowseLandmarksPresenter;
import interface_adapter.browselandmarks.BrowseLandmarksViewModel;
import interface_adapter.homescreen.HomescreenController;
import interface_adapter.homescreen.HomescreenPresenter;
import interface_adapter.homescreen.HomescreenViewModel;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.login.LoginViewModel;
import interface_adapter.selectedplace.SelectedPlaceController;
import interface_adapter.selectedplace.SelectedPlacePresenter;
import interface_adapter.selectedplace.SelectedPlaceViewModel;
import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupPresenter;
import interface_adapter.signup.SignupViewModel;
import use_case.browselandmarks.BrowseLandmarksInputBoundary;
import use_case.browselandmarks.BrowseLandmarksInteractor;
import use_case.homescreen.HomescreenInputBoundary;
import use_case.homescreen.HomescreenInteractor;
import use_case.homescreen.HomescreenOutputBoundary;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInteractor;
import use_case.login.LoginOutputBoundary;
import use_case.selectedplace.SelectedPlaceInputBoundary;
import use_case.selectedplace.SelectedPlaceInteractor;
import use_case.selectedplace.SelectedPlaceOutputBoundary;
import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInteractor;
import use_case.signup.SignupOutputBoundary;
import view.BrowseLandmarksView;
import view.HomescreenView;
import view.LoginView;
import view.SelectedPlaceView;
import view.SignupView;
import view.ViewManager;

import javax.swing.*;
import java.awt.*;

public class AppBuilder {

    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();

    // core
    private final UserFactory userFactory = new UserFactory();
    private final ViewManagerModel viewManagerModel = new ViewManagerModel();
    @SuppressWarnings("unused")
    private final ViewManager viewManager =
            new ViewManager(cardPanel, cardLayout, viewManagerModel);

    private LandmarkDataAccessInterface landmarkDAO = new JsonLandmarkDataAccessObject("minimal_landmarks.json");

    private final UserDataAccessInterface userDataAccessObject =
            new FileUserDataAccessObject("users.json", userFactory, landmarkDAO);

    // ---- view models & views ----
    private LoginViewModel loginViewModel;
    private LoginView loginView;

    private SignupViewModel signupViewModel;
    private SignupView signupView;

    private HomescreenViewModel homescreenViewModel;
    private HomescreenView homescreenView;

    private BrowseLandmarksViewModel browseLandmarksViewModel;
    private BrowseLandmarksView browseLandmarksView;

    private SelectedPlaceViewModel selectedPlaceViewModel;
    private SelectedPlaceView selectedPlaceView;

    // ---- use case controllers ----
    private SelectedPlaceController selectedPlaceController;
    private BrowseLandmarksController browseLandmarksController;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    // === VIEW REGISTRATION ===

    public AppBuilder addLoginView() {
        loginViewModel = new LoginViewModel();
        loginView = new LoginView(loginViewModel, viewManagerModel);
        cardPanel.add(loginView, loginView.getViewName());
        return this;
    }

    public AppBuilder addHomescreenView() {
        homescreenViewModel = new HomescreenViewModel();
        homescreenView = new HomescreenView(homescreenViewModel, viewManagerModel);
        cardPanel.add(homescreenView, homescreenView.getViewName());
        return this;
    }

    public AppBuilder addSignupView() {
        signupViewModel = new SignupViewModel();
        signupView = new SignupView(signupViewModel, viewManagerModel);
        cardPanel.add(signupView, signupView.getViewName());
        return this;
    }

    /** STEP 1 — Create ONLY SelectedPlaceView */
    public AppBuilder addSelectedPlaceView() {
        selectedPlaceViewModel = new SelectedPlaceViewModel();
        selectedPlaceView = new SelectedPlaceView(selectedPlaceViewModel, viewManagerModel);
        cardPanel.add(selectedPlaceView, selectedPlaceView.getViewName());
        return this;
    }

    /** STEP 2 — Wire SelectedPlace Use Case + Controller */
    public AppBuilder addSelectedPlaceUseCase() {
        SelectedPlaceOutputBoundary spPresenter =
                new SelectedPlacePresenter(selectedPlaceViewModel, viewManagerModel);

        SelectedPlaceInputBoundary spInteractor =
                new SelectedPlaceInteractor(landmarkDAO, userDataAccessObject, spPresenter);

        selectedPlaceController = new SelectedPlaceController(spInteractor);
        selectedPlaceView.setSelectedPlaceController(selectedPlaceController);

        return this;
    }

    /** BrowseLandmarks depends on selectedPlaceController. */
    public AppBuilder addBrowseLandmarksView() {
        browseLandmarksViewModel = new BrowseLandmarksViewModel();
        BrowseLandmarksPresenter presenter =
                new BrowseLandmarksPresenter(browseLandmarksViewModel);

        BrowseLandmarksInputBoundary interactor =
                new BrowseLandmarksInteractor(landmarkDAO, presenter, userDataAccessObject);

        browseLandmarksController = new BrowseLandmarksController(interactor);

        browseLandmarksView = new BrowseLandmarksView(
                browseLandmarksViewModel,
                browseLandmarksController,
                selectedPlaceController,     // now safe
                viewManagerModel
        );

        cardPanel.add(browseLandmarksView, browseLandmarksView.getViewName());
        return this;
    }

    // === USE CASE WIRING ===

    public AppBuilder addSignupUseCase() {
        SignupOutputBoundary output =
                new SignupPresenter(viewManagerModel, signupViewModel, loginViewModel);

        SignupInputBoundary interactor =
                new SignupInteractor(userDataAccessObject, output, userFactory);

        SignupController controller = new SignupController(interactor);
        signupView.setSignupController(controller);
        return this;
    }

    public AppBuilder addHomescreenUseCase() {
        HomescreenOutputBoundary output =
                new HomescreenPresenter(homescreenViewModel, viewManagerModel, browseLandmarksViewModel);

        HomescreenInputBoundary interactor =
                new HomescreenInteractor(output);

        HomescreenController controller =
                new HomescreenController(interactor);

        homescreenView.setHomescreenController(controller);
        return this;
    }

    public AppBuilder addLoginUseCase() {
        LoginOutputBoundary output =
                new LoginPresenter(viewManagerModel, homescreenViewModel, loginViewModel);

        LoginInputBoundary interactor =
                new LoginInteractor(userDataAccessObject, output);

        LoginController controller =
                new LoginController(interactor);

        loginView.setLoginController(controller);
        return this;
    }

    // === BUILD ===

    public JFrame build() {
        JFrame app = new JFrame("User Login Example");
        app.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        app.add(cardPanel);

        viewManagerModel.setState(loginView.getViewName());
        viewManagerModel.firePropertyChange();

        return app;
    }
}