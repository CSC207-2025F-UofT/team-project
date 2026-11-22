package app;

import data_access.FlashcardDAO;
import data_access.FlashcardDataAccessInterface;
import data_access.InMemoryTimerDataAccess;
import entities.TimerSessionFactory;
import interface_adapters.ViewManagerModel;
import interface_adapters.cancel_timer.CancelTimerController;
import interface_adapters.cancel_timer.CancelTimerPresenter;
import interface_adapters.flashcard_generator.FlashcardGeneratorController;
import interface_adapters.flashcard_generator.FlashcardGeneratorPresenter;
import interface_adapters.flashcard_generator.FlashcardGeneratorViewModel;
import interface_adapters.flashcard_studying.FlashcardStudyingViewModel;
import interface_adapters.flashcard_studying.FlashcardStudyingController;
import interface_adapters.flashcard_studying.FlashcardStudyingPresenter;
import interface_adapters.home.HomeViewModel;
import interface_adapters.menu_bar.NavigationBarViewModel;
import interface_adapters.menu_bar.SwitchViewController;
import interface_adapters.menu_bar.SwitchViewPresenter;
import interface_adapters.open_settings.OpenSettingsController;
import interface_adapters.open_settings.OpenSettingsPresenter;
import interface_adapters.start_timer.StartTimerController;
import interface_adapters.start_timer.StartTimerPresenter;
import interface_adapters.update_timer_settings.UpdateTimerSettingsController;
import interface_adapters.update_timer_settings.UpdateTimerSettingsPresenter;
import interface_adapters.update_timer_settings.TimerSettingsViewModel;
import use_case.cancel_timer.CancelTimerInputBoundary;
import use_case.cancel_timer.CancelTimerInteractor;
import use_case.cancel_timer.CancelTimerOutputBoundary;
import use_case.flashcard_generator.FlashcardGeneratorInputBoundary;
import use_case.flashcard_generator.FlashcardGeneratorInteractor;
import use_case.flashcard_generator.FlashcardGeneratorOutputBoundary;
import use_case.flashcard_studying.FlashcardStudyingInputBoundary;
import use_case.flashcard_studying.FlashcardStudyingInteractor;
import use_case.flashcard_studying.FlashcardStudyingOutputBoundary;
import use_case.menu_bar.SwitchViewInputBoundary;
import use_case.menu_bar.SwitchViewInteractor;
import use_case.menu_bar.SwitchViewOutputBoundary;
import use_case.open_settings.OpenSettingsInputBoundary;
import use_case.open_settings.OpenSettingsInteractor;
import use_case.open_settings.OpenSettingsOutputBoundary;
import use_case.start_timer.StartTimerInputBoundary;
import use_case.start_timer.StartTimerInteractor;
import use_case.start_timer.StartTimerOutputBoundary;
import use_case.update_timer_settings.UpdateTimerSettingsInputBoundary;
import use_case.update_timer_settings.UpdateTimerSettingsInteractor;
import use_case.update_timer_settings.UpdateTimerSettingsOutputBoundary;
import view.*;

import javax.swing.*;
import java.awt.*;

/**
 * Builds and initializes the application by creating its views and use cases.
 */
