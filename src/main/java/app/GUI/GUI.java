package app.GUI;

import entity.user.UserFactory;
import interface_adapter.ViewManagerModel;
import interface_adapter.createAccount.CreateAccountViewModel;
import interface_adapter.homepage.HomePageController;
import interface_adapter.homepage.HomePagePresenter;
import interface_adapter.homepage.HomePageViewModel;
import interface_adapter.loggedInHomePage.LoggedInHomePageController;
import interface_adapter.loggedInHomePage.LoggedInHomePagePresenter;
import interface_adapter.loggedInHomePage.LoggedInHomePageViewModel;
import interface_adapter.loggedInFavourites.LoggedInFavouritesViewModel;
import interface_adapter.loggedInSearch.LoggedInSearchViewModel;
import use_case.homePage.HomePageInputBoundary;
import use_case.homePage.HomePageInteractor;
import use_case.homePage.HomePageOutputBoundary;
import use_case.loggedInHomePage.LoggedInHomePageInputBoundary;
import use_case.loggedInHomePage.LoggedInHomePageInteractor;
import use_case.loggedInHomePage.LoggedInHomePageOutputBoundary;
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

    private HomePageView homePageView;
    private HomePageViewModel homePageViewModel;

    private CreateAccountView createAccountView;
    private CreateAccountViewModel createAccountViewModel;

    private LoggedInHomePageView loggedInHomePageView;
    private LoggedInHomePageViewModel loggedInHomePageViewModel;

    private LoggedInSeachView loggedInSeachView;
    private LoggedInSearchViewModel loggedInSearchViewModel;

    private LoggedInFavouritesView loggedInFavouritesView;
    private LoggedInFavouritesViewModel loggedInFavouritesViewModel;

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
        final HomePageOutputBoundary homePageOutputBoundary = new HomePagePresenter(homePageViewModel,
                createAccountViewModel, viewManagerModel);
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

    //**LOGGED IN HOME PAGE **//
    public GUI addLoggedInHomePageView() {
        loggedInHomePageViewModel = new LoggedInHomePageViewModel();
        loggedInHomePageView = new LoggedInHomePageView(loggedInHomePageViewModel);
        cardPanel.add(loggedInHomePageView, loggedInHomePageView.getViewName());
        return this;
    }

    //**LOGGED IN HOME PAGE USE CASES **//
    public GUI addLoggedInHomePageUseCases() {
        final LoggedInHomePageOutputBoundary loggedInHomePageOutputBoundary = new LoggedInHomePagePresenter(
                loggedInHomePageViewModel,
                loggedInSearchViewModel,
                loggedInFavouritesViewModel,
                viewManagerModel);
        final LoggedInHomePageInputBoundary loggedInHomePageInputBoundary = new LoggedInHomePageInteractor(loggedInHomePageOutputBoundary);

        LoggedInHomePageController controller = new LoggedInHomePageController(loggedInHomePageInputBoundary);
        loggedInHomePageView.setHomePageController(controller);
        return this;
    }

    // **LOGGED IN SEARCH PAGE **//
    public GUI addLoggedInSearchView() {
        loggedInSearchViewModel = new LoggedInSearchViewModel();
        loggedInSeachView = new LoggedInSeachView(loggedInSearchViewModel);
        cardPanel.add(loggedInSeachView, loggedInSeachView.getViewName());
        return this;
    }

    //**LOGGED IN FAVOURITES PAGE **//
    public GUI addLoggedInFavouriteView() {
        loggedInFavouritesViewModel = new LoggedInFavouritesViewModel();
        loggedInFavouritesView = new LoggedInFavouritesView(loggedInFavouritesViewModel);
        cardPanel.add(loggedInFavouritesView, loggedInFavouritesView.getViewName());
        return this;
    }

    public JFrame build() {
        final JFrame application = new JFrame("Sophisticated Weather App");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(cardPanel);
        //** View on Start Up. **//
        viewManagerModel.setState(loggedInHomePageView.getViewName());
        viewManagerModel.firePropertyChanged();

        return application;
    }
}
