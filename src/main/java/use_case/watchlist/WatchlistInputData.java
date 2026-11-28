package use_case.watchlist;

public class WatchlistInputData {

    private final String username;
    private final String stockWatcher;

    public WatchlistInputData(String username, String stockWatcher){
        this.username = username;
        this.stockWatcher = stockWatcher;
    }

    public WatchlistInputData(String username){
        this.username = username;
        this.stockWatcher = null;
    }

    public String getUsername(){
        return username;
    }

    public String getStockWatcher(){
        return stockWatcher;
    }

}
