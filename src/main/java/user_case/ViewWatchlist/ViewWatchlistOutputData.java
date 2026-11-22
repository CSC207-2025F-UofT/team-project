package user_case.ViewWatchlist;

import entity.WatchlistMovie;

import java.util.List;

/**
 * Output data for the View Watchlist use case.
 */
public class ViewWatchlistOutputData {

    private final String username;
    private final List<WatchlistMovie> movies;

    public ViewWatchlistOutputData(String username, List<WatchlistMovie> movies) {
        this.username = username;
        this.movies = movies;
    }

    public String getUsername() {
        return username;
    }

    public List<WatchlistMovie> getMovies() {
        return movies;
    }
}
