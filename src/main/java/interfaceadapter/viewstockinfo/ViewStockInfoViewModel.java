package interfaceadapter.viewstockinfo;

import interfaceadapter.ViewModel;
import java.util.List;

/** Stores the stock lookup result for the stock information view. */
public class ViewStockInfoViewModel extends ViewModel<ViewStockInfoState> {
  public static final String RESULT_PROPERTY = "result";
  public static final String WATCHLIST_PROPERTY = "watchlist";

  /** Creates the view model with an empty watchlist and no stock selected. */
  public ViewStockInfoViewModel() {
    super("stock info");
    setState(new ViewStockInfoState(List.of(), null, null, null));
  }
}
