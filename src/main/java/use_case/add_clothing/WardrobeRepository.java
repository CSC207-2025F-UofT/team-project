package use_case.add_clothing;

import entity.ClothingItem;
import java.util.List;

/** Database teammate implements this port. Failed writes must not partially change data. */
public interface WardrobeRepository {
    List<ClothingItem> getClothing();
    void saveClothing(ClothingItem item);
}
