package interface_adapters.cancel_timer;

import use_case.cancel_timer.CancelTimerInputBoundary;

/**
 * Controller for the Cancel Timer Use Case.
 */
public class CancelTimerController {
    private final CancelTimerInputBoundary interactor;

    public CancelTimerController(CancelTimerInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Executes the Cancel Timer Settings Use Case.
     */
    public void execute() {
        interactor.execute();
    }
}
