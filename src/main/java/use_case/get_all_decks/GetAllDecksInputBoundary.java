package use_case.get_all_decks;

/**
 * Input Boundary for actions which are related to GetALlDecks.
 */
public interface GetAllDecksInputBoundary {

    /**
     * Executes the GetAllDecks use case.
     * @param getAllDecksInputData the input data
     */
    void execute(GetAllDecksInputData getAllDecksInputData);

    /**
     * Executes the switch to deck view use case.
     */
    void switchToDeckView();
}
