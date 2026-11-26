package app;

import data_access.UserDataAccess;
import entity.UserFactory;
import interface_adapter.ViewManagerModel;
import interface_adapter.Login.LoginViewModel;
import interface_adapter.Signup.*;
import user_case.SignUp.SignupInputData;
import user_case.SignUp.SignupInteractor;
import user_case.SignUp.SignupInputBoundary;
import view.LoginView;
import view.SignupView;
import interface_adapter.Login.*;
import interface_adapter.LoggedIn.LoggedInViewModel;
import user_case.Login.*;
import data_access.MovieDataAccessObject;
import interface_adapter.Search.*;
import user_case.Search.*;
import view.SearchPageView;
import view.SearchedResultsPageView;
import data_access.GetMovieData;
import data_access.MovieDetailDataAccessObject;
import interface_adapter.MovieDetail.*;
import user_case.MovieDetail.*;
import view.MovieDetailView;
import data_access.WatchlistDataAccessObject;
import entity.WatchlistMovieFactory;
import interface_adapter.AddToWatchlist.*;
import interface_adapter.ViewWatchlist.*;
import user_case.AddToWatchlist.*;
import user_case.ViewWatchlist.*;
import view.WatchlistPageView;
import java.io.File;

import javax.swing.*;
import java.awt.*;

public class AppBuilder {
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel views = new JPanel(cardLayout);
    private final ViewManagerModel viewManagerModel = new ViewManagerModel();

    private final UserDataAccess userDataAccess = new UserDataAccess();
    private final UserFactory userFactory = new UserFactory();
    private SearchedResultsPageView resultView;
    private MovieDetailController movieDetailController;
    private AddToWatchlistController addToWatchlistController;
    private ViewWatchlistController viewWatchlistController;
    private String watchlistReturnView = SearchBarViewModel.VIEW_NAME;

    //view models
    private final LoginViewModel loginViewModel = new LoginViewModel();
    private final SignupViewModel signupViewModel = new SignupViewModel();
    private LoginView loginView;
    private final LoggedInViewModel loggedInViewModel = new LoggedInViewModel();

    public AppBuilder() {
        viewManagerModel.addListener(() -> {
            cardLayout.show(views, viewManagerModel.getCurrentViewName());
        });
    }

    //sign up case
    public AppBuilder addSignUpCase() {
        SignupPresenter presenter = new SignupPresenter(viewManagerModel, signupViewModel, loginViewModel);
        SignupInputBoundary interactor = new SignupInteractor(userDataAccess, presenter, userFactory);
        SignupController controller = new SignupController(interactor);
        SignupView signupView = new SignupView(signupViewModel);
        signupView.setSignupController(controller);
        views.add(signupView, SignupViewModel.VIEW_NAME);
        return this;
    }

    //login view
    public AppBuilder addLoginView(){
        loginView = new LoginView(loginViewModel, viewManagerModel);
        views.add(loginView,LoginViewModel.VIEW_NAME);
        return this;
    }

    //login case
    public AppBuilder addLoginUseCase(){
        LoginPresenter presenter = new LoginPresenter(viewManagerModel, loggedInViewModel, loginViewModel);
        LoginInputBoundary interactor = new LoginInteractor(userDataAccess, presenter);
        LoginController controller = new LoginController(interactor);
        loginView.setLoginController(controller);
        return this;
    }

    //search use case
    public AppBuilder addSearchUseCase() {
        //view models
        SearchBarViewModel searchViewModel = new SearchBarViewModel();
        SearchedViewModel searchedViewModel = new SearchedViewModel();

        //data access
        MovieDataAccessObject dataAccess = new MovieDataAccessObject();

        //presenter
        SearchPresenter presenter = new SearchPresenter(viewManagerModel, searchViewModel, searchedViewModel);

        //interactor
        SearchInputBoundary interactor = new SearchInteractor(dataAccess, presenter);

        //controller
        SearchController controller = new SearchController(interactor);

        //homepage
        SearchPageView searchView = new SearchPageView(searchViewModel);
        searchView.setSearchController(controller);
        searchView.onOpenWatchlist(() -> openWatchlist(SearchBarViewModel.VIEW_NAME));
        views.add(searchView, SearchBarViewModel.VIEW_NAME);

        //display search result
        searchedViewModel.addListener(() -> {
            if (searchedViewModel.getState().getOutputMovieData() != null) {
                SwingUtilities.invokeLater(() -> {
                    if (resultView != null) {
                        views.remove(resultView);
                    }
                    resultView = new SearchedResultsPageView(searchedViewModel);
                    resultView.setSearchController(controller);
                    resultView.setMovieDetailController(movieDetailController);
                    resultView.setAddToWatchlistController(addToWatchlistController);
                    resultView.setUsername(loggedInViewModel.getState().getUsername());
                    resultView.onOpenWatchlist(() -> openWatchlist(SearchedViewModel.VIEW_NAME));

                    views.add(resultView, SearchedViewModel.VIEW_NAME);
                    cardLayout.show(views, SearchedViewModel.VIEW_NAME);

                    views.revalidate();
                    views.repaint();
                });
            }
        }); return this;
    }

