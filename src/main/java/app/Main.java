//package app;
//import data_access.GetMovieData;
//import java.io.IOException;
//
//public class Main {
//    public static void main(String[] args){
//        //Read TMDB token from Intellij
//        String token = System.getenv("TMDB_ACCESS_TOKEN");
//        //Create the shared API client
//        GetMovieData apiClient = new GetMovieData(token);
//
//        try{
//            String result = apiClient.get("search/movie?query=Interstellar");
//            System.out.println(result);
//        } catch (IOException e){
//            System.out.println(e.getMessage());
//        }
//    }
//}
//
package app;

import data_access.FavoriteDataAccessObject;
import data_access.FavoritePosterDataAccessObject;
import interface_adapter.Favorite.FavoriteController;
import interface_adapter.Favorite.FavoritePresenter;
import interface_adapter.Favorite.FavoriteViewModel;
import user_case.Favorite.FavoriteInteractor;
import java.nio.file.Path;
import data_access.MovieDataAccessObject;
import data_access.ReviewDataAccess;
import interface_adapter.Reviews.ReviewPresenter;
import interface_adapter.Reviews.ReviewsController;
import interface_adapter.Reviews.ReviewsViewModel;
import interface_adapter.Search.*;
import interface_adapter.ViewManagerModel;
import user_case.Reviews.ReviewsInputBoundary;
import user_case.Reviews.ReviewsInteractor;
import user_case.Search.*;
import view.FavoritesPageView;
import data_access.WatchlistDataAccessObject;
import entity.WatchlistMovieFactory;
import interface_adapter.AddToWatchlist.AddToWatchlistController;
import interface_adapter.AddToWatchlist.AddToWatchlistPresenter;
import interface_adapter.AddToWatchlist.AddToWatchlistViewModel;
import interface_adapter.ViewWatchlist.ViewWatchlistController;
import interface_adapter.ViewWatchlist.ViewWatchlistPresenter;
import interface_adapter.ViewWatchlist.WatchlistViewModel;
import user_case.AddToWatchlist.AddToWatchlistInteractor;
import user_case.ViewWatchlist.ViewWatchlistInteractor;
import view.WatchlistPageView;
import entity.Movie;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import view.SearchPageView;
import view.SearchedResultsPageView;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            // Create the application window
            JFrame application = new JFrame("Flicksy");
            application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Create CardLayout for switching views
            CardLayout cardLayout = new CardLayout();
            JPanel views = new JPanel(cardLayout);

            application.add(views, BorderLayout.CENTER);

            FavoriteViewModel favoriteModel = new FavoriteViewModel();
            Path favoritesFile = Path.of(System.getProperty("user.home"), ".flicksy", "favorites.json");
            FavoriteDataAccessObject favoritesDao = new FavoriteDataAccessObject(favoritesFile.toString());
            Map<Integer, FavoritesPageView.MovieInfo> favoriteMovies = new ConcurrentHashMap<>();
            String token = System.getenv("token");
            FavoritePosterDataAccessObject favoritePosters = new FavoritePosterDataAccessObject(token);
            FavoritesPageView favoritesPage = new FavoritesPageView(favoritesDao::getFavoriteMovieIds,
                    id -> {
                        Movie movie = favoritePosters.getMovieById(id);
                        if (movie == null) {
                            return favoriteMovies.get(id);
                        }
                        FavoritesPageView.MovieInfo fresh = new FavoritesPageView.MovieInfo(
                                movie.getName(), movie.getPosterPath());
                        favoriteMovies.put(id, fresh);
                        return fresh;
                    }, favoritesDao::removeFavorite);
            FavoriteController favoriteController = new FavoriteController(new FavoriteInteractor(
                    favoritesDao, new FavoritePresenter(favoriteModel)));

            // Watchlist (add + view), saved in ~/.flicksy/watchlist.json
            Path watchlistFile = Path.of(System.getProperty("user.home"), ".flicksy", "watchlist.json");
            watchlistFile.getParent().toFile().mkdirs();
            WatchlistDataAccessObject watchlistDao = new WatchlistDataAccessObject(watchlistFile.toString());
            String envUser = System.getenv("FLICKSY_USERNAME");
            String watchlistUser = envUser == null || envUser.isBlank() ? "guest" : envUser;

            WatchlistViewModel watchlistViewModel = new WatchlistViewModel();
            ViewWatchlistController viewWatchlistController = new ViewWatchlistController(
                    new ViewWatchlistInteractor(watchlistDao, new ViewWatchlistPresenter(watchlistViewModel)));
            WatchlistPageView watchlistView = new WatchlistPageView(watchlistViewModel);
            watchlistView.setViewWatchlistController(viewWatchlistController);

            AddToWatchlistViewModel addToWatchlistViewModel = new AddToWatchlistViewModel();
            AddToWatchlistController addToWatchlistController = new AddToWatchlistController(
                    new AddToWatchlistInteractor(watchlistDao,
                            new AddToWatchlistPresenter(addToWatchlistViewModel), new WatchlistMovieFactory()));
            addToWatchlistViewModel.addListener(state -> JOptionPane.showMessageDialog(views, state.getMessage(),
                    "Watchlist", state.isSuccess()
                            ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE));
            // Create the ViewModels
            SearchBarViewModel searchBarViewModel =
                    new SearchBarViewModel();

            SearchedViewModel searchedViewModel =
                    new SearchedViewModel();

