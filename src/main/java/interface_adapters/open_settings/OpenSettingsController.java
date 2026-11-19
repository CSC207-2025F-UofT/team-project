package interface_adapters.open_settings;

import use_case.open_settings.OpenSettingsInputBoundary;

/**
 * Controller for the Open Settings Use Case.
 */
public class OpenSettingsController {

    private OpenSettingsInputBoundary interactor;

    public OpenSettingsController(OpenSettingsInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Executes the Open Timer Settings Use Case.
     */
    public void execute() {
        interactor.execute();
    }
}
