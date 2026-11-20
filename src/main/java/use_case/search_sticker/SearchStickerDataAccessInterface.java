package use_case.search_sticker;

import entity.Sticker;
import java.util.List;

public interface SearchStickerDataAccessInterface {
    List<Sticker> searchStickers(String query) throws StickerSearchException;
}