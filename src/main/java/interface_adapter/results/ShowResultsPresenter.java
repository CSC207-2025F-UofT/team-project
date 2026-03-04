package interface_adapter.results;

import use_case.show_results.ShowResultsOutputBoundary;
import use_case.show_results.ShowResultsOutputData;

/**
 * Presenter for showing results screen.
 */
public class ShowResultsPresenter implements ShowResultsOutputBoundary {
    private final ResultsViewModel resultsViewModel;

    public ShowResultsPresenter(ResultsViewModel resultsViewModel) {
        this.resultsViewModel = resultsViewModel;
    }

    @Override
    public void prepareSuccessView(ShowResultsOutputData outputData) {
        final ResultsState state = resultsViewModel.getState();
        state.setUserName(outputData.getUserName());
        state.setTotalMoves(outputData.getTotalMoves());
        state.setPathHistory(outputData.getPathHistory());
        state.setFinalLocation(outputData.getFinalLocation());

        resultsViewModel.firePropertyChange();
    }
}
