package interface_adapters.menu_bar;

import use_case.menu_bar.SwitchViewInputBoundary;

public class SwitchViewController {

    private final SwitchViewInputBoundary interactor;

    public SwitchViewController(SwitchViewInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(String viewName) {
        this.interactor.execute(viewName);
    }

}
