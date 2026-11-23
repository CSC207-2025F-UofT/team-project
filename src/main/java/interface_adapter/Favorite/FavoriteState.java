package interface_adapter.Favorite;

/** UI state for the latest add-to-favourites request. */
public class FavoriteState {
    private final String username;
    private final int tmdbMovieId;
    private final String message;
    private final boolean successful;

    public FavoriteState(String username, int tmdbMovieId, String message, boolean successful) {
        this.username = username;
        this.tmdbMovieId = tmdbMovieId;
        this.message = message;
        this.successful = successful;
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

    public boolean isSuccessful() {
        return successful;
    }
}
