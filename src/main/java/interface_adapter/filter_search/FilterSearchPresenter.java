package interface_adapter.filter_search;

import use_case.filter_search.FilterSearchOutputBoundary;
import use_case.filter_search.FilterSearchOutputData;

public class FilterSearchPresenter implements FilterSearchOutputBoundary {

    private final FilterSearchViewModel viewModel;

    public FilterSearchPresenter(FilterSearchViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(FilterSearchOutputData outputData) {

        FilterSearchState newState = new FilterSearchState();
        newState.setResults(outputData.getResults());
        newState.setErrorMessage(null);

        viewModel.setState(newState);
    }

    @Override
    public void presentError(String errorMessage) {

        FilterSearchState newState = new FilterSearchState();
        newState.setResults(null);
        newState.setErrorMessage(errorMessage);

        viewModel.setState(newState);
    }
}
