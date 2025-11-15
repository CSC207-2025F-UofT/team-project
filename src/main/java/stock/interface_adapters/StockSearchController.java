package stock.interface_adapters;

package stock.interface_adapters.controllers;

import stock.use_case.stock_search.StockSearchInputData;
import stock.use_case.stock_search.StockSearchInteractor;
import stock.use_case.stock_search.StockSearchOutputData;

public class StockSearchController {
    private final StockSearchInteractor stockSearchInteractor;

    public StockSearchController(StockSearchInteractor stockSearchInteractor) {
        this.stockSearchInteractor = stockSearchInteractor;
    }


    public StockSearchOutputData search(String keywords) {
        StockSearchInputData input = new StockSearchInputData(keywords);
        return stockSearchInteractor.execute(input);
    }
}
