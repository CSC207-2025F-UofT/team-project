package user_case.AddToWatchlist;

import entity.WatchlistMovie;

public interface AddToWatchlistDataAccessInterface {
    /**
     * To check whether the movie is already in user watchlist
     * @param username username
     * @param movieId movie id
     * @return true if the movie is already existed in watch list
     */
    boolean isInWatchlist(String username, String movieId);

    /**
     * Adds a movie to the user's watchlist and saves it permanently.
     * @param username the user
     * @param movie the movie to save
     */
    void addToWatchlist(String username, WatchlistMovie movie);
}
