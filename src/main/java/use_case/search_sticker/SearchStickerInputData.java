package use_case.search_sticker;

public class SearchStickerInputData {
    private final String query;

    public SearchStickerInputData(String query) {
        this.query = query;
    }

    public String getQuery() { return query; }
}
