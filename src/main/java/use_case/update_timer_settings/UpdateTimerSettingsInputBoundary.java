package use_case.update_timer_settings;

/**
 * The input boundary for the Update Timer Settings Use Case.
 */
public interface UpdateTimerSettingsInputBoundary {

    /**
     * Execute the Update Timer Settings Use Case.
     * @param inputData the input data for this use case
     */
    void execute(UpdateTimerSettingsInputData inputData);
}
