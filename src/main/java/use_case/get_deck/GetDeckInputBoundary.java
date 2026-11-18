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
     * Executes the switchToDeckView use case.
     * Used for switching between deck and study view.
     */
    void switchToDeckView();

    /**
     * Executes the switchToStudyView use case.
     * Used for switching between deck and study view.
     */
    void switchToStudyView();
}
