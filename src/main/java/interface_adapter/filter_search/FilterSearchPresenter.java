package interface_adapter.filter_search;

import interface_adapter.ViewManagerModel;
import use_case.filter_search.FilterSearchOutputBoundary;
import use_case.filter_search.FilterSearchOutputData;

public class FilterSearchPresenter implements FilterSearchOutputBoundary {

    private final FilterSearchViewModel viewModel;
    private final ViewManagerModel viewManagerModel;

    public FilterSearchPresenter(FilterSearchViewModel viewModel,
                                 ViewManagerModel viewManagerModel) {
        this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareSuccessView(FilterSearchOutputData outputData) {
        FilterSearchState state = new FilterSearchState();
        state.setFiltered(outputData.getFilteredTransactions());
        state.setError(null);

        viewModel.setState(state);
        viewModel.firePropertyChanged();

        viewManagerModel.setState("filter search");
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        FilterSearchState state = new FilterSearchState();
        state.setFiltered(null);
        state.setError(errorMessage);

        viewModel.setState(state);
        viewModel.firePropertyChanged();

        viewManagerModel.setState("filter search");
        viewManagerModel.firePropertyChange();
    }
}
