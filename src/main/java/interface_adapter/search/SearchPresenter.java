package interface_adapter.search;

import use_case.search.SearchOutputData;
import use_case.search.SearchOutputDataBoundary;

public class SearchPresenter implements SearchOutputDataBoundary {

    private final SearchViewModel viewModel;

    public SearchPresenter(SearchViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(SearchOutputData outputData) {
        viewModel.setResults(outputData.getResults());
        viewModel.setErrorMessage(null);

        viewModel.firePropertyChanged();  // 通知 LoggedInView 更新 UI
    }

    @Override
    public void prepareFailView(String errorMessage) {
        viewModel.setResults(null);
        viewModel.setErrorMessage(errorMessage);

        viewModel.firePropertyChanged();  // 通知 LoggedInView 显示错误
    }
}
