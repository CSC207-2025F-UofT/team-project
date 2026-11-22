package interfaceadapter.setriskprofile;

import usecase.setriskprofile.SetRiskProfileInputBoundary;
import usecase.setriskprofile.SetRiskProfileInputData;

/**
 * The controller for the Set Risk Profile use case.
 */
public class SetRiskProfileController {
  private final SetRiskProfileInputBoundary interactor;

  /**
   * Creates a new Set Risk Profile controller.
   *
   * @param interactor Interactor for the use case
   */
  public SetRiskProfileController(SetRiskProfileInputBoundary interactor) {
    this.interactor = interactor;
  }

  /**
   * Executes the Set Risk Profile use case.
   *
   * @param ticker Ticker of the stock to estimate for
   * @param amount Dollar amount to invest
   * @param targetGainPercent Target gain in percent (e.g., 20% would be 20.0)
   * @param maxHoldingPeriod Maximum holding period in trading days
   * @param windowMonths Number of past months of prices to estimate from
   */
  public void execute(String ticker, double amount, double targetGainPercent,
                      int maxHoldingPeriod, int windowMonths) {
    interactor.execute(new SetRiskProfileInputData(ticker, amount, targetGainPercent / 100.0,
            maxHoldingPeriod, windowMonths));
  }

  /**
   * Loads the user's saved settings and the available presets.
   */
  public void loadSettings() {
    interactor.loadSettings();
  }
}
