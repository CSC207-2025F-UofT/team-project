package interface_adapter.search_sticker;

import interface_adapter.ViewModel;

public class SearchStickerViewModel extends ViewModel<SearchStickerState> {
    public SearchStickerViewModel() {
        super("search sticker");
        setState(new SearchStickerState());
    }
}