    //movie details use case
    public AppBuilder addMovieDetailUseCase(){
        MovieDetailViewModel viewModel = new MovieDetailViewModel();
        MovieDetailPresenter presenter = new MovieDetailPresenter(viewManagerModel, viewModel);
        GetMovieData api = new GetMovieData(System.getenv("TMDB_ACCESS_TOKEN"));
        MovieDetailDataAccessObject dataAccess = new MovieDetailDataAccessObject(api);
        MovieDetailInputBoundary interactor = new MovieDetailInteractor(dataAccess, presenter);
        movieDetailController = new MovieDetailController(interactor);

        //movie details popup
        MovieDetailView detailView = new MovieDetailView(viewModel);

        JDialog popup = new JDialog();
        popup.setTitle("Movie Details");
        popup.add(detailView);
        popup.setSize(550,350);
        popup.setLocationRelativeTo(null);

        //open popup when movie details received
        viewModel.addPropertyChangeListener(e ->{
            popup.setVisible(true);
        });
        return this;
    }

    //watchlist use cases (add to watchlist + view watchlist)
    public AppBuilder addWatchlistUseCases() {
        File file = new File(System.getProperty("user.home"), ".flicksy/watchlist.json");
        file.getParentFile().mkdirs();
        WatchlistDataAccessObject dataAccess = new WatchlistDataAccessObject(file.getPath());

        //view watchlist
        WatchlistViewModel watchlistViewModel = new WatchlistViewModel();
        ViewWatchlistOutputBoundary viewPresenter = new ViewWatchlistPresenter(watchlistViewModel);
        ViewWatchlistInputBoundary viewInteractor = new ViewWatchlistInteractor(dataAccess, viewPresenter);
        viewWatchlistController = new ViewWatchlistController(viewInteractor);

        WatchlistPageView watchlistView = new WatchlistPageView(watchlistViewModel);
        watchlistView.setViewWatchlistController(viewWatchlistController);

        //page + back button
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> cardLayout.show(views, watchlistReturnView));
        JPanel watchlistPage = new JPanel(new BorderLayout());
        watchlistPage.add(backButton, BorderLayout.NORTH);
        watchlistPage.add(watchlistView, BorderLayout.CENTER);
        views.add(watchlistPage, WatchlistViewModel.VIEW_NAME);

        //add to watchlist
        AddToWatchlistViewModel addViewModel = new AddToWatchlistViewModel();
        AddToWatchlistOutputBoundary addPresenter = new AddToWatchlistPresenter(addViewModel);
        AddToWatchlistInputBoundary addInteractor =
                new AddToWatchlistInteractor(dataAccess, addPresenter, new WatchlistMovieFactory());
        addToWatchlistController = new AddToWatchlistController(addInteractor);

        //show confirmation / error after "Add to Watchlist" is clicked
        addViewModel.addListener(state -> JOptionPane.showMessageDialog(views, state.getMessage(),
                "Watchlist", state.isSuccess()
                        ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE));
        return this;
    }

    //open watchlist page for the logged-in user
    private void openWatchlist(String returnView) {
        if (viewWatchlistController == null) {
            return;
        }
        watchlistReturnView = returnView;
        viewWatchlistController.execute(loggedInViewModel.getState().getUsername());
        cardLayout.show(views, WatchlistViewModel.VIEW_NAME);
    }

    //build app
    public JFrame build(){
        JFrame application = new JFrame("Flicksy Search");
        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        application.setContentPane(views);
        application.setSize(1000,700);
        application.setLocationRelativeTo(null);

        viewManagerModel.setState(LoginViewModel.VIEW_NAME);
        viewManagerModel.notifyViewChanged();

        return application;
    }
}
