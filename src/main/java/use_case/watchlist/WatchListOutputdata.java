package use_case.watchlist;

import java.util.List;

public class WatchListOutputdata {

    private final List<String> stockWatchers;
    private final List<Change> changes;

    public WatchListOutputdata(List<String> stockWatchers, List<Change> changes){
        this.stockWatchers = stockWatchers;
        this.changes = changes;
    }

    public List<String> getStockWatchers(){
        return stockWatchers;
    }

    public List<Change> getChanges(){
        return changes;
    }

    public static class Change {

        private final String stockWatcher;
        private  final double latestPrice;
        private final double percentChange;
        private final String errorMessage;

        private Change(String stockWatcher, double latestPrice, double percentChange, String errorMessage){
            this.stockWatcher = stockWatcher;
            this.latestPrice = latestPrice;
            this. percentChange = percentChange;
            this.errorMessage = errorMessage;
        }

        public static Change success(String stockWatcher, double lastestPrice, double percentChange){
            return new Change(stockWatcher, lastestPrice, percentChange, null);
        }

        public static Change failure(String stockWatcher, double lastestPrice, double percentChange, String errorMessage){
            return new Change(stockWatcher, 0, 0, errorMessage);
        }


        public String getStockWatcher(){
            return stockWatcher;
        }

        public double getLatestPrice(){
            return latestPrice;
        }

        public double getPercentChange(){
            return percentChange;
        }

        public String getErrorMessage(){
            return errorMessage;
        }

        public boolean hasError(){
            return errorMessage != null;
        }
    }
}
