package interface_adapter.rest;

import use_case.rest.RestInputBoundary;

/**
 * Controller for resting at the residence.
 */
public class RestController {
    private final RestInputBoundary interactor;

    public RestController(RestInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute() {
        interactor.execute();
    }

    public void retrySave() {
        interactor.retrySave();
    }
}
