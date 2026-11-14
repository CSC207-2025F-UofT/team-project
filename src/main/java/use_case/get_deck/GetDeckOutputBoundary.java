package use_case.get_deck;

/**
 * The output boundary for the GetDeck Use Case.
 */
public interface GetDeckOutputBoundary {
    /**
     * Prepares the success view for the GetDeck Use Case.
     * @param outputData the output data
     */
    void prepareSuccessView(GetDeckOutputData outputData);

    /**
     * Prepares the failure view for the GetDeck Use Case
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);
}
