package interface_adapter.search_sticker;

import use_case.search_sticker.SearchStickerInputBoundary;
import use_case.search_sticker.SearchStickerInputData;

/**
 * Turns what the user typed into input data and hands it to the use case.
 */
public class SearchStickerController {
    private final SearchStickerInputBoundary interactor;

    public SearchStickerController(SearchStickerInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(String query) {
        interactor.execute(new SearchStickerInputData(query));
    }
}