package use_case.search_sticker;

import entity.Sticker;
import java.util.ArrayList;
import java.util.List;

public class SearchStickerInteractor implements SearchStickerInputBoundary {
    private final SearchStickerDataAccessInterface dataAccess;
    private final SearchStickerOutputBoundary presenter;

    public SearchStickerInteractor(SearchStickerDataAccessInterface dataAccess,
                                   SearchStickerOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(SearchStickerInputData inputData) {
        String query = inputData.getQuery();
        if (query == null || query.isBlank()) {
            presenter.prepareFailView("Please enter a search term");
            return;
        }
        query = query.trim();

        List<Sticker> results;
        try {
            results = dataAccess.searchStickers(query);
        } catch (StickerSearchException e) {
            presenter.prepareFailView("Couldn't reach sticker search. Try again.");
            return;
        }

        if (results.isEmpty()) {
            presenter.prepareFailView("No stickers found for " + query);
            return;
        }

        List<String> ids = new ArrayList<>();
        List<String> urls = new ArrayList<>();
        for (Sticker sticker : results) {
            ids.add(sticker.getId());
            urls.add(sticker.getImageUrl());
        }
        presenter.prepareSuccessView(new SearchStickerOutputData(query, ids, urls));
    }
}