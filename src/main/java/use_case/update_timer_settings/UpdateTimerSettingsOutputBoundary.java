package use_case.update_timer_settings;

/**
 * The output boundary for the Update Timer Settings Use Case.
 */
public interface UpdateTimerSettingsOutputBoundary {
    /**
     * Prepares the success view for the Update Timer Settings Use Case.
     * @param outputData the output data
     */
    void prepareSuccessView(UpdateTimerSettingsOutputData outputData);
    /**
     * Prepares the fail view for the Update Timer Settings Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);

}
