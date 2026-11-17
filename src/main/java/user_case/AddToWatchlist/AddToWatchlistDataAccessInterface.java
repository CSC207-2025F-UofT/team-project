package user_case.AddToWatchlist;

public interface AddToWatchlistDataAccessInterface {
    /**
     * To check whether the movie is already in user watchlist
     * @param username username
     * @param movieId movie id
     * @return true if the movie is already existed
     */
    boolean isInWatchlist(String username, String movieId);

    /**
     * Add the movie to user watchlist.
     * @param username username user
     * @param movieId movie id
     * @param movieTitle movie title
     */
    void addToWatchlist(String username, String movieId, String movieTitle);
}
