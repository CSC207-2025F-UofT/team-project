package entity;

import java.util.ArrayList;
import java.util.List;

/**
 * One piece of clothing in the user's closet: name, category, sub-category,
 * colour, style, whether it can be layered, the warmth levels it can be worn at
 * (picked on the slider, e.g. [2, 3, 4, 5, 6]), and the path to its picture.
 * Warmth levels go from -10 (coldest) to 10 (warmest), every whole number in between.
 * Pure data and simple rules only: no Swing, JSON or API code here.
 */
public class ClothingItem {
    private static final String DRESS = "Dress";

    private final String id;
    private final String name;
    private final Category category;
    private final String subCategory;
    private final String color;
    private final String style;
    private final boolean layer;
    private final List<Integer> warmthLevels;
    private final String imagePath;

    /**
     * Makes a clothing item.
     *
     * @param id unique id, e.g. "c001"
     * @param name what the user called it, e.g. "Pink Dress Shirt"
     * @param category TOP or BOTTOM
     * @param subCategory e.g. "Long-sleeved shirt", "Mini skirt"
     * @param color e.g. "Pink"
     * @param style e.g. "Feminine"
     * @param layer true if it can be worn over another top (cardigan, jacket)
     * @param warmthLevels the warmth levels it can be worn at, coldest first
     * @param imagePath relative path to the saved picture
     */
    public ClothingItem(String id, String name, Category category, String subCategory,
                        String color, String style, boolean layer, List<Integer> warmthLevels,
                        String imagePath) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.subCategory = subCategory;
        this.color = color;
        this.style = style;
        this.layer = layer;
        this.warmthLevels = new ArrayList<>(warmthLevels);
        this.imagePath = imagePath;
    }

    /**
     * A dress is a top that doesn't need a bottom.
     *
     * @return true if this item is a dress
     */
    public boolean isDress() {
        return category == Category.TOP && DRESS.equalsIgnoreCase(subCategory);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public String getColor() {
        return color;
    }

    public String getStyle() {
        return style;
    }

    public boolean isLayer() {
        return layer;
    }

    /**
     * The warmth levels this item can be worn at.
     *
     * @return a copy of the list, coldest first, e.g. [2, 3, 4, 5, 6]
     */
    public List<Integer> getWarmthLevels() {
        return new ArrayList<>(warmthLevels);
    }

    public String getImagePath() {
        return imagePath;
    }
}
