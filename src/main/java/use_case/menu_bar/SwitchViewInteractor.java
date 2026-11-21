package use_case.menu_bar;

/**
 * The Switch View Interactor.
 */
public class SwitchViewInteractor implements SwitchViewInputBoundary {

    private final SwitchViewOutputBoundary presenter;

    public SwitchViewInteractor(SwitchViewOutputBoundary presenter) {
        this.presenter = presenter;
    }

    /**
     * Execute the Switch View Use Case when the menu bar is pressed.
     */
    @Override
    public void execute(String viewName) {
        presenter.prepareSuccessView(viewName);
    }
}
