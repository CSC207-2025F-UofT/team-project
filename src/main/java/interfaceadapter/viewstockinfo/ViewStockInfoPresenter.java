package interfaceadapter.viewstockinfo;

import java.util.List;
import usecase.viewstockinfo.ViewStockInfoOutputBoundary;
import usecase.viewstockinfo.ViewStockInfoOutputData;

/** Updates the view model when a lookup finishes. */
public class ViewStockInfoPresenter implements ViewStockInfoOutputBoundary {
  private final ViewStockInfoViewModel viewModel;

  /**
   * Creates a presenter for watchlist and stock lookup results.
   *
   * @param viewModel The view model to update with results and errors
   */
  public ViewStockInfoPresenter(ViewStockInfoViewModel viewModel) {
    this.viewModel = viewModel;
  }

  @Override
  public synchronized void prepareWatchlistView(List<String> tickers) {
    showWatchlist(tickers, null);
  }

  @Override
  public synchronized void prepareWatchlistFailView(String errorMessage) {
    showWatchlist(List.of(), errorMessage);
  }

  @Override
  public synchronized void prepareSuccessView(ViewStockInfoOutputData outputData) {
    showResult(outputData, null);
  }

  @Override
  public synchronized void prepareFailView(String errorMessage) {
    showResult(null, errorMessage);
  }

  private void showWatchlist(List<String> tickers, String errorMessage) {
    ViewStockInfoState current = viewModel.getState();
    viewModel.setState(new ViewStockInfoState(tickers, errorMessage,
            current.getResult(), current.getErrorMessage()));
    viewModel.firePropertyChange(ViewStockInfoViewModel.WATCHLIST_PROPERTY);
  }

  private void showResult(ViewStockInfoOutputData result, String errorMessage) {
    ViewStockInfoState current = viewModel.getState();
    viewModel.setState(new ViewStockInfoState(current.getTickers(),
            current.getWatchlistErrorMessage(), result, errorMessage));
    viewModel.firePropertyChange(ViewStockInfoViewModel.RESULT_PROPERTY);
  }
}
