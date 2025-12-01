package app;

import API.GeoapifyStaticMap;
import API.MoveStaticMapInterface;
import data_access.FileGameDataAccessObject;
import data_access.InMemoryBattleDataAccess;
import entity.*;
import interface_adapter.Battle.Battle_Controller;
import interface_adapter.Battle.Battle_Presenter;
import interface_adapter.Battle.Battle_State;
import interface_adapter.Battle.Battle_ViewModel;
import interface_adapter.ViewManagerModel;
import interface_adapter.move.MoveController;
import interface_adapter.move.MovePresenter;
import interface_adapter.move.MoveViewModel;
import interface_adapter.quiz.Quiz_ViewModel;
import interface_adapter.results.ResultsViewModel;
import interface_adapter.results.ShowResultsController;
import interface_adapter.results.ShowResultsPresenter;
import use_case.Battle.Battle_Interactor;
import use_case.move.MoveInputBoundary;
import use_case.move.MoveInteractor;
import use_case.move.MoveOutputData;
import use_case.show_results.ShowResultsInputBoundary;
import use_case.show_results.ShowResultsInteractor;
import view.Battle_View;
import view.MoveView;
import view.Quiz_View;
import view.ResultsView;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class MoveTestApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
        });
    }

    private static void createAndShowGUI() {
        JFrame application = new JFrame("Adventure & Battle Integrated System");
        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        application.setSize(1024, 768);

        CardLayout cardLayout = new CardLayout();
        JPanel views = new JPanel(cardLayout);

        ViewManagerModel viewManagerModel = new ViewManagerModel();

        Battle_ViewModel battleViewModel = new Battle_ViewModel();
        Quiz_ViewModel quizViewModel = new Quiz_ViewModel();

        InMemoryBattleDataAccess battleDataAccess = new InMemoryBattleDataAccess();

        Battle_Presenter battlePresenter = new Battle_Presenter(battleViewModel, viewManagerModel);

        Battle_Interactor battleInteractor = new Battle_Interactor(battleDataAccess, battlePresenter);

        Battle_Controller battleController = new Battle_Controller(battleInteractor, quizViewModel);

        Battle_View battleView = new Battle_View(battleViewModel);
        battleView.setBattleController(battleController);

        Quiz_View quizView = new Quiz_View(quizViewModel, viewManagerModel);
        quizView.setBattleController(battleController);

        MoveViewModel moveViewModel = new MoveViewModel();
        ResultsViewModel resultsViewModel = new ResultsViewModel();

        FileGameDataAccessObject gameDataAccess = new FileGameDataAccessObject();
        System.out.println(gameDataAccess.getGame().getUser().getHP());
        MoveStaticMapInterface mapService = new GeoapifyStaticMap();

        MovePresenter movePresenter = new MovePresenter(viewManagerModel, moveViewModel, mapService, battleViewModel);

        MoveInputBoundary moveInteractor = new MoveInteractor(gameDataAccess, movePresenter);
        MoveController moveController = new MoveController(moveInteractor);

        MoveView moveView = new MoveView(moveViewModel);
        moveView.setMoveController(moveController);

        ShowResultsPresenter resultsPresenter = new ShowResultsPresenter(resultsViewModel);
        ShowResultsInputBoundary resultsInteractor = new ShowResultsInteractor(gameDataAccess, resultsPresenter);
        ShowResultsController resultsController = new ShowResultsController(resultsInteractor);
        ResultsView resultsView = new ResultsView(resultsViewModel);


        views.add(moveView, moveView.getViewName());
        views.add(resultsView, resultsView.getViewName());
        views.add(battleView, "Battle");
        views.add(quizView, "Quiz");


        viewManagerModel.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                System.out.println(evt.getPropertyName());
                if ("state".equals(evt.getPropertyName())) {
                    String viewName = (String) evt.getNewValue();
                    System.out.println("Switching view to: " + viewName);
                    cardLayout.show(views, viewName);


                    if ("Battle".equals(viewName)) {

                    }
                }
            }
        });


        moveView.getEndGameButton().addActionListener(e -> {
            resultsController.execute();
            viewManagerModel.setState(resultsViewModel.getViewName());
            viewManagerModel.firePropertyChange();
        });

        resultsView.getBackButton().addActionListener(e -> {
            System.out.println("Restarting game...");
            application.dispose();
            createAndShowGUI();
        });


        try {
            AdventureGame initialGame = gameDataAccess.getGame();
            boolean canMoveLeft = initialGame.canMove(Direction.LEFT);
            boolean canMoveRight = initialGame.canMove(Direction.RIGHT);
            Location loc = initialGame.getGameMap().getCurrentLocation();
            User user = initialGame.getUser();
            Monster mon = loc.getMonster();
            int index = initialGame.getGameMap().getCurrentLocationIndex();
            int size = initialGame.getGameMap().getMapSize();

            MoveOutputData initialOutputData = new MoveOutputData(
                    loc.getName(), loc.getLatitude(), loc.getLongitude(),
                    index, size, canMoveLeft, canMoveRight, mon
            );
            movePresenter.present(initialOutputData);


            Battle_State battleState = battleViewModel.getState();
            battleState.setUser(user);
            battleState.setMonster(mon != null ? mon : new Monster());
            battleState.setBattleMessage("Battle System initialized.");
            battleViewModel.setState(battleState);
            battleViewModel.firePropertyChange();

        } catch (Exception e) {
            System.err.println("Error during initial game load!");
            e.printStackTrace();
        }


        application.add(views);
        application.setLocationRelativeTo(null);
        application.setVisible(true);
    }
}