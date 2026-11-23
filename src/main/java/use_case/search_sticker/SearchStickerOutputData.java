package use_case.search_sticker;

import java.util.List;

public class SearchStickerOutputData {
    private final String query;
    private final List<String> stickerIds;
    private final List<String> imageUrls;

    public SearchStickerOutputData(String query, List<String> stickerIds, List<String> imageUrls) {
        this.query = query;
        this.stickerIds = List.copyOf(stickerIds);
        this.imageUrls = List.copyOf(imageUrls);
    }

    public String getQuery() { return query; }
    public List<String> getStickerIds() { return stickerIds; }
    public List<String> getImageUrls() { return imageUrls; }
}