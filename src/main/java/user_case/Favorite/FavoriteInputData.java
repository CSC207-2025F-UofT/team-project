package user_case.Favorite;

/** The data needed to add a TMDB movie to a Flicksy user's favourites. */
public class FavoriteInputData {
    private final String username;
    private final int tmdbMovieId;

    public FavoriteInputData(String username, int tmdbMovieId) {
        this.username = username;
        this.tmdbMovieId = tmdbMovieId;
    }

    public String getUsername() {
        return username;
    }

    public int getTmdbMovieId() {
        return tmdbMovieId;
    }
}
