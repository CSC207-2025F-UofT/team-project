package entity;

import java.util.List;
import java.util.Objects;

public final class Outfit {
    private final String id;
    private final String name;
    private final List<String> clothingIds;
    private final List<String> tagIds;

    public Outfit(String id, String name, List<String> clothingIds, List<String> tagIds) {
        this.id = Objects.requireNonNull(id);
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Enter an outfit name.");
        }
        if (clothingIds.isEmpty()) {
            throw new IllegalArgumentException("Select at least one clothing item.");
        }
        this.name = name.trim();
        this.clothingIds = List.copyOf(clothingIds);
        this.tagIds = List.copyOf(tagIds);
    }
    public String getId() { return id; }
    public String getName() { return name; }
    public List<String> getClothingIds() { return clothingIds; }
    public List<String> getTagIds() { return tagIds; }
}
