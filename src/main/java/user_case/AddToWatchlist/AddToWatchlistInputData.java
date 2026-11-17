package user_case.AddToWatchlist;

public class AddToWatchlistInputData {

    private final String username;
    private final String movieId;
    private final String movieTitle;

    public AddToWatchlistInputData(String username, String movieId, String movieTitle) {
        this.username = username;
        this.movieId = movieId;
        this.movieTitle = movieTitle;
    }

    public String getUsername() {
        return username;
    }

    public String getMovieId() {
        return movieId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }
}
