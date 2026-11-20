package interface_adapters.menu_bar;

import interface_adapters.ViewManagerModel;
import use_case.menu_bar.SwitchViewOutputBoundary;

public class SwitchViewPresenter implements SwitchViewOutputBoundary {
    private final ViewManagerModel viewManagerModel;

    public SwitchViewPresenter (ViewManagerModel viewManagerModel) {
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareSuccessView(String viewName) {
        this.viewManagerModel.setState(viewName);
        this.viewManagerModel.notifyViewChanged();
    }

}
