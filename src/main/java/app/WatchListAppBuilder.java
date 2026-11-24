package app;

import data_access.WatchListStockDataAccessObject;
import interface_adapter.watchlist.WatchListController;
import interface_adapter.watchlist.WatchListViewModel;
import interface_adapter.watchlist.WatchlistPresenter;
import stockprototype.StockDataSource;
import use_case.watchlist.*;
import view.WatchlistView;

public class WatchListAppBuilder {

    public static WatchlistView createWatchlistView(String userName, StockDataSource stockDataSource, WatchListDataAccessInterface watchListDAO){
        WatchlistStockDataAccessInterface priceDAO = new WatchListStockDataAccessObject(stockDataSource);
        WatchListViewModel viewModel = new WatchListViewModel();

        WatchlistOutputBoundary presenter = new WatchlistPresenter(viewModel);

        WatchlistInputBoundary interactor = new WatchListInteractor(watchListDAO, priceDAO, presenter);

        WatchListController controller = new WatchListController(interactor);

        return new WatchlistView(controller, viewModel, userName);
    }
}
