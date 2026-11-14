package use_case.get_deck;

/**
 * Input Boundary for the GetDeck Use Case.
 */
public interface GetDeckInputBoundary {

    /**
     * Executes the GetDeck use case.
     * @param getDeckInputData the input data
     */
    void execute(GetDeckInputData getDeckInputData);

    /**
     * Executes the switch to HomescreenView use case.
     */
    void switchToHomescreenView();
}
