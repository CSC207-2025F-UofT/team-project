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
import interface_adapter.Favorite.FavoriteController;
import interface_adapter.Favorite.FavoritePresenter;
import interface_adapter.Favorite.FavoriteViewModel;
import user_case.Favorite.FavoriteInteractor;
import view.FavoritePanel;
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
            FavoriteController favoriteController = new FavoriteController(new FavoriteInteractor(
                    new FavoriteDataAccessObject(favoritesFile.toString()), new FavoritePresenter(favoriteModel)));
            FavoritePanel favoritePanel = new FavoritePanel(favoriteController, favoriteModel,
                    () -> System.getenv("FLICKSY_USERNAME"));

            JComboBox<MovieChoice> movies = new JComboBox<>();
            movies.setPrototypeDisplayValue(new MovieChoice("Select a movie to favorite", 0));
            movies.setEnabled(false);
            movies.addActionListener(event -> {
                MovieChoice selected = (MovieChoice) movies.getSelectedItem();
                favoritePanel.setMovieId(selected == null ? 0 : selected.id);
            });
            JPanel favoriteBar = new JPanel(new BorderLayout());
            JPanel selection = new JPanel(new FlowLayout(FlowLayout.LEADING));
            JLabel movieLabel = new JLabel("Movie to favorite:");
            movieLabel.setLabelFor(movies);
            selection.add(movieLabel);
            selection.add(movies);
            favoriteBar.add(selection, BorderLayout.WEST);
            favoriteBar.add(favoritePanel, BorderLayout.CENTER);
            application.add(favoriteBar, BorderLayout.SOUTH);

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

            // Listen for changes to the searched ViewModel
            searchedViewModel.addListener(() -> {

                if (searchedViewModel.getState()
                        .getOutputMovieData() != null) {

                    SwingUtilities.invokeLater(() -> {

                        movies.removeAllItems();
                        for (String[] row : searchedViewModel.getState().getOutputMovieData()) {
                            movies.addItem(new MovieChoice(row[0], Integer.parseInt(row[3])));
                        }
                        movies.setEnabled(movies.getItemCount() > 0);

                        // Remove the previous results page
                        for (Component component : views.getComponents()) {
                            if (component instanceof SearchedResultsPageView) {
                                views.remove(component);
                            }
                        }

                        // Create the results page using returned movie data
                        SearchedResultsPageView searchedResultsPageView =
                                new SearchedResultsPageView(searchedViewModel);

                        searchedResultsPageView.setSearchController(
                                searchController
                        );

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

    private static final class MovieChoice {
        private final String title;
        private final int id;

        MovieChoice(String title, int id) {
            this.title = title;
            this.id = id;
        }

        @Override
        public String toString() {
            return title + " (" + id + ")";
        }
    }
}
