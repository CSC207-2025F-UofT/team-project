package entity;

import java.util.List;
import java.util.Objects;

public final class ClothingItem {
    private final String id;
    private final String name;
    private final ClothingType type;
    private final String imagePath;
    private final List<String> extractedFeatures;

    public ClothingItem(String id, String name, ClothingType type,
                        String imagePath, List<String> extractedFeatures) {
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.type = Objects.requireNonNull(type);
        this.imagePath = Objects.requireNonNull(imagePath);
        this.extractedFeatures = List.copyOf(extractedFeatures);
    }
    public String getId() { return id; }
    public String getName() { return name; }
    public ClothingType getType() { return type; }
    public String getImagePath() { return imagePath; }
    public List<String> getExtractedFeatures() { return extractedFeatures; }
}
