package use_case.add_clothing;

import use_case.outfit.OutfitOutputData.ClothingData;
import java.util.List;

/** Plain immutable response data: no Swing components or database objects. */
public final class WardrobeOutputData {
    public final List<ClothingData> clothing;
    public final String message;
    public final boolean itemAdded;

    public WardrobeOutputData(List<ClothingData> clothing, String message, boolean itemAdded) {
        this.clothing = List.copyOf(clothing);
        this.message = message;
        this.itemAdded = itemAdded;
    }
}
