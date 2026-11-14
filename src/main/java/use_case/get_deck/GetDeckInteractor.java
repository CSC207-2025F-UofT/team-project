package use_case.get_deck;

import entity.Deck;
import entity.Flashcard;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * The GetDeck Interactor.
 */
public class GetDeckInteractor implements GetDeckInputBoundary{
    private final GetDeckDataAccessInterface deckDataAccessObject;
    private final GetDeckOutputBoundary getDeckPresenter;

    public GetDeckInteractor(GetDeckDataAccessInterface deckDataAccessObject,
                             GetDeckOutputBoundary getDeckPresenter) {
        this.deckDataAccessObject = deckDataAccessObject;
        this.getDeckPresenter = getDeckPresenter;
    }

    @Override
    public void execute(GetDeckInputData getDeckInputData) {
        String name = getDeckInputData.getName();
        boolean exists = deckDataAccessObject.deckExists(name);
        if (!exists) {
            getDeckPresenter.prepareFailView("Deck does not exist");
        } else {
            Deck deck = deckDataAccessObject.getDeck(name);

            ArrayList<Map<String, String>> flashcard_info = new ArrayList<>();
            ArrayList<Flashcard> flashcards = deck.getFlashcards();
            for (Flashcard flashcard: flashcards) {
                Map<String, String> map = new HashMap<>();
                map.put(flashcard.getQuestion(), flashcard.getAnswer());
                flashcard_info.add(map);
            }

            final GetDeckOutputData getDeckOutputData = new GetDeckOutputData(name, flashcard_info);
            getDeckPresenter.prepareSuccessView(getDeckOutputData);
        }
    }
}
