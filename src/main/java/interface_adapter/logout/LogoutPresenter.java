package interface_adapter.logout;

import interface_adapter.ViewManagerModel;
import interface_adapter.createAccount.CreateAccountState;
import interface_adapter.createAccount.CreateAccountViewModel;
import interface_adapter.homepage.HomePageState;
import interface_adapter.homepage.HomePageViewModel;
import interface_adapter.loggedInFavouritesPage.LoggedInFavouritesPageState;
import interface_adapter.loggedInFavouritesPage.LoggedInFavouritesPageViewModel;
import interface_adapter.loggedInHomePage.LoggedInHomePageState;
import interface_adapter.loggedInHomePage.LoggedInHomePageViewModel;
import interface_adapter.loggedInSearchPage.LoggedInSearchPageState;
import interface_adapter.loggedInSearchPage.LoggedInSearchPageViewModel;
import interface_adapter.signin.SignInState;
import interface_adapter.signin.SignInViewModel;
import interface_adapter.weatherReportPage.WeatherReportPageState;
import interface_adapter.weatherReportPage.WeatherReportPageViewModel;
import use_case.logout.LogoutOutputBoundary;
import use_case.logout.LogoutOutputData;

/**
 * The Presenter for the Logout Use Case.
 */
public class LogoutPresenter implements LogoutOutputBoundary {

    private CreateAccountViewModel createAccountViewModel;
    private LoggedInFavouritesPageViewModel loggedInFavouritesPageViewModel;
    private LoggedInHomePageViewModel loggedInHomePageViewModel;
    private LoggedInSearchPageViewModel loggedInSearchPageViewModel;
    private SignInViewModel signInViewModel;
    private WeatherReportPageViewModel weatherReportPageViewModel;
    private ViewManagerModel viewManagerModel;

    public LogoutPresenter(ViewManagerModel viewManagerModel,
                           CreateAccountViewModel  createAccountViewModel,
                           LoggedInFavouritesPageViewModel loggedInFavouritesPageViewModel,
                           LoggedInHomePageViewModel loggedInHomePageViewModel,
                           LoggedInSearchPageViewModel  loggedInSearchPageViewModel,
                           SignInViewModel  signInViewModel,
                           WeatherReportPageViewModel weatherReportPageViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.createAccountViewModel = createAccountViewModel;
        this.loggedInFavouritesPageViewModel = loggedInFavouritesPageViewModel;
        this.loggedInHomePageViewModel = loggedInHomePageViewModel;
        this.loggedInSearchPageViewModel = loggedInSearchPageViewModel;
        this.signInViewModel = signInViewModel;
        this.weatherReportPageViewModel = weatherReportPageViewModel;
    }

    @Override
    public void prepareSuccessView(LogoutOutputData response) {

        final CreateAccountState state1 = createAccountViewModel.getState();
        final LoggedInFavouritesPageState state2 = loggedInFavouritesPageViewModel.getState();
        final LoggedInHomePageState state3 = loggedInHomePageViewModel.getState();
        final LoggedInSearchPageState state4 = loggedInSearchPageViewModel.getState();
        final SignInState state5 = signInViewModel.getState();
        final WeatherReportPageState state6 = weatherReportPageViewModel.getState();
        state1.clearState();
        state2.clearState();
        state3.clearState();
        state4.clearSuggestions();
        state6.clearState();
        state5.setUsername(response.getUsername());
        state5.setPassword("");
        this.signInViewModel.firePropertyChanged();
        this.viewManagerModel.setState(signInViewModel.getViewName());
        this.viewManagerModel.firePropertyChanged();
    }
}