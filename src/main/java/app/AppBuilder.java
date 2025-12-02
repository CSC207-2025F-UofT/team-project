package app;

import API.GeoapifyStaticMap;
import API.MoveStaticMapInterface;
import data_access.FileGameDataAccessObject;
import data_access.QuizzesReader;
import entity.Inventory;
import interface_adapter.Battle.BattleController;
import interface_adapter.Battle.BattlePresenter;
import interface_adapter.Battle.BattleViewModel;
import interface_adapter.InventoryAddItem.InventoryAddItemController;
import interface_adapter.InventoryAddItem.InventoryAddItemPresenter;
import interface_adapter.InventoryAddItem.InventoryAddItemViewModel;
import interface_adapter.InventoryUseItem.InventoryUseItemController;
import interface_adapter.InventoryUseItem.InventoryUseItemPresenter;
import interface_adapter.InventoryUseItem.InventoryUseItemViewModel;
import interface_adapter.ViewManagerModel;
import interface_adapter.move.MoveController;
import interface_adapter.move.MovePresenter;
import interface_adapter.move.MoveViewModel;
import interface_adapter.opengame.OpenGameController;
import interface_adapter.opengame.OpenGamePresenter;
import interface_adapter.opengame.OpenGameViewModel;
import interface_adapter.quiz.*;
import interface_adapter.results.ResultsViewModel;
import interface_adapter.results.ShowResultsController;
import interface_adapter.results.ShowResultsPresenter;
import use_case.Battle.BattleInputBoundary;
import use_case.Battle.BattleInteractor;
import use_case.Battle.BattleOutputBoundary;
import use_case.InventoryAddItem.InventoryAddItemInputBoundary;
import use_case.InventoryAddItem.InventoryAddItemInteractor;
import use_case.InventoryAddItem.InventoryAddItemOutputBoundary;
import use_case.InventoryUseItem.InventoryUseItemInputBoundary;
import use_case.InventoryUseItem.InventoryUseItemInteractor;
import use_case.InventoryUseItem.InventoryUseItemOutputBoundary;
import use_case.loadQuiz.LoadQuizInputBoundary;
import use_case.loadQuiz.LoadQuizInteractor;
import use_case.loadQuiz.LoadQuizOutputBoundary;
import use_case.move.MoveInputBoundary;
import use_case.move.MoveInteractor;
import use_case.move.MoveOutputBoundary;
import use_case.openGame.*;
import use_case.submitQuiz.SubmitQuizInputBoundary;
import use_case.submitQuiz.SubmitQuizInteractor;
import use_case.submitQuiz.SubmitQuizOutputBoundary;
import use_case.show_results.ShowResultsInputBoundary;
import use_case.show_results.ShowResultsInteractor;
import use_case.show_results.ShowResultsOutputBoundary;
import view.*;

import javax.swing.*;
import java.awt.*;

