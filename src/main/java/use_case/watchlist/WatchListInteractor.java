package use_case.watchlist;

import entity.Watchlist;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WatchListInteractor implements WatchlistInputBoundary {

    private final WatchListDataAccessInterface watchListDataAccessObject;
    private final WatchlistStockDataAccessInterface watchListStockDataAccessObject;
    private final WatchlistOutputBoundary watchlistPresenter;

    public WatchListInteractor(
            WatchListDataAccessInterface watchListDataAccessObject, WatchlistStockDataAccessInterface watchlistStockDataAccessObject,
            WatchlistOutputBoundary watchlistPresenter){
        this.watchListDataAccessObject = watchListDataAccessObject;
        this.watchListStockDataAccessObject = watchlistStockDataAccessObject;
        this. watchlistPresenter = watchlistPresenter;

    }

    @Override
    public void showWatchlist(WatchlistInputData inputData){
        Watchlist watchlist = watchListDataAccessObject.get(inputData.getUsername());

        watchlistPresenter.prepareSuccessView(new WatchListOutputdata(watchlist.getStocks(), new ArrayList<>()));
    }

    @Override
    public void addStock(WatchlistInputData inputData){
        String userName = inputData.getUsername();
        String stockWatcher = inputData.getStockWatcher();

        if (stockWatcher == null || stockWatcher.isBlank()) {
            watchlistPresenter.prepareFailView("Cannot leave entry blank");
            return;
        }

        String stock = stockWatcher.trim().toUpperCase();
        if (!stock.matches("[A-Z0-9.\\-]{1,10}")){
            watchlistPresenter.prepareFailView("Enter a valid stock symbol");
            return;
        }

        Watchlist watchlist = watchListDataAccessObject.get(userName);
        if (watchlist.getStocks().contains(stock)){
            watchlistPresenter.prepareFailView(stock + " is already on your list");
            return;
        }

        watchlist.addStock(stock);
        watchListDataAccessObject.save(userName, watchlist);

        watchlistPresenter.prepareSuccessView(new WatchListOutputdata(watchlist.getStocks(), new ArrayList<>()));
    }

    @Override
    public void removeStock (WatchlistInputData inputData){
        String username = inputData.getUsername();

        Watchlist watchlist = watchListDataAccessObject.get(username);
        watchlist.removeStock(inputData.getStockWatcher());
        watchListDataAccessObject.save(username, watchlist);

        watchlistPresenter.prepareSuccessView(new WatchListOutputdata(watchlist.getStocks(), new ArrayList<>()));
    }

    private WatchListOutputdata.Change findChange(String stockWatcher){
        try {
            List<Double> closes = watchListStockDataAccessObject.getStockPrice(stockWatcher, 2);

            if (closes.size() < 2 || closes.get(0) == 0){
                return WatchListOutputdata.Change.failure(stockWatcher,0, 0,
                        "Not enough data available from API");
            }

            double previous = closes.get(0);
            double latest = closes.get(1);
            double percent = (latest - previous) / previous * 100;

            return WatchListOutputdata.Change.success(stockWatcher, latest, percent, "");
        }
        catch (IOException e){
            return WatchListOutputdata.Change.failure(stockWatcher, 0, 0, e.getMessage());
        }
    }

    @Override
    public void refreshChange(WatchlistInputData inputData){
        String username = inputData.getUsername();

        Watchlist watchlist = watchListDataAccessObject.get(username);

        List<WatchListOutputdata.Change> changes = new ArrayList<>();
        for (String stockWatcher : watchlist.getStocks()){
            changes.add(findChange(stockWatcher));
        }
    }
}
