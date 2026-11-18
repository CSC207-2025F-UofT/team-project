package use_case.get_deck;

import entity.Deck;
import entity.Flashcard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        String viewName = getDeckInputData.getViewName();

        // if deck does not exist show error
        boolean exists = deckDataAccessObject.deckExists(name);
        if (!exists) {
            getDeckPresenter.prepareFailView("Deck does not exist");
            return;
        }

        Deck deck = deckDataAccessObject.getDeck(name);
        List<Flashcard> flashcards = deck.getFlashcards();

        // if going to study view with empty deck show error
        if (flashcards.isEmpty() && viewName.equals("studyView")) {
            getDeckPresenter.prepareFailView("There are no flashcards in this deck");
            return;
        }

        // populate GetDeckOutputData
        ArrayList<Map<String, String>> flashcardInfo = new ArrayList<>();
        for (Flashcard flashcard : flashcards) {
            Map<String, String> map = new HashMap<>();
            map.put(flashcard.getQuestion(), flashcard.getAnswer());
            flashcardInfo.add(map);
        }
        final GetDeckOutputData getDeckOutputData = new GetDeckOutputData(name,
                flashcardInfo, getDeckInputData.getViewName());

        if (flashcards.isEmpty() && viewName.equals("deckView")) {
            getDeckPresenter.prepareEmptyView(
                    getDeckOutputData,
                    "There are no flashcards in this deck");
        } else {
            getDeckPresenter.prepareSuccessView(getDeckOutputData);
        }
    }

    @Override
    public void switchToDeckView() {
        getDeckPresenter.switchToDeckView();
    }

    @Override
    public void switchToStudyView() {
        getDeckPresenter.switchToStudyView();
    }
}
