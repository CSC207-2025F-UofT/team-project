package app;

import data_access.DataAccessObject;
import interface_adapter.ViewManagerModel;
import interface_adapter.game_running.GameRunningViewModel;
import interface_adapter.game_running.LoadQuestionController;
import interface_adapter.game_running.LoadQuestionPresenter;
import use_case.load_question.LoadQuestionInputBoundary;
import use_case.load_question.LoadQuestionInteractor;
import use_case.load_question.LoadQuestionOutputBoundary;
import view.GameRunningView;
import view.ViewManager;

import javax.swing.*;
import java.awt.*;

public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    final ViewManagerModel viewManagerModel = new ViewManagerModel();
    ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

    final DataAccessObject dataAccessObject = new DataAccessObject();

    // add your stuff here :)
    private GameRunningView gameRunningView;
    private GameRunningViewModel gameRunningViewModel;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    // add your view here
    public AppBuilder addGameRunningView() {
        gameRunningViewModel = new GameRunningViewModel();
        gameRunningView = new GameRunningView(gameRunningViewModel);
        cardPanel.add(gameRunningView, GameRunningViewModel.VIEW_NAME);
        return this;
    }

    // ad your use case here >:)
    public AppBuilder addLoadQuestionUseCase() {
        final LoadQuestionOutputBoundary loadQuestionOutputBoundary = new LoadQuestionPresenter(viewManagerModel, gameRunningViewModel);
        final LoadQuestionInputBoundary loadQuestionInteractor = new LoadQuestionInteractor(dataAccessObject, loadQuestionOutputBoundary);

        LoadQuestionController loadQuestionController = new LoadQuestionController(loadQuestionInteractor);
        gameRunningView.setLoadQuestionController(loadQuestionController);

        return this;
    }

    public JFrame build() {
        final JFrame application = new JFrame("Family Feud");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        application.setSize(1000, 600);
        application.setResizable(false);

        application.add(cardPanel);

        viewManagerModel.setState(GameRunningViewModel.VIEW_NAME);
        viewManagerModel.notifyViewChanged();

        return application;
    }

}
