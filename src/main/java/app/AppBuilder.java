package app;

import data_access.MockDeckDataAccessObject;
import entity.DeckFactory;
import interface_adapter.ViewManagerModel;
import interface_adapter.get_all_decks.GetAllDecksController;
import interface_adapter.get_all_decks.GetAllDecksPresenter;
import interface_adapter.get_all_decks.GetAllDecksViewModel;
import use_case.get_all_decks.GetAllDecksInputBoundary;
import use_case.get_all_decks.GetAllDecksInteractor;
import use_case.get_all_decks.GetAllDecksOutputBoundary;
import view.HomescreenView;
import view.ViewManager;

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

    private HomescreenView getAllDecksView;
    private GetAllDecksViewModel getAllDecksViewModel;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    public AppBuilder addGetAllDecksView() {
        getAllDecksViewModel = new GetAllDecksViewModel();
        getAllDecksView = new HomescreenView(getAllDecksViewModel);
        cardPanel.add(getAllDecksView, getAllDecksViewModel.VIEW_NAME);
        return this;
    }

    public AppBuilder addGetAllDecksUseCase() {
        final GetAllDecksOutputBoundary getAllDecksOutputBoundary = new GetAllDecksPresenter(viewManagerModel,
                getAllDecksViewModel);
        final GetAllDecksInputBoundary getAllDecksInteractor = new GetAllDecksInteractor(
                deckDataAccessObject, getAllDecksOutputBoundary);

        GetAllDecksController controller = new GetAllDecksController(getAllDecksInteractor);
        getAllDecksView.setGetAllDecksController(controller);
        return this;
    }

    public JFrame build() {
        final JFrame application = new JFrame("Flashcards");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(cardPanel);

        viewManagerModel.setState(GetAllDecksViewModel.VIEW_NAME);
        viewManagerModel.notifyViewChanged();

        // initial execution of GetAllDecksUseCase when program starts
        getAllDecksView.initialExecuteGetAllDecks();

        return application;
    }

    // makes java swing look more modern, has gui match the operating system
    public void setGUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}
    }
}
