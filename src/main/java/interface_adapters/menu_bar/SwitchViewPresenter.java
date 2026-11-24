package interface_adapters.menu_bar;

import interface_adapters.ViewManagerModel;
import use_case.menu_bar.SwitchViewOutputBoundary;

/**
 * Presenter for the Switch View Use Case.
 */
public class SwitchViewPresenter implements SwitchViewOutputBoundary {
    private final NavigationBarViewModel navigationBarViewModel;
    private final ViewManagerModel viewManagerModel;

    public SwitchViewPresenter (NavigationBarViewModel navigationBarViewModel, ViewManagerModel viewManagerModel) {
        this.viewManagerModel = viewManagerModel;
        this.navigationBarViewModel = navigationBarViewModel;
    }

    /**
     * Prepares the View Manager Model for a successful execution of the use case.
     * @param viewName the name of the view to switch to
     */
    @Override
    public void prepareSuccessView(String viewName) {
        navigationBarViewModel.getState().setCurrentViewName(viewName);
        navigationBarViewModel.notifyStateChanged();

        this.viewManagerModel.setState(viewName);
        this.viewManagerModel.notifyViewChanged();
    }

}
