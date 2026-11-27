package interface_adapter.game_running;

import use_case.log_input.LogInputInputBoundary;
import use_case.log_input.LogInputInputData;

public class LogInputController {
    private final LogInputInputBoundary logInputUseCaseInteractor;

    public LogInputController(LogInputInputBoundary logInputUseCaseInteractor) {
        this.logInputUseCaseInteractor = logInputUseCaseInteractor;
    }

    /**
     * Executes the Load Question use case.
     */
    public void execute(String teamName, String questionText, String guess, boolean correct, int pointsAwarded) {
        LogInputInputData logInputInputData = new LogInputInputData(teamName, questionText, guess, correct, pointsAwarded);
        logInputUseCaseInteractor.execute(logInputInputData);
    }
}
