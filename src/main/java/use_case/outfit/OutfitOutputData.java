package use_case.outfit;

import java.util.List;

/** Plain immutable response data: no Swing components or database objects. */
public final class OutfitOutputData {
    public static final class ClothingData {
        public final String id, name, category, imagePath;
        public ClothingData(String id, String name, String category, String imagePath) {
            this.id = id; this.name = name; this.category = category; this.imagePath = imagePath;
        }
    }
    public static final class TagData {
        public final String id, name;
        public TagData(String id, String name) { this.id = id; this.name = name; }
    }
    public static final class OutfitData {
        public final String id, name;
        public final List<String> clothingIds, tagIds;
        public OutfitData(String id, String name, List<String> clothingIds, List<String> tagIds) {
            this.id = id; this.name = name;
            this.clothingIds = List.copyOf(clothingIds); this.tagIds = List.copyOf(tagIds);
        }
    }
    public final List<ClothingData> clothing;
    public final List<TagData> tags;
    public final List<OutfitData> outfits;
    public final String selectedTagId, message;
    public final boolean outfitCreated;

    public OutfitOutputData(List<ClothingData> clothing, List<TagData> tags,
                            List<OutfitData> outfits, String selectedTagId,
                            String message, boolean outfitCreated) {
        this.clothing = List.copyOf(clothing); this.tags = List.copyOf(tags);
        this.outfits = List.copyOf(outfits); this.selectedTagId = selectedTagId;
        this.message = message; this.outfitCreated = outfitCreated;
    }
}
