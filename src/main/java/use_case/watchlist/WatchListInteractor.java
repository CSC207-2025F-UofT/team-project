package use_case.watchlist;

import entity.Watchlist;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WatchListInteractor implements WatchlistInputBoundary {

    private final WatchListDataAccessInterface watchListDataAccess;
    private final WatchlistStockDataAccessInterface watchListStockDataAccessObject;
    private final WatchlistOutputBoundary watchlistPresenter;

    public WatchListInteractor(
            WatchListDataAccessInterface watchListDataAccess, WatchlistStockDataAccessInterface watchlistStockDataAccessObject,
            WatchlistOutputBoundary watchlistPresenter){
        this.watchListDataAccess = watchListDataAccess;
        this.watchListStockDataAccessObject = watchlistStockDataAccessObject;
        this. watchlistPresenter = watchlistPresenter;

    }

    @Override
    public void showWatchlist(WatchlistInputData inputData){
        Watchlist watchlist;
        try{
            watchlist = watchListDataAccess.get(inputData.getUsername());
        }   catch (RuntimeException error){
            watchlistPresenter.prepareFailView(error.getMessage());
            return;
        }

        watchlistPresenter.prepareSuccessView((new WatchListOutputdata(watchlist.getStocks(), new ArrayList<>())));
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

        Watchlist watchlist;
        try{
            watchlist = watchListDataAccess.get(userName);

            if (watchlist.getStocks().contains(stock)){
                watchlistPresenter.prepareFailView(stock + " is already on the watchlist");
                return;
            }
            watchlist.addStock(stock);
            watchListDataAccess.save(userName, watchlist);
        } catch (RuntimeException error){
            watchlistPresenter.prepareFailView(error.getMessage());
            return;
        }

        watchlistPresenter.prepareSuccessView(new WatchListOutputdata(watchlist.getStocks(), new ArrayList<>()));
    }

    @Override
    public void removeStock (WatchlistInputData inputData){
        String username = inputData.getUsername();
        String stockWatcher = inputData.getStockWatcher();

        if (stockWatcher == null || stockWatcher.isBlank()){
            watchlistPresenter.prepareFailView("No stock selected");
            return;
        }

        String stock = stockWatcher.trim().toUpperCase();

        Watchlist watchlist;
        try{
            watchlist = watchListDataAccess.get(username);

            if(!watchlist.getStocks().contains(stock)){
                watchlistPresenter.prepareFailView(stock + " is not on the watchlist");
                return;
            }

            watchlist.removeStock(stock);
            watchListDataAccess.save(username, watchlist);
        }
        catch (RuntimeException error){
            watchlistPresenter.prepareFailView(error.getMessage());
            return;
        }

        watchlistPresenter.prepareSuccessView(new WatchListOutputdata(watchlist.getStocks(), new ArrayList<>()));
    }

    private WatchListOutputdata.Change findChange(String stockWatcher){
        try {
            List<Double> closes = watchListStockDataAccessObject.getStockPrice(stockWatcher, 2);

            if (closes == null || closes.size() < 2){
                return WatchListOutputdata.Change.failure(stockWatcher,0, 0,
                        "Not enough data available from API");
            }

            Double previous = closes.get(0);
            Double latest = closes.get(1);

            if (!isValidPrice(previous) || !isValidPrice(latest)){
                return WatchListOutputdata.Change.failure(stockWatcher, 0, 0,"invalid price data");
            }

            double percent = (latest - previous) / previous * 100;

            return WatchListOutputdata.Change.success(stockWatcher, latest, percent);
        }
        catch (IOException | RuntimeException error){
            return WatchListOutputdata.Change.failure(stockWatcher, 0, 0, error.getMessage());
        }
    }

    private boolean isValidPrice (Double price){

        return price != null && Double.isFinite(price) && price > 0;
    }

    @Override
    public void refreshChange(WatchlistInputData inputData){

        Watchlist watchlist;

        try{
            watchlist = watchListDataAccess.get(inputData.getUsername());
        }
        catch (RuntimeException error){
            watchlistPresenter.prepareFailView(error.getMessage());
            return;
        }

        List<WatchListOutputdata.Change> changes = new ArrayList<>();
        for (String stockWatcher : watchlist.getStocks()){
            changes.add(findChange(stockWatcher));
        }

        watchlistPresenter.prepareSuccessView(new WatchListOutputdata(watchlist.getStocks(), changes));
    }
}
