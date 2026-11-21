package use_case.watchlist;

import entity.Watchlist;

public interface watchListDataAccessInterface {
    Watchlist load(String username);

    void save(String username, Watchlist watchlist);
}