public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    final TimerSessionFactory sessionFactory = new TimerSessionFactory();
    final ViewManagerModel viewManagerModel = new ViewManagerModel();
    ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);


    InMemoryTimerDataAccess timerDataAccess = new InMemoryTimerDataAccess(sessionFactory);

    private TimerSettingsView timerSettingsView;
    private TimerSettingsViewModel timerSettingsViewModel;
    private HomeView homeView;
    private HomeViewModel homeViewModel;

    private NavigationBarViewModel navigationBarViewModel;
    private NavigationBarPanel navigationBarPanel;

    private FlashcardStudyingView flashcardStudyingView;
    private FlashcardStudyingViewModel flashcardStudyingViewModel;

    private FlashcardGeneratorView flashcardGeneratorView;
    private FlashcardGeneratorViewModel flashcardGeneratorViewModel;


    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    /**
     * Adds the Update Timer Settings View to the application.
     * The timer settings view is displayed in a separate popup window rather than being added to the card panel.
     * @return this builder
     */
    public AppBuilder addTimerSettingsView() {
        // We do not add this to the cardPanel because it is a separate popup JFrame
        timerSettingsViewModel = new TimerSettingsViewModel();
        timerSettingsView = new TimerSettingsView(timerSettingsViewModel);
        return this;
    }

    /**
     * Adds the Home View to the application.
     * @return this builder
     */
    public AppBuilder addHomeView() {
        homeViewModel = new HomeViewModel();
        homeView = new HomeView(homeViewModel);
        cardPanel.add(homeView, ViewNames.HOME);
        return this;
    }

    /**
     * Adds the Flashcard Studying View to the application.
     * @return this builder
     */
    public AppBuilder addFSView() {
        flashcardStudyingViewModel = new FlashcardStudyingViewModel();
        flashcardStudyingView = new FlashcardStudyingView(flashcardStudyingViewModel);
        cardPanel.add(flashcardStudyingView, ViewNames.STUDY_FLASHCARDS);
        return this;
    }

    /**
     * Adds flashcard studying use case to AppBuilder
     * @return
     */
    public AppBuilder addFSUseCase(){
        // Initialize presenter and DAO
        final FlashcardStudyingOutputBoundary presenter = new FlashcardStudyingPresenter(
                flashcardStudyingViewModel);

        FlashcardDataAccessInterface DAO = new FlashcardDAO();

        // Initialize Interactor
        final FlashcardStudyingInputBoundary interactor = new FlashcardStudyingInteractor(presenter, DAO);

        // Initialize controller
        final FlashcardStudyingController controller = new FlashcardStudyingController(interactor);

        // Set Controller for View
        flashcardStudyingView.setController(controller);

        return this;
    }

    public AppBuilder addNavigationBar() {
        navigationBarViewModel = new NavigationBarViewModel();
        navigationBarPanel = new NavigationBarPanel(navigationBarViewModel);
        return this;
    }

    /**
     * Adds the Open Settings Use Case to the application.
     * @return this builder
     */
    public AppBuilder addOpenSettingsUseCase() {
        final OpenSettingsOutputBoundary openSettingsPresenter = new OpenSettingsPresenter(timerSettingsViewModel);
        final OpenSettingsInputBoundary openSettingsInteractor = new OpenSettingsInteractor(openSettingsPresenter);

        OpenSettingsController controller = new OpenSettingsController(openSettingsInteractor);
        homeView.setSettingsController(controller);
        return this;
    }

    /**
     * Adds the Update Timer Settings Use Case to the application.
     * @return this builder
     */
    public AppBuilder addUpdateTimerSettingsUseCase() {
        final UpdateTimerSettingsOutputBoundary saveSessionPresenter = new UpdateTimerSettingsPresenter(timerSettingsViewModel, homeViewModel);
        final UpdateTimerSettingsInputBoundary saveSessionInteractor = new UpdateTimerSettingsInteractor(
                saveSessionPresenter, timerDataAccess, sessionFactory);

        UpdateTimerSettingsController controller = new UpdateTimerSettingsController(saveSessionInteractor);
        timerSettingsView.setController(controller);
        return this;
    }

    /**
     * Adds the Start Timer Use Case to the application.
     * @return this builder
     */
    public AppBuilder addStartTimerUseCase() {
        final StartTimerOutputBoundary startTimerPresenter = new StartTimerPresenter(homeViewModel, navigationBarViewModel);
        final StartTimerInputBoundary startTimerInteractor = new StartTimerInteractor(startTimerPresenter, timerDataAccess);

        StartTimerController startTimerController = new StartTimerController(startTimerInteractor);
        homeView.setStartTimerController(startTimerController);
        return this;
    }

    /**
     * Adds the Cancel Timer Use Case to the application.
     * @return this builder
     */
    public AppBuilder addCancelTimerUseCase() {
        final CancelTimerOutputBoundary cancelTimerPresenter = new CancelTimerPresenter(homeViewModel, navigationBarViewModel);
        final CancelTimerInputBoundary cancelTimerInteractor = new CancelTimerInteractor(cancelTimerPresenter, timerDataAccess);

        CancelTimerController cancelTimerController = new CancelTimerController(cancelTimerInteractor);
        homeView.setCancelTimerController(cancelTimerController);
        return this;
    }

    /**
     * Adds the Switch View Use Case (for the menu bar) to the application.
     * @return this builder
     */
    public AppBuilder addSwitchViewUseCase() {
        final SwitchViewOutputBoundary switchViewPresenter = new SwitchViewPresenter(navigationBarViewModel, viewManagerModel);
        final SwitchViewInputBoundary switchViewInteractor = new SwitchViewInteractor(switchViewPresenter);

        SwitchViewController switchViewController = new SwitchViewController(switchViewInteractor);
        navigationBarPanel.setSwitchViewController(switchViewController);
        return this;
    }

    /**
     * Adds the FlashcardGeneratorView to the application
     * @return this builder
     */
    public AppBuilder addFlashcardGenerator(){
        flashcardGeneratorView = new FlashcardGeneratorView();

        flashcardGeneratorViewModel = new FlashcardGeneratorViewModel();

        cardPanel.add(flashcardGeneratorView,ViewNames.MAKE_FLASHCARDS);
        return this;

    }

    /**
     * Adds flashcard generator use case to AppBuilder
     * @return
     */
    public AppBuilder addFlashcardGeneratorUseCase(){
        // Initialize presenter and DAO
        final FlashcardGeneratorOutputBoundary presenter = new FlashcardGeneratorPresenter(
                flashcardGeneratorViewModel
                ,viewManagerModel,
                flashcardStudyingViewModel);

        FlashcardDataAccessInterface DAO = new FlashcardDAO();

        // Initialize Interactor
        final FlashcardGeneratorInputBoundary interactor = new FlashcardGeneratorInteractor(DAO, presenter);

        // Initialize controller
        final FlashcardGeneratorController controller = new FlashcardGeneratorController(interactor);

        // Set Controller for View
        flashcardGeneratorView.setController(controller);


        return this;
    }

    /**
     * Builds the main application window
     * @return the JFrame application
     */
    public JFrame build() {
        final JFrame application = new JFrame();
        application.setSize(new Dimension(800, 600));
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.setLayout(new BorderLayout());
        application.add(navigationBarPanel, BorderLayout.NORTH);
        application.add(cardPanel, BorderLayout.CENTER);

        navigationBarViewModel.getState().setCurrentViewName(ViewNames.HOME);
        navigationBarViewModel.notifyStateChanged();

        viewManagerModel.setState(ViewNames.HOME);
        viewManagerModel.notifyViewChanged();

        // TODO: REMOVE LATER
        JFrame testFrame = new JFrame("Timer Sync Test");
        testFrame.setSize(400, 200);
        testFrame.add(new TimerDisplayPanel(homeViewModel));
        testFrame.setLocation(100, 100);
        testFrame.setVisible(true);

        return application;
    }
}
