package interface_adapter.settings;

import interface_adapter.ViewManagerModel;
import interface_adapter.loggedInHomePage.LoggedInHomePageViewModel;
import use_case.settings.SettingsOutputBoundary;

public class SettingsPresenter implements SettingsOutputBoundary {
    private final ViewManagerModel viewManagerModel;
    private final LoggedInHomePageViewModel loggedInHomePageViewModel;

    public SettingsPresenter(ViewManagerModel viewManagerModel, LoggedInHomePageViewModel loggedInHomePageViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.loggedInHomePageViewModel = loggedInHomePageViewModel;
    }

    @Override
    public void goBack() {
        viewManagerModel.setState(loggedInHomePageViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }
}
