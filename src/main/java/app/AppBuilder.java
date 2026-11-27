package app;

import data_access.FilePlayerDataAccessObject;
import entity.PlayerDataFactory;
import interface_adapter.ViewManagerModel;
import interface_adapter.lobby.LobbyViewModel;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.login.LoginViewModel;
import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupPresenter;
import interface_adapter.signup.SignupViewModel;
import interface_adapter.viewplayerstats.ViewPlayerStatsViewModel;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInteractor;
import use_case.login.LoginOutputBoundary;
import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInteractor;
import use_case.signup.SignupOutputBoundary;
import view.LobbyView;
import view.LoginView;
import view.SignupView;
import view.ViewManager;
import view.ViewPlayerStatsView;

import javax.swing.*;
import java.awt.*;

/**
 * Builds the application: creates every layer and wires them together.
 * This is the only class that knows about all the concrete classes.
 *
 * <p>To add a feature: write an addXView() method that creates the view model and view
 * and adds the view to cardPanel under VIEW_NAME, and an addXUseCase() method that
 * creates the presenter, interactor and controller and gives the controller to the
 * view. Then call both from Main.</p>
 */
public class AppBuilder {

    /** Where player accounts and stats are saved, relative to where the app is run. */
    private static final String PLAYER_FILE = "players.csv";

    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    private final ViewManagerModel viewManagerModel = new ViewManagerModel();
    private final ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

    // One shared storage object, so login, sign up and stats all see the same players
    private final FilePlayerDataAccessObject playerDataAccessObject =
            new FilePlayerDataAccessObject(PLAYER_FILE);

    private LoginViewModel loginViewModel;
    private LoginView loginView;
    private SignupViewModel signupViewModel;
    private SignupView signupView;
    private LobbyViewModel lobbyViewModel;

    /**
     * Creates an empty builder.
     */
    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    /**
     * Adds the login screen.
     *
     * @return this builder
     */
    public AppBuilder addLoginView() {
        loginViewModel = new LoginViewModel();
        loginView = new LoginView(loginViewModel);
        cardPanel.add(loginView, LoginViewModel.VIEW_NAME);
        return this;
    }

    /**
     * Adds the sign up screen.
     *
     * @return this builder
     */
    public AppBuilder addSignupView() {
        signupViewModel = new SignupViewModel();
        signupView = new SignupView(signupViewModel);
        cardPanel.add(signupView, SignupViewModel.VIEW_NAME);
        return this;
    }

    /**
     * Adds the (placeholder) lobby, where login goes on success.
     *
     * @return this builder
     */
    public AppBuilder addLobbyView() {
        lobbyViewModel = new LobbyViewModel();
        final LobbyView lobbyView =
                new LobbyView(lobbyViewModel);

        lobbyView.addViewStatsListener(event -> {
            viewManagerModel.setState(
                    ViewPlayerStatsViewModel.VIEW_NAME);
            viewManagerModel.notifyViewChanged();
        });
        cardPanel.add(lobbyView, LobbyViewModel.VIEW_NAME);
        return this;
    }

    /**
     * Creates and registers the player statistics screen.
     *
     * @return this builder
     */
    public AppBuilder addViewPlayerStatsView() {
        final ViewPlayerStatsView statsView =
                ViewPlayerStatsFactory.create(
                        playerDataAccessObject,
                        playerDataAccessObject);

        statsView.addBackListener(event -> {
            viewManagerModel.setState(LobbyViewModel.VIEW_NAME);
            viewManagerModel.notifyViewChanged();
        });

        cardPanel.add(
                statsView,
                ViewPlayerStatsViewModel.VIEW_NAME);

        return this;
    }
    /**
     * Wires the Login use case. Call after the login, sign up and lobby views are added.
     *
     * @return this builder
     */
    public AppBuilder addLoginUseCase() {
        final LoginOutputBoundary loginPresenter = new LoginPresenter(
                viewManagerModel, loginViewModel, signupViewModel, lobbyViewModel);
        final LoginInputBoundary loginInteractor = new LoginInteractor(
                playerDataAccessObject, loginPresenter);
        loginView.setLoginController(new LoginController(loginInteractor));
        return this;
    }

    /**
     * Wires the Sign Up use case. Call after the login and sign up views are added.
     *
     * @return this builder
     */
    public AppBuilder addSignupUseCase() {
        final SignupOutputBoundary signupPresenter = new SignupPresenter(
                viewManagerModel, signupViewModel, loginViewModel);
        final SignupInputBoundary signupInteractor = new SignupInteractor(
                playerDataAccessObject, signupPresenter, new PlayerDataFactory());
        signupView.setSignupController(new SignupController(signupInteractor));
        return this;
    }

    /**
     * Creates the window, starting on the login screen.
     *
     * @return the application window, ready to show
     */
    public JFrame build() {
        final JFrame application = new JFrame("UNO Desktop");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        application.add(cardPanel);

        viewManagerModel.setState(LoginViewModel.VIEW_NAME);
        viewManagerModel.notifyViewChanged();
        return application;
    }
}
