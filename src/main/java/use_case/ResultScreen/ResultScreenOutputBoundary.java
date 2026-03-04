package use_case.ResultScreen;

import use_case.show_results.ShowResultsOutputData;

public interface ResultScreenOutputBoundary {
    void prepareSuccessView(ResultScreenOutputData outputData);

    void prepareSuccessView(ShowResultsOutputData outputData);
}
