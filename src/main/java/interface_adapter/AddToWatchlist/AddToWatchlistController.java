package interface_adapter.AddToWatchlist;

import user_case.AddToWatchlist.AddToWatchlistInputBoundary;
import user_case.AddToWatchlist.AddToWatchlistInputData;

/**
 * Controller for the Add to Watchlist use case.
 * Called by the view when the "Add to Watchlist" button is clicked.
 */
public class AddToWatchlistController {
    private final AddToWatchlistInputBoundary interactor;

    public AddToWatchlistController(AddToWatchlistInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Adds the given movie to the user's watchlist.
     * @param username the logged-in user
     * @param movieId the id of the movie being viewed
     * @param movieTitle the title of the movie being viewed
     * @param posterUrl the full poster URL of the movie being viewed
     */
    public void execute(String username, String movieId, String movieTitle, String posterUrl) {
        interactor.execute(new AddToWatchlistInputData(username, movieId, movieTitle, posterUrl));
    }
}
