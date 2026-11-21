package use_case.view_completion_count;
/**
 * Output boundary for the ViewCompletionCount use case.
 * Implemented by ViewCompletionCountPresenter.
 */
public interface ViewCompletionCountOutputBoundary {
    /**
     * Called when the completion count was retrieved successfully.
     * @param out the output data containing completed and total task counts
     */
    void prepareSuccessView(ViewCompletionCountOutputData out);
    /**
     * Called when retrieval fails (e.g. board not found).
     * @param error a message describing what went wrong
     */
    void prepareFailView(String error);

}