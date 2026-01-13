package interface_adapter.results;

import use_case.show_results.ShowResultsInputBoundary;
import use_case.show_results.ShowResultsInputData;

/**
 * Controller for showing results screen.
 */
public class ShowResultsController {
    private final ShowResultsInputBoundary showResultsUseCaseInteractor;

    public ShowResultsController(ShowResultsInputBoundary showResultsUseCaseInteractor) {
        this.showResultsUseCaseInteractor = showResultsUseCaseInteractor;
    }

    /**
     * Executes the show results use case.
     */
    public void execute() {
        final ShowResultsInputData inputData = new ShowResultsInputData();
        showResultsUseCaseInteractor.execute(inputData);
    }
}
