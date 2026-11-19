package use_case.open_settings;

/**
 * The Open Settings Interactor.
 */
public class OpenSettingsInteractor implements OpenSettingsInputBoundary {
    private OpenSettingsOutputBoundary presenter;

    public OpenSettingsInteractor(OpenSettingsOutputBoundary presenter) {
        this.presenter = presenter;
    }

    /**
     * Executes the Open Settings Use Case.
     */
    @Override
    public void execute() {
        presenter.prepareSuccessView();
    }
}
