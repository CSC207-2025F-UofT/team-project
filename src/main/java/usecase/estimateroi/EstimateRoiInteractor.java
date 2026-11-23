package usecase.estimateroi;

import java.util.List;

/**
 * The interactor for the Estimate ROI use case. It estimates the value of an investment after one
 * year by assuming the stock keeps growing at its average yearly rate over the chosen history.
 */
public class EstimateRoiInteractor implements EstimateRoiInputBoundary {
  private final EstimateRoiDataAccessInterface dataAccess;
  private final EstimateRoiOutputBoundary presenter;

  /**
   * Creates an interactor for the Estimate ROI use case.
   *
   * @param dataAccess The source of historical prices
   * @param presenter The output boundary that shows the result
   */
  public EstimateRoiInteractor(EstimateRoiDataAccessInterface dataAccess,
                               EstimateRoiOutputBoundary presenter) {
    this.dataAccess = dataAccess;
    this.presenter = presenter;
  }

  @Override
  public void execute(EstimateRoiInputData inputData) {
    final double amount = inputData.getAmount();
    final int historyLength = inputData.getHistoryLength();

    if (!Double.isFinite(amount) || amount <= 0) {
      presenter.prepareFailView("Please enter a positive investment amount.");
      return;
    }
    if (historyLength <= 0) {
      presenter.prepareFailView("Please select a valid history length.");
      return;
    }

    final List<Double> prices;
    try {
      prices = dataAccess.getHistoricalPrices(inputData.getTicker(), historyLength);
    } catch (RuntimeException exception) {
      presenter.prepareFailView("Unable to retrieve stock data. Please try again.");
      return;
    }

    if (prices == null || prices.size() < 2) {
      presenter.prepareFailView("Not enough historical data available.");
      return;
    }

    final double oldPrice = prices.get(0);
    final double currentPrice = prices.get(prices.size() - 1);
    if (!Double.isFinite(oldPrice) || !Double.isFinite(currentPrice)
            || oldPrice <= 0 || currentPrice <= 0) {
      presenter.prepareFailView("Invalid stock prices.");
      return;
    }

    // Average yearly growth rate over the history (compound annual growth rate).
    final double annualReturn = Math.pow(currentPrice / oldPrice, 1.0 / historyLength) - 1;
    final double estimatedValue = amount * (1 + annualReturn);

    presenter.prepareSuccessView(new EstimateRoiOutputData(
            estimatedValue, estimatedValue - amount, annualReturn * 100, historyLength));
  }
}
