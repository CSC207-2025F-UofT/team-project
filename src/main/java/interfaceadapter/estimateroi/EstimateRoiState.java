package interfaceadapter.estimateroi;

import usecase.estimateroi.EstimateRoiOutputData;

/**
 * The state shown by the Estimate ROI view.
 */
public class EstimateRoiState {
  /** The last successful estimate, kept when a later request fails. */
  private final EstimateRoiOutputData result;
  private final String errorMessage;

  EstimateRoiState(EstimateRoiOutputData result, String errorMessage) {
    this.result = result;
    this.errorMessage = errorMessage;
  }

  public EstimateRoiOutputData getResult() {
    return result;
  }

  public String getErrorMessage() {
    return errorMessage;
  }
}
