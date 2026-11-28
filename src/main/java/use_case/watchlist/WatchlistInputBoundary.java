package use_case.watchlist;

public interface WatchlistInputBoundary {

    void showWatchlist(WatchlistInputData inputData);
    void addStock(WatchlistInputData inputData);
    void removeStock(WatchlistInputData inputData);
    void refreshChange(WatchlistInputData inputData);
}
