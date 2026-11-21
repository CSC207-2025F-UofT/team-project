package app;

import data_access.InMemoryDeckRepository;
import entity.Card;
import entity.Deck;

import interface_adapter.edit_card.EditCardController;
import interface_adapter.edit_card.EditCardPresenter;
import interface_adapter.edit_card.EditCardViewModel;

import interface_adapter.add_card.AddCardController;
import interface_adapter.add_card.AddCardPresenter;
import interface_adapter.add_card.AddCardViewModel;

import use_case.add_card.AddCardInputBoundary;
import use_case.add_card.AddCardDataAccessInterface;
import use_case.add_card.AddCardInteractor;
import use_case.edit_card.EditCardInteractor;

import view.EditCardView;

import java.util.List;

public class AppBuilder {
    private final InMemoryDeckRepository repository;

    private final AddCardViewModel addCardViewModel;
    private final AddCardController addCardController;

    public AppBuilder() {
        // Temporary demo data, not a signed-in user.
        Deck demoDeck = new Deck(1, "Java test1", "Kobi owner");

        demoDeck.add_card(new Card(
                1,
                "What does JVM stand for?",
                "Java Virtual Machine"
        ));

        repository = new InMemoryDeckRepository(
                List.of(demoDeck)
        );

        addCardViewModel = new AddCardViewModel();

        AddCardPresenter addPresenter =
                new AddCardPresenter(addCardViewModel);

        AddCardInteractor addInteractor =
                new AddCardInteractor(repository, addPresenter);

        addCardController = new AddCardController(addInteractor);
    }

    public AddCardController getAddCardController() {
        return addCardController;
    }

    public AddCardViewModel getAddCardViewModel() {
        return addCardViewModel;
    }

    public EditCardView buildEditCardView(int deckId, int cardId) {
        Card selectedCard = repository.findCard(deckId, cardId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Selected card does not exist."
                ));

        EditCardViewModel editViewModel = new EditCardViewModel();

        EditCardPresenter editPresenter =
                new EditCardPresenter(editViewModel);

        EditCardInteractor editInteractor =
                new EditCardInteractor(
                        repository,
                        editPresenter
                );

        EditCardController editController =
                new EditCardController(editInteractor);

        return new EditCardView(
                editController,
                editViewModel,
                deckId,
                cardId,
                selectedCard.getQuestion(),
                selectedCard.getAnswer()
        );
    }
}
