package data_access;

import stockprototype.StockDataSource;
import use_case.watchlist.WatchlistStockDataAccessInterface;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;

public class WatchListStockDataAccessObject implements WatchlistStockDataAccessInterface {

    private final StockDataSource stockDataSource;

    public WatchListStockDataAccessObject(StockDataSource stockDataSource){
        this.stockDataSource = stockDataSource;
    }

    @Override
    public List<Double> getStockPrice(String stockWatcher, int count) throws IOException{
        NavigableMap<LocalDate, BigDecimal> prices = stockDataSource.getDailyClosingPrices(stockWatcher);

        List<Double> all = new ArrayList<>();
        for(BigDecimal price : prices.values()){
            all.add(price.doubleValue());
        }
        int start = Math.max(0, all.size() - count);
        return new ArrayList<>(all.subList(start, all.size()));
    }
}
