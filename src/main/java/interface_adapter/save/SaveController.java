package interface_adapter.save;

import use_case.save.SaveInputBoundary;
import use_case.save.SaveInputData;

public class SaveController {
    private final SaveInputBoundary interactor;

    public SaveController(SaveInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(String username, int floor) {
        interactor.execute(new SaveInputData(username, floor));
    }
}
