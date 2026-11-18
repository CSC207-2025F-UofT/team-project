package interface_adapter.get_all_decks;

import use_case.get_all_decks.GetAllDecksInputBoundary;

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
        getAllDecksUseCaseInteractor.execute();
    }
}
