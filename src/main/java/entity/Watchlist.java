package entity;

import java.util.ArrayList;
import java.util.List;

public class Watchlist {

    private final List<String> stockWatchers = new ArrayList<>();

    public void addStock(String stockWatcher) {
        if (!stockWatchers.contains(stockWatcher)) {
            stockWatchers.add(stockWatcher);
        }
    }

    public void removeStock(String stockWatcher) {
        stockWatchers.remove(stockWatcher);
    }

    public List<String> getStocks() {
        return new ArrayList<>(stockWatchers);
    }
}
