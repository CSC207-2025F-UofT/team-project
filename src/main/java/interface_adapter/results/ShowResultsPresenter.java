package interface_adapter.results;

import interface_adapter.ViewManagerModel;
import interface_adapter.move.MoveViewModel;
import use_case.show_results.ShowResultsOutputBoundary;
import use_case.show_results.ShowResultsOutputData;

/**
 * Presenter for showing results screen.
 */
public class ShowResultsPresenter implements ShowResultsOutputBoundary {
    private final ResultsViewModel resultsViewModel;
    private final ViewManagerModel viewManagerModel;
    private final MoveViewModel moveViewModel;

    public ShowResultsPresenter(ResultsViewModel resultsViewModel, MoveViewModel moveViewModel, ViewManagerModel viewManagerModel) {
        this.resultsViewModel = resultsViewModel;
        this.moveViewModel = moveViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareSuccessView(ShowResultsOutputData outputData) {
        final ResultsState state = resultsViewModel.getState();
        state.setUserName(outputData.getUserName());
        state.setTotalMoves(outputData.getTotalMoves());
        state.setPathHistory(outputData.getPathHistory());
        state.setFinalLocation(outputData.getFinalLocation());

        resultsViewModel.firePropertyChange();

        viewManagerModel.setState(resultsViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void switchToOpenGameView() {
        moveViewModel.firePropertyChange();

        viewManagerModel.setState("OpenGame");
        viewManagerModel.firePropertyChange();
    }
}
