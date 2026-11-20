package use_case.menu_bar;

public class SwitchViewInteractor implements SwitchViewInputBoundary {

    private final SwitchViewOutputBoundary presenter;

    public SwitchViewInteractor(SwitchViewOutputBoundary presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute(String viewName) {
        presenter.prepareSuccessView(viewName);
    }
}
