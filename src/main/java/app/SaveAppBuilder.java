package app;

import data_access.FileSaveDataAccessObject;
import interface_adapter.ViewManagerModel;
import interface_adapter.leaderboard.LeaderboardController;
import interface_adapter.leaderboard.LeaderboardPresenter;
import interface_adapter.load.LoadController;
import interface_adapter.load.LoadPresenter;
import interface_adapter.save.SaveController;
import interface_adapter.save.SavePresenter;
import interface_adapter.save.SaveViewModel;
import use_case.leaderboard.LeaderboardInputBoundary;
import use_case.leaderboard.LeaderboardInteractor;
import use_case.leaderboard.LeaderboardOutputBoundary;
import use_case.load.LoadInputBoundary;
import use_case.load.LoadInteractor;
import use_case.load.LoadOutputBoundary;
import use_case.save.SaveInputBoundary;
import use_case.save.SaveInteractor;
import use_case.save.SaveOutputBoundary;
import view.SaveLeaderboardView;
import view.ViewManager;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.CardLayout;

/**
 * Wires up Use Case 4 (save + leaderboard) so it can run on its own.
 */
public class SaveAppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    private final ViewManagerModel viewManagerModel = new ViewManagerModel();
    private final FileSaveDataAccessObject saveDataAccess;

    private SaveViewModel saveViewModel;
    private SaveLeaderboardView saveView;

    public SaveAppBuilder(FileSaveDataAccessObject saveDataAccess) {
        this.saveDataAccess = saveDataAccess;
        cardPanel.setLayout(cardLayout);
        new ViewManager(cardPanel, cardLayout, viewManagerModel);
    }

    public SaveAppBuilder addSaveView() {
        saveViewModel = new SaveViewModel();
        saveView = new SaveLeaderboardView(saveViewModel);
        cardPanel.add(saveView, saveViewModel.getViewName());
        return this;
    }

    public SaveAppBuilder addSaveUseCase() {
        SaveOutputBoundary presenter = new SavePresenter(saveViewModel);
        SaveInputBoundary interactor = new SaveInteractor(saveDataAccess, presenter);
        saveView.setSaveController(new SaveController(interactor));
        return this;
    }

    public SaveAppBuilder addLeaderboardUseCase() {
        LeaderboardOutputBoundary presenter = new LeaderboardPresenter(saveViewModel);
        LeaderboardInputBoundary interactor = new LeaderboardInteractor(saveDataAccess, presenter);
        saveView.setLeaderboardController(new LeaderboardController(interactor));
        return this;
    }

    public SaveAppBuilder addLoadUseCase() {
        LoadOutputBoundary presenter = new LoadPresenter(saveViewModel);
        LoadInputBoundary interactor = new LoadInteractor(saveDataAccess, presenter);
        saveView.setLoadController(new LoadController(interactor));
        return this;
    }

    public JFrame build() {
        JFrame frame = new JFrame("D&D Dungeon Crawler - Save & Leaderboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(cardPanel);

        viewManagerModel.setState(saveViewModel.getViewName());
        viewManagerModel.notifyViewChanged();

        frame.pack();
        frame.setLocationRelativeTo(null);
        return frame;
    }
}