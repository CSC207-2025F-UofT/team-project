package interface_adapter.get_deck;

import use_case.get_deck.GetDeckInputBoundary;
import use_case.get_deck.GetDeckInputData;

/**
 * Controller for the GetDeck Use Case.
 */
public class GetDeckController {

    private final GetDeckInputBoundary getDeckUseCaseInteractor;

    public GetDeckController(GetDeckInputBoundary getDeckUseCaseInteractor) {
        this.getDeckUseCaseInteractor = getDeckUseCaseInteractor;
    }

    /**
     * Executes the GetDeck Use Case.
     * @param deckName the name of the selected deck
     */
    public void execute(String deckName, String viewName) {
        final GetDeckInputData getDeckInputData = new GetDeckInputData(deckName, viewName);

        getDeckUseCaseInteractor.execute(getDeckInputData);
    }
}
