package interface_adapter.start_round;

import use_case.start_round.StartRoundInputBoundary;

public class StartRoundController {
    private final StartRoundInputBoundary interactor;

    public StartRoundController(StartRoundInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute() {
        interactor.execute();
    }
}