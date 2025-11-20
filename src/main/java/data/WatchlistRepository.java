package data;

import java.util.List;

public interface WatchlistRepository {

    boolean isWatched(String username, String symbol);

    void addWatched(String username,
                    String symbol,
                    String name,
                    String exchange);

    void removeWatched(String username, String symbol);

    // list all watched stocks
    List<String> findSymbolsByUsername(String username);
}