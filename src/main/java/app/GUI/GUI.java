package app.GUI;

import api.googlePlacesAPI.GooglePlacesFetcher;
import data_access.FileUserDataAccessObjectWithLocations;
import entity.user.UserFactory;
import interface_adapter.ViewManagerModel;
import interface_adapter.createAccount.CreateAccountController;
import interface_adapter.createAccount.CreateAccountPresenter;
import interface_adapter.createAccount.CreateAccountViewModel;
import interface_adapter.homepage.HomePageController;
import interface_adapter.homepage.HomePagePresenter;
import interface_adapter.homepage.HomePageViewModel;
import interface_adapter.loggedInFavouritesPage.LoggedInFavouritesPageController;
import interface_adapter.loggedInFavouritesPage.LoggedInFavouritesPagePresenter;
import interface_adapter.loggedInFavouritesPage.LoggedInFavouritesPageViewModel;
import interface_adapter.loggedInHomePage.LoggedInHomePageController;
import interface_adapter.loggedInHomePage.LoggedInHomePagePresenter;
import interface_adapter.loggedInHomePage.LoggedInHomePageViewModel;
import interface_adapter.loggedInSearchPage.LoggedInSearchPageController;
import interface_adapter.loggedInSearchPage.LoggedInSearchPagePresenter;
import interface_adapter.loggedInSearchPage.LoggedInSearchPageViewModel;
import interface_adapter.logout.LogoutController;
import interface_adapter.logout.LogoutPresenter;
import interface_adapter.signin.SignInController;
import interface_adapter.signin.SignInPresenter;
import interface_adapter.signin.SignInViewModel;
import interface_adapter.weatherReportPage.WeatherReportPageController;
import interface_adapter.weatherReportPage.WeatherReportPageViewModel;
import use_case.changePassword.*;
import use_case.createAccount.CreateAccountInputBoundary;
import use_case.createAccount.CreateAccountInteractor;
import use_case.createAccount.CreateAccountOutputBoundary;
import use_case.homePage.HomePageInputBoundary;
import use_case.homePage.HomePageInteractor;
import use_case.homePage.HomePageOutputBoundary;
import use_case.loggedInFavouritesPage.LoggedInFavouritesPageInputBoundary;
import use_case.loggedInFavouritesPage.LoggedInFavouritesPageInteractor;
import use_case.loggedInFavouritesPage.LoggedInFavouritesPageOutputBoundary;
import use_case.loggedInHomePage.LoggedInHomePageInputBoundary;
import use_case.loggedInHomePage.LoggedInHomePageInteractor;
import use_case.loggedInHomePage.LoggedInHomePageOutputBoundary;
import use_case.loggedInSearchPage.LoggedInSearchPageInputBoundary;
import use_case.loggedInSearchPage.LoggedInSearchPageInteractor;
import use_case.loggedInSearchPage.LoggedInSearchPageOutputBoundary;
import use_case.logout.LogoutInputBoundary;
import use_case.logout.LogoutInteractor;
import use_case.logout.LogoutOutputBoundary;
import use_case.signIn.SignInInputBoundary;
import use_case.signIn.SignInInteractor;
import use_case.signIn.SignInOutputBoundary;
import use_case.currentWeather.CurrentWeatherInputBoundary;
import use_case.currentWeather.CurrentWeatherInteractor;
import use_case.currentWeather.CurrentWeatherOutputBoundary;
import interface_adapter.weatherReportPage.WeatherReportPagePresenter;
//import use_case.signIn.LoginUserDataAccessInterface;
import view.*;

import javax.swing.*;
import java.awt.*;

// **  GUI  **//
public class GUI {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    final UserFactory userFactory = new UserFactory();
    final ViewManagerModel viewManagerModel = new ViewManagerModel();
    ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

    // DATABASE
    final FileUserDataAccessObjectWithLocations userDataAccessObject = new FileUserDataAccessObjectWithLocations("src/main/resources/users.csv", userFactory);

    //API
    private final GooglePlacesFetcher googlePlacesFetcher = new GooglePlacesFetcher();

    // VIEWS
    private HomePageView homePageView;
    private HomePageViewModel homePageViewModel;

    private CreateAccountView createAccountView;
    private CreateAccountViewModel createAccountViewModel;

    private SignInView signInView;
    private SignInViewModel signInViewModel;

