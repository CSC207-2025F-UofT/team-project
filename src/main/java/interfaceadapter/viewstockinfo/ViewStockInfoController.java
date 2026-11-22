package interfaceadapter.viewstockinfo;

import usecase.viewstockinfo.ViewStockInfoInputBoundary;
import usecase.viewstockinfo.ViewStockInfoInputData;

/** The controller for the View Stock Info use case. */
public class ViewStockInfoController {
  private final ViewStockInfoInputBoundary interactor;

  public ViewStockInfoController(ViewStockInfoInputBoundary interactor) {
    this.interactor = interactor;
  }

  public void execute(String ticker) {
    interactor.execute(new ViewStockInfoInputData(ticker));
  }

  public void loadWatchlist() {
    interactor.loadWatchlist();
  }
}
