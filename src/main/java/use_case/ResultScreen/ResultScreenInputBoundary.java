package use_case.ResultScreen;

import use_case.show_results.ShowResultsInputData;

public interface ResultScreenInputBoundary {
    void execute(ResultScreenInputData inputData);

    void execute(ShowResultsInputData showResultsInputData);
}
