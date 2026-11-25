package interface_adapter.load;

import use_case.load.LoadInputBoundary;
import use_case.load.LoadInputData;

public class LoadController {
    private final LoadInputBoundary interactor;

    public LoadController(LoadInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(String username) {
        interactor.execute(new LoadInputData(username));
    }
}