package interface_adapter.view_history;

import use_case.view_history.ViewHistoryInputBoundary;

/**
 * Called by the history screen when the caregiver opens it.
 */
public class ViewHistoryController {

    private final ViewHistoryInputBoundary interactor;

    public ViewHistoryController(ViewHistoryInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Loads the history so the screen can display it.
     */
    public void execute() {
        interactor.execute();
    }
}