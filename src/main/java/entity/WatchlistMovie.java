package entity;

/**
 * A movie saved in a user's watchlist.
 * Only stores what the watchlist page needs: id, title and poster URL.
 */
public class WatchlistMovie {
    private final String id;
    private final String title;
    private final String posterUrl;

    public WatchlistMovie(String id, String title, String posterUrl) {
        this.id = id;
        this.title = title;
        this.posterUrl = posterUrl;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterUrl() {
        return posterUrl;
    }
}
