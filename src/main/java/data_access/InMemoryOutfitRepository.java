package data_access;

import entity.*;
import use_case.outfit.OutfitRepository;
import use_case.add_clothing.WardrobeRepository;
import java.util.*;

/** Demo adapter only. All data is lost when the process exits. */
public final class InMemoryOutfitRepository implements OutfitRepository, WardrobeRepository {
    private final List<ClothingItem> clothing;
    private final Map<String, Outfit> outfits = new LinkedHashMap<>();
    private final Map<String, Tag> tags = new LinkedHashMap<>();

    public InMemoryOutfitRepository(List<ClothingItem> clothing) {
        this.clothing = new ArrayList<>(clothing);
    }
    public List<ClothingItem> getClothing() { return List.copyOf(clothing); }
    public void saveClothing(ClothingItem item) { clothing.add(item); }
    public List<Outfit> getOutfits() { return new ArrayList<>(outfits.values()); }
    public List<Tag> getTags() { return new ArrayList<>(tags.values()); }
    public void saveOutfit(Outfit outfit) { outfits.put(outfit.getId(), outfit); }
    public void saveTag(Tag tag) { tags.put(tag.getId(), tag); }
    public void deleteOutfit(String id) { outfits.remove(id); }
};