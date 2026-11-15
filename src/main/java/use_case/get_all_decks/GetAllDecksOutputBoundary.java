package use_case.get_all_decks;

/**
 * The output boundary for the GetAllDecks Use Case.
 */
public interface GetAllDecksOutputBoundary {
    /**
     * Prepares the success view for the GetAllDecks Use Case.
     * @param outputData the output data
     */
    void prepareSuccessView(GetAllDecksOutputData outputData);

    /**
     * Prepares the empty view for the GetAllDecks Use Case
     * when there are no available decks to display.
     * @param errorMessage the explanation of the failure
     */
    void prepareEmptyView(String errorMessage);

    /**
     * Switches to the Deck View.
     */
    void switchToDeckView();
}
