package interface_adapters.start_timer;

import use_case.start_timer.StartTimerInputBoundary;
import use_case.start_timer.StartTimerInputData;

/**
 * Controller for the Start Timer Use Case.
 */
public class StartTimerController {

    private final StartTimerInputBoundary startTimerUseCaseInteractor;

    public StartTimerController(StartTimerInputBoundary startTimerUseCaseInteractor) {
        this.startTimerUseCaseInteractor = startTimerUseCaseInteractor;
    }

    /**
     * Executes the Start Timer Settings Use Case.
     */
    public void execute(String sessionType) {
        StartTimerInputData inputData = new StartTimerInputData(sessionType);
        startTimerUseCaseInteractor.execute(inputData);
    }
}
