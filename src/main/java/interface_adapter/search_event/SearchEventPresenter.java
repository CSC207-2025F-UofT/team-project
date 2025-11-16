package interface_adapter.search_event;
import entity.Event;
import use_case.search_event.SearchEventInputBoundary;
import use_case.search_event.SearchEventOutputBoundary;
import use_case.search_event.SearchEventOutputData;
import SearchEventViewModel.java;

    public class SearchEventPresenter implements SearchEventOutputBoundary {
        private final SearchEventViewModel viewModel;

        public SearchEventPresenter(SearchEventViewModel viewModel) {
            this.viewModel = viewModel;
        }

        @Override
        public void present(SearchEventOutputData outputData) {
            //TODO Fill in the present function
        }
    }