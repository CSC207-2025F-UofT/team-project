package interface_adapter.result;
import use_case.ResultScreen.ResultScreenInputBoundary;
import use_case.ResultScreen.ResultScreenInputData;
import use_case.show_results.ShowResultsInputBoundary;
import use_case.show_results.ShowResultsInputData;

public class ResultController {
//    private final ResultScreenInputBoundary interactor;
//
//    public ResultController(ResultScreenInputBoundary interactor) {
//        this.interactor = interactor;
//    }
//
//    public void showResultScreen(boolean completed) {
//        ResultScreenInputData data = new ResultScreenInputData(completed);
//        interactor.execute(data);
//    }
private ShowResultsInputBoundary showResultsUseCaseInteractor;

    public void ShowResultsController(ShowResultsInputBoundary showResultsUseCaseInteractor) {
        this.showResultsUseCaseInteractor = showResultsUseCaseInteractor;
    }

    public ResultController(ShowResultsInputBoundary showResultsUseCaseInteractor) {
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
