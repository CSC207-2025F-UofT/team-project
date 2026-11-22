package user_case.AddToWatchlist;

/**
 * Stores the output data after a movie is added to the watchlist.
 */
public class AddToWatchlistOutputData {
    private final String movieId;
    private final String movieTitle;
    private final String posterUrl;
    private final String message;

    public AddToWatchlistOutputData(String movieId, String movieTitle,
                                    String posterUrl, String message) {
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.posterUrl = posterUrl;
        this.message = message;
    }

    /**
     * Returns the ID of the movie.
     */
    public String getMovieId() {
        return movieId;
    }

    /**
     * Returns the title of the movie.
     */
    public String getMovieTitle() {
        return movieTitle;
    }

    /**
     * Returns the poster URL of the movie.
     */
    public String getPosterUrl() {
        return posterUrl;
    }

    /**
     * Returns the message indicating the result of adding the movie.
     */
    public String getMessage() {
        return message;
    }
}
