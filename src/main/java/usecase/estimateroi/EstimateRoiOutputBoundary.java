package usecase.estimateroi;

/**
 * Output boundary for the Estimate ROI use case.
 */
public interface EstimateRoiOutputBoundary {
  /**
   * Shows a successful estimate.
   *
   * @param outputData The estimated value, profit or loss, and history length used
   */
  void prepareSuccessView(EstimateRoiOutputData outputData);

  /**
   * Shows why an estimate could not be produced.
   *
   * @param errorMessage A message explaining the problem to the user
   */
  void prepareFailView(String errorMessage);
}
