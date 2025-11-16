package use_case.save_outfit;

import entity.Outfit;
import java.util.List;

public interface OutfitDataAccessInterface {

    void saveOutfit(Outfit outfit);

    boolean existsByNameAndProfile(String name, String weatherProfile, String location);

    List<Outfit> getAllOutfits();
}
