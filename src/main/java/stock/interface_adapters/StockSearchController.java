package stock.interface_adapters;

import stock.usecase_stocksearch.StockSearchInputData;
import stock.usecase_stocksearch.StockSearchInteractor;
import stock.usecase_stocksearch.StockSearchOutputData;

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
