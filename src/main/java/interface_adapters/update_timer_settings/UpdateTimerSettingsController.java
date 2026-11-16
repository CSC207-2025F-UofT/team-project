package interface_adapters.update_timer_settings;

import use_case.update_timer_settings.UpdateTimerSettingsInputBoundary;
import use_case.update_timer_settings.UpdateTimerSettingsInputData;

/**
 * Controller for the Update Timer Settings Use Case.
 */
public class UpdateTimerSettingsController {

    private UpdateTimerSettingsInputBoundary interactor;

    public UpdateTimerSettingsController(UpdateTimerSettingsInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Executes the Update Timer Settings Use Case.
     * @param focusDuration the length of the focus timer in minutes
     * @param breakDuration the length of the break timer in minutes
     */
    public void execute(int focusDuration, int breakDuration) {
        UpdateTimerSettingsInputData inputData = new UpdateTimerSettingsInputData(focusDuration, breakDuration);

        interactor.execute(inputData);
    }
}
