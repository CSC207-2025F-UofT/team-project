package use_case.settings;

public class SettingsInteractor implements SettingsInputBoundary {
    private final SettingsOutputBoundary userPresenter;

    public SettingsInteractor(SettingsOutputBoundary userPresenter) {
        this.userPresenter = userPresenter;
    }

    @Override
    public void goBack() {
        userPresenter.goBack();
    }
}