public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel views = new JPanel(cardLayout);
    final ViewManagerModel viewManagerModel = new ViewManagerModel();
    ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

    // set which data access implementation to use, can be any
    // of the classes from the data_access package

    // DAO version using local file storage
    private final FileGameDataAccessObject gameDataAccess = new FileGameDataAccessObject();

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
    private ItemView itemView;
    private InventoryAddItemViewModel inventoryAddItemViewModel;
    private InventoryView inventoryView;
    private InventoryUseItemViewModel inventoryUseItemViewModel;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    public AppBuilder addBattleView() {
        battleViewModel = new BattleViewModel();
        battleView = new BattleView(battleViewModel, quizViewModel);
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
        openGameView = new OpenGameView(openGameViewModel);
        cardPanel.add(openGameView, openGameView.getViewName());
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

    public AppBuilder addAddInventoryView() {
        inventoryAddItemViewModel = new InventoryAddItemViewModel();
        itemView = new ItemView(inventoryAddItemViewModel);
        cardPanel.add(itemView, itemView.getViewName());
        return this;
    }

    public AppBuilder addUseInventoryView() {
        inventoryUseItemViewModel = new InventoryUseItemViewModel();
        inventoryView = new InventoryView(inventoryUseItemViewModel);
        cardPanel.add(inventoryView, inventoryView.getViewName());
        return this;
    }

    public AppBuilder addBattleUseCase() {
        final BattleOutputBoundary battleOutputBoundary = new BattlePresenter(battleViewModel, moveViewModel, viewManagerModel);
        final BattleInputBoundary battleInteractor = new BattleInteractor(
                gameDataAccess, battleOutputBoundary);

        BattleController controller = new BattleController(battleInteractor, quizViewModel);
        battleView.setBattleController(controller);
        return this;
    }

    public AppBuilder addMoveUseCase() {
        MoveStaticMapInterface mapService = new GeoapifyStaticMap();
        final MoveOutputBoundary moveOutputBoundary = new MovePresenter(viewManagerModel, moveViewModel,
                mapService, battleViewModel);
        final MoveInputBoundary moveInteractor = new MoveInteractor(
                gameDataAccess, moveOutputBoundary);

        MoveController controller = new MoveController(moveInteractor);
        moveView.setMoveController(controller);
        return this;
    }

    public AppBuilder addOpenGameUseCase() {
        final OpenGameOutputBoundary openGameOutputBoundary = new OpenGamePresenter(
                openGameViewModel, viewManagerModel);
        final OpenGameInputBoundary openGameInteractor = new OpenGameInteractor(openGameOutputBoundary, gameDataAccess);

        OpenGameController controller = new OpenGameController(openGameInteractor);
        openGameView.setOpenGameController(controller);
        return this;
    }

    public AppBuilder addQuizUseCase() {
        final LoadQuizOutputBoundary loadQuizOutputBoundary = new LoadQuizPresenter(quizViewModel);
        final LoadQuizInputBoundary loadQuizInteractor = new LoadQuizInteractor(
                gameDataAccess, loadQuizOutputBoundary);
        final SubmitQuizOutputBoundary submitQuizOutputBoundary = new SubmitQuizPresenter(
                quizViewModel, battleViewModel, viewManagerModel);
        final SubmitQuizInputBoundary submitQuizInteractor = new SubmitQuizInteractor(
                gameDataAccess, submitQuizOutputBoundary);

        QuizController controller = new QuizController(submitQuizInteractor, loadQuizInteractor);
        quizView.setQuizController(controller);
        new QuizzesReader().loadQuizzes(gameDataAccess);
        QuizState quizState = new QuizState();
        quizView.loadQuiz(quizState.setQuizId());
        return this;
    }

    public AppBuilder addResultsUseCase() {
        final ShowResultsOutputBoundary showResultsOutputBoundary = new ShowResultsPresenter(resultsViewModel, moveViewModel, viewManagerModel);
        final ShowResultsInputBoundary showResultsInteractor = new ShowResultsInteractor(
                gameDataAccess, showResultsOutputBoundary);

        ShowResultsController controller = new ShowResultsController(showResultsInteractor);
        moveView.setResultController(controller);
        resultsView.setResultController(controller);
        return this;
    }

    public AppBuilder addAddInventoryUseCase() {
        final InventoryAddItemOutputBoundary inventoryAddItemOutputBoundary = new InventoryAddItemPresenter(
                inventoryAddItemViewModel);
        final InventoryAddItemInputBoundary inventoryAddItemInteractor = new InventoryAddItemInteractor(inventoryAddItemOutputBoundary);

        InventoryAddItemController controller = new InventoryAddItemController(inventoryAddItemInteractor);
        itemView.setController(controller);
        return this;
    }
    public AppBuilder addUseInventoryUseCase() {
        final InventoryUseItemOutputBoundary inventoryUseItemPresenter = new InventoryUseItemPresenter();
        final InventoryUseItemInputBoundary inventoryUseItemInteractor = new InventoryUseItemInteractor(
                inventoryUseItemPresenter);

        // InventoryUseItemController controller = new InventoryUseItemController();
        // inventoryView.setController(controller);
        return this;
    }

    public JFrame build() {
        final JFrame application = new JFrame("Adventure & Battle Integrated System");
        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        application.setSize(1024, 768);
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(cardPanel);

        viewManagerModel.setState(openGameView.getViewName());
        viewManagerModel.firePropertyChange();


        return application;
    }

}
