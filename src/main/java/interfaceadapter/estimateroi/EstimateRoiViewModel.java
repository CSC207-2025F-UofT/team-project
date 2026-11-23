package interfaceadapter.estimateroi;

import interfaceadapter.ViewModel;

/**
 * Stores the estimate shown by the Estimate ROI view.
 */
public class EstimateRoiViewModel extends ViewModel<EstimateRoiState> {
  public static final String STATE_PROPERTY = "estimate";

  /** Creates the view model with no estimate shown. */
  public EstimateRoiViewModel() {
    super("estimate roi");
    setState(new EstimateRoiState(null, null));
  }
}
