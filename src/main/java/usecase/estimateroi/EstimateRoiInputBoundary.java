package usecase.estimateroi;

/**
 * Input boundary for the Estimate ROI use case.
 */
public interface EstimateRoiInputBoundary {
  /**
   * Estimates the future value of an investment in a stock.
   *
   * @param inputData The stock, amount, and history length to base the estimate on
   */
  void execute(EstimateRoiInputData inputData);
}
