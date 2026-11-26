package user_case.ViewWatchlist;

/**
 * Input data for the View Watchlist use case.
 */
public class ViewWatchlistInputData {

    private final String username;

    public ViewWatchlistInputData(String username) {
        this.username = username;
    }

    /**
     * Return the username whose watchlist should be loaded.
     */
    public String getUsername() {
        return username;
    }
}