    private LoggedInHomePageView loggedInHomePageView;
    private LoggedInHomePageViewModel loggedInHomePageViewModel;

    private LoggedInSearchPageView loggedInSearchPageView;
    private LoggedInSearchPageViewModel loggedInSearchPageViewModel;

    private LoggedInFavouritesPageView loggedInFavouritesPageView;
    private LoggedInFavouritesPageViewModel loggedInFavouritesPageViewModel;

    private WeatherReportView weatherReportView;
    private WeatherReportPageViewModel weatherReportPageViewModel;

    public GUI() {
        cardPanel.setLayout(cardLayout);
    }

    //** HOME PAGE **//
    public GUI addHomePageView() {
        homePageViewModel = new HomePageViewModel();
        homePageView = new HomePageView(homePageViewModel);
        cardPanel.add(homePageView, homePageView.getViewName());
        return this;
    }

    //** HOME PAGE USE CASES **//
    public GUI addHomePageUseCase() {
        final HomePageOutputBoundary homePageOutputBoundary = new HomePagePresenter(
                homePageViewModel,
                createAccountViewModel,
                signInViewModel,
                viewManagerModel);
        final HomePageInputBoundary homePageInputBoundary = new HomePageInteractor(homePageOutputBoundary);

        HomePageController controller = new HomePageController(homePageInputBoundary);
        homePageView.setHomePageController(controller);
        return this;
    }

    //**CREATE ACCOUNT PAGE**//
    public GUI addCreateAccountView() {
        createAccountViewModel = new CreateAccountViewModel();
        createAccountView = new CreateAccountView(createAccountViewModel);
        cardPanel.add(createAccountView, createAccountView.getViewName());
        return this;
    }

    public GUI addCreateAccountUseCases() {
        final CreateAccountOutputBoundary createAccountOutputBoundary = new CreateAccountPresenter(
                createAccountViewModel,
                homePageViewModel,
                signInViewModel,
                viewManagerModel
        );
        final CreateAccountInputBoundary createAccountInputBoundary = new CreateAccountInteractor(
                createAccountOutputBoundary,
                userDataAccessObject,
                userFactory
                );

        CreateAccountController controller = new CreateAccountController(createAccountInputBoundary);
        createAccountView.setCreateController(controller);
        return this;
    }

    // ** SIGN IN VIEW PAGE ** //
    public GUI addSignInView() {
        signInViewModel = new SignInViewModel();
        signInView = new SignInView(signInViewModel);
        cardPanel.add(signInView, signInView.getViewName());
        return this;
    }

    public GUI addSignInViewUseCase() {
        final SignInOutputBoundary signInPresenter = new SignInPresenter(
                signInViewModel,
                homePageViewModel,
                loggedInHomePageViewModel,
                viewManagerModel
        );
        final SignInInputBoundary loginInputBoundary = new SignInInteractor(signInPresenter, userDataAccessObject);

        SignInController controller = new SignInController(loginInputBoundary);
        signInView.setSignInController(controller);
        return this;
    }

    //**LOGGED IN HOME PAGE **//
    public GUI addLoggedInHomePageView() {
        loggedInHomePageViewModel = new LoggedInHomePageViewModel();
        loggedInHomePageView = new LoggedInHomePageView(loggedInHomePageViewModel, userDataAccessObject);
        cardPanel.add(loggedInHomePageView, loggedInHomePageView.getViewName());
        return this;
    }

    //**LOGGED IN HOME PAGE USE CASES **//
    public GUI addLoggedInHomePageUseCases() {
        final LoggedInHomePageOutputBoundary loggedInHomePageOutputBoundary = new LoggedInHomePagePresenter(
                loggedInHomePageViewModel,
                loggedInSearchPageViewModel,
                loggedInFavouritesPageViewModel,
                viewManagerModel);
        final LoggedInHomePageInputBoundary loggedInHomePageInputBoundary = new LoggedInHomePageInteractor(userDataAccessObject,
                loggedInHomePageOutputBoundary);

        final ChangePasswordInputBoundary changePasswordInputBoundary = new ChangePasswordInteractor((ChangePasswordUserDataAccessInterface) userDataAccessObject,
                (ChangePasswordOutputBoundary)loggedInHomePageOutputBoundary,
                userFactory,
                (FileUserDataAccessObjectWithLocations) userDataAccessObject);

        LoggedInHomePageController controller = new LoggedInHomePageController(loggedInHomePageInputBoundary, changePasswordInputBoundary);
        loggedInHomePageView.setHomePageController(controller);
        return this;
    }

