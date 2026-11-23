package use_case.outfit;

import java.util.List;

/** Grouped MVP operations; can be split into individual use-case packages later. */
public interface OutfitInputBoundary {
    void load(String tagId);
    void createOutfit(String name, List<String> clothingIds, List<String> tagIds);
    void createTag(String name);
    void deleteOutfit(String outfitId);
}
