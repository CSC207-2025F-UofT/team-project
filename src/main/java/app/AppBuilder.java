package app;

import data_access.MockDeckDataAccessObject;
import entity.DeckFactory;
import interface_adapter.ViewManagerModel;
import interface_adapter.get_all_decks.GetAllDecksController;
import interface_adapter.get_all_decks.GetAllDecksPresenter;
import interface_adapter.get_all_decks.GetAllDecksViewModel;
import interface_adapter.get_deck.GetDeckController;
import interface_adapter.get_deck.GetDeckPresenter;
import interface_adapter.get_deck.deck_view.GetDeckViewModel;
import interface_adapter.get_deck.study_view.StudyViewModel;
import use_case.get_all_decks.GetAllDecksInputBoundary;
import use_case.get_all_decks.GetAllDecksInteractor;
import use_case.get_all_decks.GetAllDecksOutputBoundary;
import use_case.get_deck.GetDeckInputBoundary;
import use_case.get_deck.GetDeckInteractor;
import use_case.get_deck.GetDeckOutputBoundary;
import view.*;
import view.deck_view.DeckView;
import view.deck_view.StudyView;

import javax.swing.*;
import java.awt.*;

public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    final DeckFactory deckFactory = new DeckFactory();
    final ViewManagerModel viewManagerModel = new ViewManagerModel();

    // initializing this adds it as a listener to viewManagerModel so this line is needed
    ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

    // set which data access implementation to use, can be any
    // of the classes from the data_access package

    // DAO version using local file storage
    final MockDeckDataAccessObject deckDataAccessObject = new MockDeckDataAccessObject(deckFactory);

    private HomescreenView homescreenView;
    private GetAllDecksViewModel getAllDecksViewModel;

    private DeckView deckView;
    private GetDeckViewModel getDeckViewModel;

    private StudyView studyView;
    private StudyViewModel studyViewModel;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    public AppBuilder addHomescreenView() {
        getAllDecksViewModel = new GetAllDecksViewModel();
        homescreenView = new HomescreenView(getAllDecksViewModel);
        cardPanel.add(homescreenView, GetAllDecksViewModel.VIEW_NAME);
        return this;
    }

    public AppBuilder addDeckView() {
        getDeckViewModel = new GetDeckViewModel();
        deckView = new DeckView(getDeckViewModel);
        cardPanel.add(deckView, GetDeckViewModel.VIEW_NAME);
        return this;
    }

    public AppBuilder addStudyView() {
        studyViewModel = new StudyViewModel();
        studyView = new StudyView(studyViewModel);
        cardPanel.add(studyView, StudyViewModel.VIEW_NAME);
        return this;
    }

    public AppBuilder addGetAllDecksUseCase() {
        final GetAllDecksOutputBoundary getAllDecksOutputBoundary = new GetAllDecksPresenter(viewManagerModel,
                getAllDecksViewModel);
        final GetAllDecksInputBoundary getAllDecksInteractor = new GetAllDecksInteractor(
                deckDataAccessObject, getAllDecksOutputBoundary);

        GetAllDecksController controller = new GetAllDecksController(getAllDecksInteractor);
        homescreenView.setGetAllDecksController(controller);
        deckView.setGetAllDecksController(controller);
        studyView.setGetAllDecksController(controller);
        return this;
    }

    public AppBuilder addGetDeckUseCase() {
        final GetDeckOutputBoundary getDeckOutputBoundary = new GetDeckPresenter(viewManagerModel,
                getAllDecksViewModel, getDeckViewModel, studyViewModel);

        final GetDeckInputBoundary getDeckInteractor = new GetDeckInteractor(
                deckDataAccessObject, getDeckOutputBoundary);

        GetDeckController controller = new GetDeckController(getDeckInteractor);
        deckView.setGetDeckController(controller);
        studyView.setGetDeckController(controller);
        homescreenView.setGetDeckController(controller);
        return this;
    }

    public JFrame build() {
        final JFrame application = new JFrame("Flashcards");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(cardPanel);

        viewManagerModel.setState(GetAllDecksViewModel.VIEW_NAME);
        viewManagerModel.notifyViewChanged();

        // initial execution of GetAllDecksUseCase when program starts
        homescreenView.initialExecuteGetAllDecks();

        return application;
    }

    // makes java swing look more modern, has gui match the operating system
    public void setGUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) { /* if not supported then ignore */}
    }
}
