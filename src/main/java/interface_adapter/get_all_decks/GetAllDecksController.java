package interface_adapter.get_all_decks;

import use_case.get_all_decks.GetAllDecksInputBoundary;
import use_case.get_all_decks.GetAllDecksInputData;

/**
 * The controller for the GetAllDecks Use Case.
 */
public class GetAllDecksController {

    private final GetAllDecksInputBoundary getAllDecksUseCaseInteractor;

    public GetAllDecksController(GetAllDecksInputBoundary getAllDecksUseCaseInteractor) {
        this.getAllDecksUseCaseInteractor = getAllDecksUseCaseInteractor;
    }

    /**
     * Executes the GetAllDecks Use Case.
     */
    public void execute() {
        final GetAllDecksInputData getAllDecksInputData = new GetAllDecksInputData();

        getAllDecksUseCaseInteractor.execute(getAllDecksInputData);
    }

    /**
     * Executes the "switch to DeckView" Use Case.
     */
    public void switchToDeckView() {
        getAllDecksUseCaseInteractor.switchToDeckView();
    }
}

