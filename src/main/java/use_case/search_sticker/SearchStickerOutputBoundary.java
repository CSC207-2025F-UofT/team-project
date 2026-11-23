package use_case.search_sticker;

public interface SearchStickerOutputBoundary {
    void prepareSuccessView(SearchStickerOutputData outputData);
    void prepareFailView(String errorMessage);
}