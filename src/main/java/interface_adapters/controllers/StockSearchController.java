package interface_adapters.controllers;

import data.WatchlistRepository;
import use_case.stocksearch.StockSearchInputData;
import use_case.stocksearch.StockSearchInteractor;
import use_case.stocksearch.StockSearchOutputData;

/**
 * Controller for stock search and watchlist actions.
 * Delegates search to the StockSearchInteractor and watch/unwatch
 * operations to the WatchlistRepository.
 */
public class StockSearchController {

    private final StockSearchInteractor stockSearchInteractor;
    private final WatchlistRepository watchlistRepository;

    public StockSearchController(StockSearchInteractor stockSearchInteractor,
                                 WatchlistRepository watchlistRepository) {
        this.stockSearchInteractor = stockSearchInteractor;
        this.watchlistRepository = watchlistRepository;
    }

    /**
     * Performs a stock search based on the given keywords.
     */
    public StockSearchOutputData search(String keywords) {
        StockSearchInputData input = new StockSearchInputData(keywords);
        return stockSearchInteractor.execute(input);
    }

    /**
     * Returns true if the given symbol is in the user's watchlist.
     */
    public boolean isWatched(String username, String symbol) {
        return watchlistRepository.isWatched(username, symbol);
    }

    /**
     * Adds or removes a stock from the user's watchlist.
     */
    public void setWatched(String username,
                           String symbol,
                           String name,
                           String exchange,
                           boolean watched) {
        if (watched) {
            watchlistRepository.addWatched(username, symbol, name, exchange);
        } else {
            watchlistRepository.removeWatched(username, symbol);
        }
    }
}