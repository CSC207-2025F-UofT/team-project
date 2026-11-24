package interface_adapter.Loggedin;

import use_case.get_data.get_dataInputBoundary;
import use_case.get_data.get_dataInputData;

/**
 * The controller for the get_data Use Case.
 */
public class get_dataController {
    private final get_dataInputBoundary getDataInteractor;

    public get_dataController(get_dataInputBoundary getDataInteractor) {
        this.getDataInteractor = getDataInteractor;
    }

    /**
     * Executes the get_data Use Case.
     * @param username the user whose data should be fetched
     */
    public void execute(String username) {
        getDataInteractor.execute(new get_dataInputData(username));
    }

    /**
     * Switches to the graph view.
     */
    public void switchToUserViewGraphView() {
        getDataInteractor.switchToUserViewGraphView();
    }
}
