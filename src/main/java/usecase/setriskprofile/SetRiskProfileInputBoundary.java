package usecase.setriskprofile;

/**
 * The Set Risk Profile use case.
 */
public interface SetRiskProfileInputBoundary {
  /**
   * Execute the Set Risk Profile use case.
   *
   * @param inputData The input data for this use case
   */
  void execute(SetRiskProfileInputData inputData);

  /**
   * Loads the user's saved settings (or the defaults if none are saved) along with the available
   * risk profile presets, and passes them to the presenter.
   */
  void loadSettings();
}
