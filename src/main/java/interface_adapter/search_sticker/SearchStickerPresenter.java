package interface_adapter.search_sticker;

import use_case.search_sticker.SearchStickerOutputBoundary;
import use_case.search_sticker.SearchStickerOutputData;

import java.util.ArrayList;

/**
 * Takes the use case's result and puts it into the view model's state.
 */
public class SearchStickerPresenter implements SearchStickerOutputBoundary {
    private final SearchStickerViewModel viewModel;

    public SearchStickerPresenter(SearchStickerViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(SearchStickerOutputData outputData) {
        SearchStickerState state = viewModel.getState();
        state.setQuery(outputData.getQuery());
        state.setStickerIds(outputData.getStickerIds());
        state.setImageUrls(outputData.getImageUrls());
        state.setError(null);
        viewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        SearchStickerState state = viewModel.getState();
        state.setStickerIds(new ArrayList<>());
        state.setImageUrls(new ArrayList<>());
        state.setError(errorMessage);
        viewModel.firePropertyChanged();
    }
}