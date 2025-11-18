package interface_adapter.loggedInHomePage;

import interface_adapter.ViewManagerModel;
import interface_adapter.loggedInFavourites.LoggedInFavouritesViewModel;
import interface_adapter.loggedInSearch.LoggedInSearchViewModel;
import use_case.loggedInHomePage.LoggedInHomePageOutputBoundary;

public class LoggedInHomePagePresenter implements LoggedInHomePageOutputBoundary {
    private final LoggedInHomePageViewModel loggedInHomePageViewModel;
    private final LoggedInSearchViewModel loggedInSearchViewModel;
    private final LoggedInFavouritesViewModel loggedInFavouritesViewModel;
    private final ViewManagerModel viewManagerModel;

    public LoggedInHomePagePresenter(LoggedInHomePageViewModel loggedInHomePageViewModel,
                                     LoggedInSearchViewModel loggedInSearchViewModel,
                                     LoggedInFavouritesViewModel loggedInFavouritesViewModel,
                                     ViewManagerModel viewManagerModel) {
        this.loggedInHomePageViewModel = loggedInHomePageViewModel;
        this.loggedInSearchViewModel = loggedInSearchViewModel;
        this.loggedInFavouritesViewModel = loggedInFavouritesViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void switchToLoggedInSearchView() {
        viewManagerModel.setState(loggedInSearchViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }

    @Override
    public void switchToLoggedInFavouritesView() {
        viewManagerModel.setState(loggedInFavouritesViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }
}
