package interface_adapters.save_session;

import use_case.save_session.SaveSessionInputBoundary;
import use_case.save_session.SaveSessionInputData;

/**
 * Controller for the Save Session Use Case.
 */
public class SaveSessionController {

    private SaveSessionInputBoundary interactor;

    public SaveSessionController(SaveSessionInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Executes the Save Session Use Case.
     * @param focusDuration the length of the focus timer in minutes
     * @param breakDuration the length of the break timer in minutes
     */
    public void execute(int focusDuration, int breakDuration) {
        SaveSessionInputData inputData = new SaveSessionInputData(focusDuration, breakDuration);

        interactor.execute(inputData);
    }
}
