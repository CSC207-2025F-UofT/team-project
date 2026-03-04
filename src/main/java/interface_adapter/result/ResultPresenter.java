package interface_adapter.result;
import interface_adapter.results.ResultsState;
import interface_adapter.results.ResultsViewModel;
import use_case.ResultScreen.ResultScreenOutputBoundary;
import use_case.ResultScreen.ResultScreenOutputData;
import use_case.show_results.ShowResultsOutputData;

public class ResultPresenter implements ResultScreenOutputBoundary {
//    private final ResultViewModel vm;
//
//    public ResultPresenter(ResultViewModel vm) {
//        this.vm = vm;
//    }
//
//    @Override
//    public void prepareSuccessView(ResultScreenOutputData outputData) {
//        vm.setMessage(outputData.getMessage());
//    }
private ResultsViewModel resultsViewModel;

    public void ShowResultsPresenter(ResultsViewModel resultsViewModel) {
        this.resultsViewModel = resultsViewModel;
    }

    public ResultPresenter(ResultsViewModel resultsViewModel) {
        this.resultsViewModel = resultsViewModel;
    }

    @Override
    public void prepareSuccessView(ResultScreenOutputData outputData) {

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
