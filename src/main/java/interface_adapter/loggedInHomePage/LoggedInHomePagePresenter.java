package interface_adapter.loggedInHomePage;

import entity.weatherReport.WeatherReport;
import interface_adapter.ViewManagerModel;
import interface_adapter.loggedInFavouritesPage.LoggedInFavouritesPageState;
import interface_adapter.loggedInFavouritesPage.LoggedInFavouritesPageViewModel;
import interface_adapter.loggedInSearchPage.LoggedInSearchPageViewModel;
import use_case.changePassword.ChangePasswordOutputData;
import use_case.loggedInHomePage.LoggedInHomePageOutputBoundary;
import use_case.changePassword.ChangePasswordOutputBoundary;

import java.util.List;

public class LoggedInHomePagePresenter implements LoggedInHomePageOutputBoundary, ChangePasswordOutputBoundary {
    private final LoggedInHomePageViewModel loggedInHomePageViewModel;
    private final LoggedInSearchPageViewModel loggedInSearchPageViewModel;
    private final LoggedInFavouritesPageViewModel loggedInFavouritesPageViewModel;
    private final ViewManagerModel viewManagerModel;

    public LoggedInHomePagePresenter(LoggedInHomePageViewModel loggedInHomePageViewModel,
                                     LoggedInSearchPageViewModel loggedInSearchPageViewModel,
                                     LoggedInFavouritesPageViewModel loggedInFavouritesPageViewModel,
                                     ViewManagerModel viewManagerModel) {
        this.loggedInHomePageViewModel = loggedInHomePageViewModel;
        this.loggedInSearchPageViewModel = loggedInSearchPageViewModel;
        this.loggedInFavouritesPageViewModel = loggedInFavouritesPageViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void switchToLoggedInSearchView() {
        viewManagerModel.setState(loggedInSearchPageViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }

    @Override
    public void switchToLoggedInFavouritesView(List<WeatherReport> weatherReports) {
        LoggedInFavouritesPageState state = loggedInFavouritesPageViewModel.getState();
        state.setWeatherReports(weatherReports);
        loggedInFavouritesPageViewModel.setState(state);
        loggedInFavouritesPageViewModel.firePropertyChanged();
        viewManagerModel.setState(loggedInFavouritesPageViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }

    @Override
    public void prepareSuccessView(ChangePasswordOutputData outputData) {
        loggedInHomePageViewModel.getState().setPassword("");
        loggedInHomePageViewModel.getState().setPasswordError("");
        loggedInHomePageViewModel.firePropertyChanged("passwordError");
    }

    @Override
    public void prepareFailView(String error) {
        loggedInHomePageViewModel.getState().setPasswordError(error);
        loggedInHomePageViewModel.firePropertyChanged("passwordError");
    }
}
