package use_case.get_all_decks;

import entity.Deck;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The GetAllDecks Interactor.
 */
public class GetAllDecksInteractor implements GetAllDecksInputBoundary {
    private final GetAllDecksDataAccessInterface deckDataAccessObject;
    private final GetAllDecksOutputBoundary getAllDecksPresenter;

    public GetAllDecksInteractor(GetAllDecksDataAccessInterface deckDataAccessInterface,
                                 GetAllDecksOutputBoundary getAllDecksOutputBoundary) {
        this.deckDataAccessObject = deckDataAccessInterface;
        this.getAllDecksPresenter = getAllDecksOutputBoundary;
    }

    @Override
    public void execute(GetAllDecksInputData getAllDecksInputData) {
        if (deckDataAccessObject.isEmpty()) {
            getAllDecksPresenter.prepareEmptyView("No Flashcard Decks have been added");
        }
        else {
            final List<Deck> decks = deckDataAccessObject.getAllDecks();
            final Map<String, Integer> decksWithCount = new HashMap<>();

            for (Deck deck: decks) {
                decksWithCount.put(deck.getName(), deck.getFlashcardCount());
            }

            final GetAllDecksOutputData getAllDecksOutputData =
                    new GetAllDecksOutputData(decksWithCount);
            getAllDecksPresenter.prepareSuccessView(getAllDecksOutputData);
        }
    }

    @Override
    public void switchToDeckView() { getAllDecksPresenter.switchToDeckView(); }
}

