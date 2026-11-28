package app;

import data_access.DataAccessObject;
import interface_adapter.ViewManagerModel;
import interface_adapter.game_running.*;
import interface_adapter.game_summary.GameSummaryViewModel;
import use_case.load_question.LoadQuestionInputBoundary;
import use_case.load_question.LoadQuestionInteractor;
import use_case.load_question.LoadQuestionOutputBoundary;
import use_case.log_input.LogInputInputBoundary;
import use_case.log_input.LogInputInteractor;
import use_case.question_input.QuestionInputInputBoundary;
import use_case.question_input.QuestionInputInteractor;
import use_case.question_input.QuestionInputOutputBoundary;
import interface_adapter.create_teams.CreateTeamViewModel;
import interface_adapter.create_teams.StartGameController;
import interface_adapter.create_teams.StartGamePresenter;
import interface_adapter.game_running.GameRunningViewModel;
import interface_adapter.game_running.LoadQuestionController;
import interface_adapter.game_running.LoadQuestionPresenter;
import use_case.load_question.LoadQuestionInputBoundary;
import use_case.load_question.LoadQuestionInteractor;
import use_case.load_question.LoadQuestionOutputBoundary;
import use_case.start_game.StartGameInputBoundary;
import use_case.start_game.StartGameInteractor;
import use_case.start_game.StartGameOutputBoundary;
import view.CreateTeamView;
import view.GameRunningView;
import view.GameSummaryView;
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
    private GameSummaryView gameSummaryView;
    private GameSummaryViewModel gameSummaryViewModel;
    private CreateTeamView createTeamView;
    private CreateTeamViewModel createTeamViewModel;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    public AppBuilder addGameRunningView() {
        gameRunningViewModel = new GameRunningViewModel();
        gameRunningView = new GameRunningView(gameRunningViewModel);
        cardPanel.add(gameRunningView, GameRunningViewModel.VIEW_NAME);
        return this;
    }

    public AppBuilder addGameSummaryView() {
        gameSummaryViewModel = new GameSummaryViewModel();
        gameSummaryView = new GameSummaryView(gameSummaryViewModel);
        cardPanel.add(gameSummaryView, GameSummaryViewModel.VIEW_NAME);
        return this;
    }

    public AppBuilder addCreateTeamView() {
        createTeamViewModel = new CreateTeamViewModel();
        createTeamView = new CreateTeamView(createTeamViewModel);
        cardPanel.add(createTeamView, CreateTeamViewModel.VIEW_NAME);
        return this;
    }

    public AppBuilder addLoadQuestionUseCase() {
        final LoadQuestionOutputBoundary loadQuestionOutputBoundary = new LoadQuestionPresenter(viewManagerModel, gameRunningViewModel);
        final LoadQuestionInputBoundary loadQuestionInteractor = new LoadQuestionInteractor(dataAccessObject, loadQuestionOutputBoundary);

        LoadQuestionController loadQuestionController = new LoadQuestionController(loadQuestionInteractor);
        gameRunningView.setLoadQuestionController(loadQuestionController);

        return this;
    }

    public AppBuilder addQuestionInputUseCase() {
        final QuestionInputOutputBoundary questionInputOutputBoundary = new QuestionInputPresenter(viewManagerModel, gameRunningViewModel);
        final QuestionInputInputBoundary questionInputInteractor = new QuestionInputInteractor(dataAccessObject, questionInputOutputBoundary);

        QuestionInputController questionInputController = new QuestionInputController(questionInputInteractor);
        gameRunningView.setQuestionInputController(questionInputController);
      
        return this;
    }
  
    public AppBuilder addStartGameUseCase() {
        final StartGameOutputBoundary startGameOutputBoundary = new StartGamePresenter(viewManagerModel, createTeamViewModel, gameRunningViewModel);
        final StartGameInputBoundary startGameInteractor = new StartGameInteractor(dataAccessObject, startGameOutputBoundary);

        StartGameController startGameController = new StartGameController(startGameInteractor);
        createTeamView.setStartGameController(startGameController);

        return this;
    }

    public JFrame build() {
        final JFrame application = new JFrame("Family Feud");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        application.setSize(1000, 600);
        application.setResizable(false);

        application.add(cardPanel);

        viewManagerModel.setState(CreateTeamViewModel.VIEW_NAME);
        viewManagerModel.notifyViewChanged();

        return application;
    }

}
