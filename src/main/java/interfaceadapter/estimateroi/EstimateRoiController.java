package interfaceadapter.estimateroi;

import usecase.estimateroi.EstimateRoiInputBoundary;
import usecase.estimateroi.EstimateRoiInputData;

/**
 * The controller for the Estimate ROI use case.
 */
public class EstimateRoiController {
  private final EstimateRoiInputBoundary interactor;

  /**
   * Creates a controller for the Estimate ROI use case.
   *
   * @param interactor The input boundary that produces the estimate
   */
  public EstimateRoiController(EstimateRoiInputBoundary interactor) {
    this.interactor = interactor;
  }

  /**
   * Requests an estimate for an investment in a stock.
   *
   * @param ticker The ticker of the stock
   * @param amount The dollar amount to invest
   * @param historyYears How many years of past prices to base the estimate on
   */
  public void execute(String ticker, double amount, int historyYears) {
    interactor.execute(new EstimateRoiInputData(ticker, amount, historyYears));
  }
}
