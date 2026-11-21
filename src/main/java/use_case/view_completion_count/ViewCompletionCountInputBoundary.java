package use_case.view_completion_count;
/**
 * Input boundary for the ViewCompletionCount use case.
 * Implemented by ViewCompletionCountInteractor.
 */
public interface ViewCompletionCountInputBoundary {
    /**
     * Executes the ViewCompletionCount use case.
     * @param in the input data containing username and boardId
     */
    void execute(ViewCompletionCountInputData in);
}