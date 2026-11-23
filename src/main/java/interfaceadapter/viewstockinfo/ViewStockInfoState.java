package interfaceadapter.viewstockinfo;

import java.util.List;
import usecase.viewstockinfo.ViewStockInfoOutputData;

/** The stock details and watchlist state shown by the view. */
public class ViewStockInfoState {
  private final List<String> tickers;
  private final String watchlistErrorMessage;
  private final ViewStockInfoOutputData result;
  private final String errorMessage;

  ViewStockInfoState(List<String> tickers, String watchlistErrorMessage,
                     ViewStockInfoOutputData result, String errorMessage) {
    this.tickers = List.copyOf(tickers);
    this.watchlistErrorMessage = watchlistErrorMessage;
    this.result = result;
    this.errorMessage = errorMessage;
  }

  public List<String> getTickers() {
    return tickers;
  }

  public String getWatchlistErrorMessage() {
    return watchlistErrorMessage;
  }

  public ViewStockInfoOutputData getResult() {
    return result;
  }

  public String getErrorMessage() {
    return errorMessage;
  }
}
