package user_case.ViewWatchlist;

import entity.WatchlistMovie;

import java.util.List;

/**
 * Data access interface required by the View Watchlist use case.
 */
public interface ViewWatchlistDataAccessInterface {

    /**
     * Returns all the movies saved in the given user's watchlist.
     * @param username the user whose watchlist is requested
     * @return the list of movies in the user's watchlist (empty if none)
     */
    List<WatchlistMovie> getWatchlist(String username);
}
