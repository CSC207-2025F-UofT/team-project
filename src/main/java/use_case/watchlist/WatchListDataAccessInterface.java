package use_case.watchlist;

import entity.Watchlist;

public interface WatchListDataAccessInterface {

    Watchlist get(String username);

    void save(String username, Watchlist watchlist);
}
