package interface_adapters.menu_bar;

import use_case.menu_bar.SwitchViewInputBoundary;

/**
 * Controller for the Switch View Use Case.
 */
public class SwitchViewController {

    private final SwitchViewInputBoundary interactor;

    public SwitchViewController(SwitchViewInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Executes the Switch View Use Case.
     * @param viewName the name of the view to switch to
     */
    public void execute(String viewName) {
        this.interactor.execute(viewName);
    }

}
