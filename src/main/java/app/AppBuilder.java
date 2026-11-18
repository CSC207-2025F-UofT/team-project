package app;

import data_access.InMemoryTimerDataAccess;
import entities.TimerSessionFactory;
import interface_adapters.ViewManagerModel;
import interface_adapters.cancel_timer.CancelTimerController;
import interface_adapters.cancel_timer.CancelTimerPresenter;
import interface_adapters.home.HomeViewModel;
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
import use_case.open_settings.OpenSettingsInputBoundary;
import use_case.open_settings.OpenSettingsInteractor;
import use_case.open_settings.OpenSettingsOutputBoundary;
import use_case.start_timer.StartTimerInputBoundary;
import use_case.start_timer.StartTimerInteractor;
import use_case.start_timer.StartTimerOutputBoundary;
import use_case.update_timer_settings.UpdateTimerSettingsInputBoundary;
import use_case.update_timer_settings.UpdateTimerSettingsInteractor;
import use_case.update_timer_settings.UpdateTimerSettingsOutputBoundary;
import view.FontManager;
import view.HomeView;
import view.TimerSettingsView;

import javax.swing.*;
import java.awt.*;

/**
 * Builds and initializes the application by creating its views and use cases.
 */
public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    TimerSessionFactory sessionFactory = new TimerSessionFactory();
    final ViewManagerModel viewManagerModel = new ViewManagerModel();

    InMemoryTimerDataAccess timerDataAccess = new InMemoryTimerDataAccess(sessionFactory);

    private TimerSettingsView timerSettingsView;
    private TimerSettingsViewModel timerSettingsViewModel;
    private HomeView homeView;
    private HomeViewModel homeViewModel;

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
        cardPanel.add(homeView, homeViewModel.VIEW_NAME);
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
     * Adds the Start Timer Settings Use Case to the application.
     * @return this builder
     */
    public AppBuilder addStartTimerUseCase() {
        final StartTimerOutputBoundary startTimerPresenter = new StartTimerPresenter(homeViewModel);
        final StartTimerInputBoundary startTimerInteractor = new StartTimerInteractor(startTimerPresenter, timerDataAccess);

        StartTimerController startTimerController = new StartTimerController(startTimerInteractor);
        homeView.setStartTimerController(startTimerController);
        return this;
    }

    public AppBuilder addCancelTimerUseCase() {
        final CancelTimerOutputBoundary cancelTimerPresenter = new CancelTimerPresenter(homeViewModel);
        final CancelTimerInputBoundary cancelTimerInteractor = new CancelTimerInteractor(cancelTimerPresenter, timerDataAccess);

        CancelTimerController cancelTimerController = new CancelTimerController(cancelTimerInteractor);
        homeView.setCancelTimerController(cancelTimerController);
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

        application.add(cardPanel);

        viewManagerModel.setState("home view");
        viewManagerModel.notifyViewChanged();

        return application;
    }

    // MOVE TO MAIN LATER
    public static void main(String[] args) {
        FontManager.loadFonts();

        AppBuilder appBuilder = new AppBuilder();
        JFrame application = appBuilder
                .addTimerSettingsView()
                .addHomeView()
                .addUpdateTimerSettingsUseCase()
                .addOpenSettingsUseCase()
                .addStartTimerUseCase()
                .addCancelTimerUseCase()
                .build();

        application.setLocationRelativeTo(null);
        application.setVisible(true);
    }
}
