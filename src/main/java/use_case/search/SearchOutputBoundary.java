package use_case.search;

/**
 * The output boundary for the Search Use Case.
 */
public interface SearchOutputBoundary {
    /**
     * Prepares the success view for the Search Use Case.
     * @param response for the output data
     */
    void prepareSuccessView(String response);
    /**
     * Prepares the failure view for the Search Use Case.
     * @param error the explanation of the failure
     */
    void prepareFailView(String error);
}
