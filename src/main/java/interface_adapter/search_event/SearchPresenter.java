package interface_adapter.search_event;
import entity.Event;
import use_case.search_event.SearchEventInputBoundary;
import use_case.search_event.SearchEventOutputBoundary;
import use_case.search_event.SearchEventOutputData;

import java.util.List;

public class SearchPresenter implements SearchEventOutputBoundary {
    private final SearchViewModel viewModel;

    public SearchPresenter(SearchViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(SearchEventOutputData outputData) {
        //TODO Fill in the present function
    }
}
