package use_case.outfit;

import entity.ClothingItem;
import entity.Outfit;
import entity.Tag;
import java.util.List;

/** Database teammate implements this port. Failed writes must not partially change data. */
public interface OutfitRepository {
    List<ClothingItem> getClothing();
    List<Outfit> getOutfits();
    List<Tag> getTags();
    void saveOutfit(Outfit outfit);
    void saveTag(Tag tag);
    void deleteOutfit(String outfitId);
}
