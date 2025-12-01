package interface_adapter.result;
import use_case.ResultScreen.ResultScreenInputBoundary;
import use_case.ResultScreen.ResultScreenInputData;

public class ResultController {
    private final ResultScreenInputBoundary interactor;

    public ResultController(ResultScreenInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void showResultScreen(boolean completed) {
        ResultScreenInputData data = new ResultScreenInputData(completed);
        interactor.execute(data);
    }
}
