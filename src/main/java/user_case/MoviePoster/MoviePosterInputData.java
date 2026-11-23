package user_case.MoviePoster;

/**
 * Input Data for the View Movie Poster Use Case.
 */
public class MoviePosterInputData {
    private final String movieTitle;
    private final String posterPath;

    public MoviePosterInputData(String movieTitle, String posterPath) {
        this.movieTitle = movieTitle;
        this.posterPath = posterPath;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public String getPosterPath() {
        return posterPath;
    }
}