package usecase.setriskprofile;

/**
 * Output boundary for the Set Risk Profile use case.
 */
public interface SetRiskProfileOutputBoundary {
  /**
   * Prepares the success view for the Set Risk Profile use case.
   *
   * @param outputData The output data
   */
  void prepareSuccessView(SetRiskProfileOutputData outputData);

  /**
   * Prepares the failure view for the Set Risk Profile use case.
   *
   * @param errorMessage Explanation of the failure
   */
  void prepareFailView(String errorMessage);

  /**
   * Prepares the view with the available presets and the user's current (saved or default)
   * settings.
   *
   * @param outputData The presets and settings
   */
  void prepareSettingsView(SetRiskProfileSettingsOutputData outputData);
}