//            ReviewsViewModel reviewsViewModel = new ReviewsViewModel();

            ViewManagerModel viewManagerModel = new ViewManagerModel();
            // Initialize the data access object
            // Replace with your actual DAO class
            MovieDataAccessObject searchDataAccessObject =
                    new MovieDataAccessObject();

//            ReviewDataAccess reviewDataAccessObject = new ReviewDataAccess();

            // Create the presenter
            SearchPresenter searchPresenter = new SearchPresenter(viewManagerModel,
                            searchBarViewModel,
                            searchedViewModel
                    );


//            ReviewPresenter reviewPresenter = new ReviewPresenter(re);

            // Create the interactor
            SearchInputBoundary searchInteractor =
                    new SearchInteractor(
                            searchDataAccessObject,
                            searchPresenter
                    );

            // Create the controller
            SearchController searchController =
                    new SearchController(searchInteractor);

//            ReviewsInputBoundary reviewsInteractor = new ReviewsInteractor(reviewDataAccessObject,);

//            ReviewsController reviewsController = new ReviewsController();
            // Create the initial search page
            SearchPageView searchPageView =
                    new SearchPageView(searchBarViewModel);

            // Connect the controller to the search page
            searchPageView.setSearchController(searchController);

            // Add the search page to CardLayout
            views.add(searchPageView, "search");
            views.add(favoritesPage, "favorites");
            java.util.function.Consumer<String> openFavorites = previous -> {
                favoritesPage.open(System.getenv("FLICKSY_USERNAME"), () -> cardLayout.show(views, previous));
                cardLayout.show(views, "favorites");
            };
            searchPageView.onOpenFavorites(() -> openFavorites.accept("search"));

            // Watchlist page with a Back button to the previous page
            String[] watchlistReturnView = {"search"};
            JButton watchlistBack = new JButton("Back");
            watchlistBack.addActionListener(e -> cardLayout.show(views, watchlistReturnView[0]));
            JPanel watchlistPage = new JPanel(new BorderLayout());
            watchlistPage.add(watchlistBack, BorderLayout.NORTH);
            watchlistPage.add(watchlistView, BorderLayout.CENTER);
            views.add(watchlistPage, WatchlistViewModel.VIEW_NAME);
            java.util.function.Consumer<String> openWatchlist = previous -> {
                watchlistReturnView[0] = previous;
                viewWatchlistController.execute(watchlistUser);
                cardLayout.show(views, WatchlistViewModel.VIEW_NAME);
            };
            searchPageView.onOpenWatchlist(() -> openWatchlist.accept("search"));

            // Listen for changes to the searched ViewModel
            searchedViewModel.addListener(() -> {

                if (searchedViewModel.getState()
                        .getOutputMovieData() != null) {

                    SwingUtilities.invokeLater(() -> {

                        // Remove the previous results page
                        for (Component component : views.getComponents()) {
                            if (component instanceof SearchedResultsPageView) {
                                views.remove(component);
                            }
                        }

                        for (String[] row : searchedViewModel.getState().getOutputMovieData()) {
                            favoriteMovies.put(Integer.parseInt(row[3]), new FavoritesPageView.MovieInfo(row[0], row[2]));
                        }
                        // Create the results page using returned movie data
                        SearchedResultsPageView searchedResultsPageView =
                                new SearchedResultsPageView(searchedViewModel, movieId -> {
                                    favoriteController.addFavorite(System.getenv("FLICKSY_USERNAME"), movieId);
                                    return favoriteModel.getState().getMessage();
                                });

                        searchedResultsPageView.setSearchController(
                                searchController
                        );

                        searchedResultsPageView.onOpenFavorites(() -> openFavorites.accept("results"));
                        searchedResultsPageView.setAddToWatchlistController(addToWatchlistController);
                        searchedResultsPageView.setUsername(watchlistUser);
                        searchedResultsPageView.onOpenWatchlist(() -> openWatchlist.accept("results"));
                        views.add(searchedResultsPageView, "results");

                        cardLayout.show(views, "results");

                        views.revalidate();
                        views.repaint();
                    });
                }
            });

            // Display the search page initially
            cardLayout.show(views, "search");

            application.setSize(1000, 700);
            application.setLocationRelativeTo(null);
            application.setVisible(true);
        });
    }
}