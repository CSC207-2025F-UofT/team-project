package data;

public interface WatchlistRepository {

    boolean isWatched(String username, String symbol);

    void addWatched(String username,
                    String symbol,
                    String name,
                    String exchange);

    void removeWatched(String username, String symbol);
}