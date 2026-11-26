package user_case.Favorite;

/** Successful result returned by the add-to-favourites use case. */
public class FavoriteOutputData {
    private final String username;
    private final int tmdbMovieId;
    private final String message;

    public FavoriteOutputData(String username, int tmdbMovieId, String message) {
        this.username = username;
        this.tmdbMovieId = tmdbMovieId;
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public int getTmdbMovieId() {
        return tmdbMovieId;
    }

    public String getMessage() {
        return message;
    }
}
