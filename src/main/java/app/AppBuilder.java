package app;

import interface_adapter.Battle.BattlePresenter;
import interface_adapter.Battle.BattleViewModel;
import interface_adapter.ViewManagerModel;
import interface_adapter.move.MoveViewModel;
import interface_adapter.opengame.OpenGameViewModel;
import interface_adapter.quiz.QuizViewModel;
import interface_adapter.results.ResultsViewModel;
import use_case.Battle.BattleOutputBoundary;
import view.*;

import javax.swing.*;
import java.awt.*;

public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    final ViewManagerModel viewManagerModel = new ViewManagerModel();
    ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

    // set which data access implementation to use, can be any
    // of the classes from the data_access package

    // DAO version using local file storage
    // TODO: Initialize the DAO
    //final FileGameDataAccessObject

    // DAO version using a shared external database
    // final DBUserDataAccessObject userDataAccessObject = new DBUserDataAccessObject(userFactory);

    private BattleView battleView;
    private BattleViewModel battleViewModel;
    private MoveView moveView;
    private MoveViewModel moveViewModel;
    private OpenGameView openGameView;
    private OpenGameViewModel openGameViewModel;
    private QuizView quizView;
    private QuizViewModel quizViewModel;
    private ResultsView resultsView;
    private ResultsViewModel resultsViewModel;
    private ResultScreenView resultScreenView;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    public AppBuilder addBattleView() {
        battleViewModel = new BattleViewModel();
        battleView = new BattleView(battleViewModel);
        cardPanel.add(battleView, battleView.getViewName());
        return this;
    }

    public AppBuilder addMoveView() {
        moveViewModel = new MoveViewModel();
        moveView = new MoveView(moveViewModel);
        cardPanel.add(moveView, moveView.getViewName());
        return this;
    }

    public AppBuilder addOpenGameView() {
        openGameViewModel = new OpenGameViewModel();
        // openGameView = new OpenGameView(openGameViewModel);
        // cardPanel.add(quizView, quizView.getViewName());
        return this;
    }

    public AppBuilder addQuizView() {
        quizViewModel = new QuizViewModel();
        quizView = new QuizView(quizViewModel);
        cardPanel.add(quizView, quizView.getViewName());
        return this;
    }

    public AppBuilder addResultsView() {
        resultsViewModel = new ResultsViewModel();
        resultsView = new ResultsView(resultsViewModel);
        cardPanel.add(resultsView, resultsView.getViewName());
        return this;
    }

    public AppBuilder addResultScreenView() {
        resultsViewModel = new ResultsViewModel();
        // resultScreenView = new ResultScreenView(resultsViewModel);
        // cardPanel.add(resultScreenView, resultScreenView.getViewName());
        return this;
    }

    public AppBuilder addBattleUseCase() {
        final BattleOutputBoundary signupOutputBoundary = new BattlePresenter(battleViewModel, viewManagerModel);
        //final Battle_InputBoundary battleInteractor = new Battle_Interactor(
        //        userDataAccessObject, signupOutputBoundary);

        //Battle_Controller controller = new Battle_Controller(battleInteractor);
        // battleView.setBattleController(controller);
        return this;
    }

    public AppBuilder addMoveUseCase() {
        return this;
    }

    public AppBuilder addOpenGameUseCase() {
        return this;
    }

    public AppBuilder addQuizUseCase() {
        return this;
    }

    public AppBuilder addResultsUseCase() {
        return this;
    }

    public AppBuilder addResultScreenUseCase() {
        return this;
    }

    public JFrame build() {
        final JFrame application = new JFrame("Java Adventure Game");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(cardPanel);

        // TODO: add view name
        // viewManagerModel.setState(openGameView.getViewName());
        viewManagerModel.firePropertyChange();

        return application;
    }

}
