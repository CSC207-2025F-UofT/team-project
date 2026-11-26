package use_case.view_history;

/**
 * The entry point to the View History use case.
 * The controller calls this when the caregiver opens the history screen.
 */
public interface ViewHistoryInputBoundary {

    /**
     * Loads the child's communication history and sends it to the presenter.
     */
    void execute();
}