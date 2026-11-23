package interface_adapter.watchlist;

import use_case.watchlist.WatchlistInputBoundary;
import use_case.watchlist.WatchlistInputData;

public class WatchListController {

    private final WatchlistInputBoundary interactor;

    public WatchListController(WatchlistInputBoundary interactor){
        this.interactor = interactor;
    }

    public void showWatchList(String username, String stockWatcher){
        interactor.showWatchlist(new WatchlistInputData(username, stockWatcher));
    }

    public void addStock(String username, String stockWatcher){
        interactor.addStock(new WatchlistInputData(username, stockWatcher));
    }

    public void removeStock(String username, String stockWatcher){
        interactor.removeStock(new WatchlistInputData(username, stockWatcher));
    }

    public void getChanges(String username){
        interactor.refreshChange(new WatchlistInputData(username));
    }

}
