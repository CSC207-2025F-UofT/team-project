package app;

import interface_adapter.logout.LogoutController;
import interface_adapter.logout.LogoutPresenter;
import use_case.logout.LogoutInputBoundary;
import use_case.logout.LogoutInteractor;
import use_case.logout.LogoutOutputBoundary;
import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupPresenter;
import interface_adapter.signup.SignupViewModel;
import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInteractor;
import use_case.signup.SignupOutputBoundary;
import view.SignupView;
import data_access.InMemoryUserDataAccessObject;
import data_access.Pbkdf2PasswordHasher;
import entity.UserSession;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.login.LoginViewModel;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInteractor;
import use_case.login.LoginOutputBoundary;
import use_case.user.PasswordHasher;
import use_case.user.UserDataAccessInterface;
import view.LoginView;
import data_access.InMemoryGameStateDataAccessObject;
import data_access.JsonDialogueDataAccessObject;
import interface_adapter.ViewManagerModel;
import interface_adapter.hud.HudViewModel;
import interface_adapter.new_game.NewGameController;
import interface_adapter.new_game.NewGamePresenter;
import interface_adapter.talk_to_npc.DialogueViewModel;
import interface_adapter.talk_to_npc.TalkToNpcController;
import interface_adapter.talk_to_npc.TalkToNpcPresenter;
import use_case.GameStateDataAccessInterface;
import use_case.new_game.NewGameInputBoundary;
import use_case.new_game.NewGameInteractor;
import use_case.new_game.NewGameOutputBoundary;
import use_case.talk_to_npc.DialogueDataAccessInterface;
import use_case.talk_to_npc.TalkToNpcInputBoundary;
import use_case.talk_to_npc.TalkToNpcInteractor;
import use_case.talk_to_npc.TalkToNpcOutputBoundary;
import view.DialogueView;
import view.GameView;
import view.TitleView;
import view.ViewManager;

import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 * Wires the app together (Dependency Injection). This is the only class
 * that knows every concrete class; each story adds its own add...View()
 * and add...UseCase() methods here.
 *
 * Call order: add views first, then use cases, then build().
 */
public class AppBuilder {

    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    private final ViewManagerModel viewManagerModel = new ViewManagerModel();
    private final ViewManager viewManager;
    private LogoutController logoutController;

    private final GameStateDataAccessInterface gameStateDataAccessObject =
            new InMemoryGameStateDataAccessObject();

    private HudViewModel hudViewModel;
    private GameView gameView;
    private TitleView titleView;
    private final UserDataAccessInterface userDataAccessObject = new InMemoryUserDataAccessObject();
    private final PasswordHasher passwordHasher = new Pbkdf2PasswordHasher();
    private final UserSession userSession = new UserSession();
    private LoginViewModel loginViewModel;
    private LoginView loginView;
    private SignupViewModel signupViewModel;
    private SignupView signupView;
    private DialogueViewModel dialogueViewModel;
    private DialogueView dialogueView;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
        viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);
    }

    public AppBuilder addGameView() {
        hudViewModel = new HudViewModel();
        gameView = new GameView(hudViewModel);
        cardPanel.add(gameView, gameView.getViewName());
        return this;
    }

    /** Adds the New Game use case and its temporary start screen. Requires addGameView(). */
    public AppBuilder addNewGameUseCase() {
        NewGameOutputBoundary presenter =
                new NewGamePresenter(hudViewModel, viewManagerModel, gameView.getViewName());
        NewGameInputBoundary interactor = new NewGameInteractor(gameStateDataAccessObject, presenter);
        NewGameController controller = new NewGameController(interactor);

        titleView = new TitleView(controller);
        cardPanel.add(titleView, titleView.getViewName());
        return this;
    }

    /** Adds the conversation screen. Requires addGameView(), because it shows the same stats. */
    public AppBuilder addDialogueView() {
        dialogueViewModel = new DialogueViewModel();
        dialogueView = new DialogueView(dialogueViewModel, hudViewModel);
        cardPanel.add(dialogueView, dialogueView.getViewName());
        return this;
    }

    /**
     * Adds the Talk to an NPC use case. Requires addGameView() and
     * addDialogueView(), because both screens have buttons that use it.
     */
    public AppBuilder addTalkToNpcUseCase() {
        DialogueDataAccessInterface dialogueDataAccessObject =
                new JsonDialogueDataAccessObject("dialogues.json");
        TalkToNpcOutputBoundary presenter = new TalkToNpcPresenter(
                dialogueViewModel, hudViewModel, viewManagerModel, gameView.getViewName());
        TalkToNpcInputBoundary interactor = new TalkToNpcInteractor(
                gameStateDataAccessObject, dialogueDataAccessObject, presenter);
        TalkToNpcController controller = new TalkToNpcController(interactor);

        gameView.setTalkToNpcController(controller);
        dialogueView.setTalkToNpcController(controller);
        return this;
    }

    public AppBuilder addLoginView(){
        loginViewModel = new LoginViewModel();
        loginView = new LoginView(loginViewModel);

        loginView.setViewManagerModel(viewManagerModel);

        cardPanel.add(loginView, loginView.getViewName());

        return this;
    }

    public AppBuilder addLoginUseCase(){
        LoginOutputBoundary presenter = new LoginPresenter(loginViewModel, viewManagerModel, titleView.getViewName());
        LoginInputBoundary interactor = new LoginInteractor(userDataAccessObject, presenter, passwordHasher, userSession);
        LoginController controller = new LoginController(interactor);

        loginView.setLoginController(controller);

        return this;
    }

    public AppBuilder addSignupView() {
        signupViewModel = new SignupViewModel();
        signupView = new SignupView(signupViewModel);

        signupView.setViewManagerModel(viewManagerModel);

        cardPanel.add(signupView, signupView.getViewName());

        return this;
    }

    public AppBuilder addSignupUseCase() {
        SignupOutputBoundary presenter =
                new SignupPresenter(
                        signupViewModel,
                        viewManagerModel,
                        loginView.getViewName()
                );

        SignupInputBoundary interactor =
                new SignupInteractor(
                        userDataAccessObject,
                        presenter,
                        passwordHasher
                );

        SignupController controller = new SignupController(interactor);

        signupView.setSignupController(controller);

        return this;
    }

    public AppBuilder addLogoutUseCase() {
        LogoutOutputBoundary presenter =
                new LogoutPresenter(
                        viewManagerModel,
                        loginView.getViewName(),
                        loginView::clearFields
                );

        LogoutInputBoundary interactor =
                new LogoutInteractor(
                        presenter,
                        userSession
                );

        logoutController = new LogoutController(interactor);

        titleView.setLogoutController(logoutController);

        return this;
    }

    public JFrame build() {
        JFrame application = new JFrame("UofTears: Survive the Semester");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        application.add(cardPanel);

        viewManagerModel.setState(loginView.getViewName());
        viewManagerModel.firePropertyChanged();
        return application;
    }
}
