package interfaceadapter.viewstockinfo;

import usecase.viewstockinfo.ViewStockInfoInputBoundary;
import usecase.viewstockinfo.ViewStockInfoInputData;

/** The controller for the View Stock Info use case. */
public class ViewStockInfoController {
  private final ViewStockInfoInputBoundary interactor;

  /**
   * Creates a controller for the View Stock Info use case.
   *
   * @param interactor The input boundary used to load the watchlist and stock details
   */
  public ViewStockInfoController(ViewStockInfoInputBoundary interactor) {
    this.interactor = interactor;
  }

  /**
   * Requests details for the stock selected from the watchlist.
   *
   * @param ticker The ticker of the selected stock
   */
  public void execute(String ticker) {
    interactor.execute(new ViewStockInfoInputData(ticker));
  }

  /** Requests the saved watchlist for the Stock Info view. */
  public void loadWatchlist() {
    interactor.loadWatchlist();
  }
}