    // **LOGGED IN SEARCH PAGE **//
    public GUI addLoggedInSearchPageView() {
        loggedInSearchPageViewModel = new LoggedInSearchPageViewModel();
        loggedInSearchPageView = new LoggedInSearchPageView(loggedInSearchPageViewModel);
        cardPanel.add(loggedInSearchPageView, loggedInSearchPageView.getViewName());
        return this;
    }

    public GUI addLoggedInSearchPageUseCases() {
        final LoggedInSearchPageOutputBoundary loggedInSearchPageOutputBoundary = new LoggedInSearchPagePresenter(
                loggedInSearchPageViewModel,
                loggedInHomePageViewModel,
                weatherReportPageViewModel,
                viewManagerModel
        );
        final LoggedInSearchPageInputBoundary loggedInSearchPageInputBoundary = new LoggedInSearchPageInteractor(loggedInSearchPageOutputBoundary, googlePlacesFetcher);

        LoggedInSearchPageController controller = new LoggedInSearchPageController(loggedInSearchPageInputBoundary);
        loggedInSearchPageView.setSearchPageController(controller);
        return this;
    }

    //**LOGGED IN FAVOURITES PAGE **//
    public GUI addLoggedInFavouritePageView() {
        loggedInFavouritesPageViewModel = new LoggedInFavouritesPageViewModel();
        loggedInFavouritesPageView = new LoggedInFavouritesPageView(loggedInFavouritesPageViewModel);
        cardPanel.add(loggedInFavouritesPageView, loggedInFavouritesPageView.getViewName());
        return this;
    }

    public GUI addLoggedInFavouritePageUseCases() {
        final LoggedInFavouritesPageOutputBoundary loggedInFavouritesPageOutputBoundary = new LoggedInFavouritesPagePresenter(
                loggedInFavouritesPageViewModel,
                loggedInHomePageViewModel,
                weatherReportPageViewModel,
                viewManagerModel
        );
        final LoggedInFavouritesPageInputBoundary loggedInFavouritesPageInputBoundary = new LoggedInFavouritesPageInteractor(loggedInFavouritesPageOutputBoundary);

        LoggedInFavouritesPageController controller = new LoggedInFavouritesPageController(loggedInFavouritesPageInputBoundary);
        loggedInFavouritesPageView.setFavouritesPageController(controller);
        return this;
    }

    //**Weather Report Page**//
    public GUI addWeatherReportView() {
        weatherReportPageViewModel = new WeatherReportPageViewModel();
        weatherReportView = new WeatherReportView(weatherReportPageViewModel);
        cardPanel.add(weatherReportView, weatherReportView.getViewName());
        return this;
    }
    public GUI addCurrentWeatherUseCases() {
        final CurrentWeatherOutputBoundary currentWeatherOutputBoundary = new WeatherReportPagePresenter(
                weatherReportPageViewModel,
                loggedInSearchPageViewModel,
                loggedInHomePageViewModel,
                viewManagerModel);
        final CurrentWeatherInputBoundary currentWeatherInputBoundary = new CurrentWeatherInteractor(
                userDataAccessObject, currentWeatherOutputBoundary);

        WeatherReportPageController controller = new WeatherReportPageController(currentWeatherInputBoundary);
        weatherReportView.setWeatherReportController(controller);
        return this;
    }

    public GUI addLogoutUseCases() {
        final LogoutOutputBoundary logoutOutputBoundary = new LogoutPresenter(viewManagerModel,
                createAccountViewModel,
                loggedInFavouritesPageViewModel,
                loggedInHomePageViewModel,
                loggedInSearchPageViewModel,
                signInViewModel,
                weatherReportPageViewModel);
        final LogoutInputBoundary logoutInputBoundary= new LogoutInteractor(userDataAccessObject, logoutOutputBoundary);
        LogoutController controller = new LogoutController(logoutInputBoundary);
        loggedInHomePageView.setLogoutController(controller);
        return this;
    }

    //** Build JFrame **//
    public JFrame build() {
        final JFrame application = new JFrame("Sophisticated Weather App");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(cardPanel);
        //** View on Start Up. **//
        viewManagerModel.setState(homePageViewModel.getViewName());
        viewManagerModel.firePropertyChanged();

        return application;
    }
}
