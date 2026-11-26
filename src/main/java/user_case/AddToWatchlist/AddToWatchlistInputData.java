package user_case.AddToWatchlist;

public class AddToWatchlistInputData {

    private final String username;
    private final String movieId;
    private final String movieTitle;
    private final String posterUrl;

    public AddToWatchlistInputData(String username, String movieId,
                                   String movieTitle, String posterUrl) {
        this.username = username;
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.posterUrl = posterUrl;
    }

    /**
     * Return the username from the input data.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Return the id of the chosen movie from the input data.
     */
    public String getMovieId() {
        return movieId;
    }

    /**
     * Return the title of the chosen movie from the input data.
     */
    public String getMovieTitle() {
        return movieTitle;
    }

    /**
     * Return the poster of the chosen movie from the input data.
     */
    public String getPosterUrl() {
        return posterUrl;
    }


}
