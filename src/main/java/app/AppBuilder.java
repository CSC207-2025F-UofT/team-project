package app;

import data_access.InMemorySaveSessionDataAccess;
import entities.TimerSessionFactory;
import interface_adapters.ViewManagerModel;
import interface_adapters.save_session.SaveSessionController;
import interface_adapters.save_session.SaveSessionPresenter;
import interface_adapters.save_session.SaveSessionViewModel;
import use_case.save_session.SaveSessionDataAccessInterface;
import use_case.save_session.SaveSessionInputBoundary;
import use_case.save_session.SaveSessionInteractor;
import use_case.save_session.SaveSessionOutputBoundary;
import view.HomeView;
import view.SaveSessionView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Builds and initializes the application by creating its views and use cases.
 */
public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    TimerSessionFactory sessionFactory = new TimerSessionFactory();
    final ViewManagerModel viewManagerModel = new ViewManagerModel();

    SaveSessionDataAccessInterface dataAccessInterface = new InMemorySaveSessionDataAccess(sessionFactory);

    private SaveSessionView saveSessionView;
    private SaveSessionViewModel saveSessionViewModel;
    private HomeView homeView;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    /**
     * Adds the Save Session View to the application.
     * The save session view is displayed in a separate popup window rather than being added to the card panel.
     * @return this builder
     */
    public AppBuilder addSaveSessionView() {
        // We do not add this to the cardPanel because it is a separate popup JFrame
        saveSessionViewModel = new SaveSessionViewModel();
        saveSessionView = new SaveSessionView(saveSessionViewModel);
        return this;
    }

    /**
     * Adds the Home View to the application.
     * @return this builder
     */
    public AppBuilder addHomeView() {
        homeView = new HomeView();

        homeView.addAdjustButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Displays the popup window: SaveSessionView
                saveSessionView.showWindow();
            }
        });

        cardPanel.add(homeView, "home view");
        return this;
    }

    /**
     * Adds the Save Session Use Case to the application.
     * @return this builder
     */
    public AppBuilder addSaveSessionUseCase() {
        final SaveSessionOutputBoundary saveSessionOutputBoundary = new SaveSessionPresenter(saveSessionViewModel);
        final SaveSessionInputBoundary saveSessionInteractor = new SaveSessionInteractor(
                saveSessionOutputBoundary, dataAccessInterface, sessionFactory);

        SaveSessionController controller = new SaveSessionController(saveSessionInteractor);
        saveSessionView.setController(controller);
        return this;
    }

    /**
     * ABuilds the main application window
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
        AppBuilder appBuilder = new AppBuilder();
        JFrame application = appBuilder
                .addSaveSessionView()
                .addSaveSessionUseCase()
                .addHomeView()
                .build();

        application.setLocationRelativeTo(null);
        application.setVisible(true);
    }
}
