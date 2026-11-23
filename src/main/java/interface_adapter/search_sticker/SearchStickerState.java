package interface_adapter.search_sticker;

import java.util.ArrayList;
import java.util.List;

/**
 * What the sticker panel shows: the last search, its results, or an error.
 */
public class SearchStickerState {
    private String query = "";
    private List<String> stickerIds = new ArrayList<>();
    private List<String> imageUrls = new ArrayList<>();
    private String error;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<String> getStickerIds() {
        return stickerIds;
    }

    public void setStickerIds(List<String> stickerIds) {
        this.stickerIds = stickerIds;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}