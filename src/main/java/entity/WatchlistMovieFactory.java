package entity;

public class WatchlistMovieFactory {
    public WatchlistMovie create(String id, String title, String posterUrl) {
        return new WatchlistMovie(id, title, posterUrl);
    }
}
