package use_case.add_clothing;

import java.util.ArrayList;
import java.util.List;

/**
 * What the user typed and picked on the Add Clothing form.
 * The Controller packs it up and hands it to AddClothingInteractor.
 */
public class AddClothingInputData {
    private final String name;
    private final String category;
    private final String subCategory;
    private final String color;
    private final String style;
    private final boolean layer;
    private final List<Integer> warmthLevels;
    private final String imageSourcePath;

    /**
     * Packs the form's values.
     *
     * @param name item name
     * @param category "TOP" or "BOTTOM"
     * @param subCategory e.g. "Mini skirt"
     * @param color e.g. "Pink"
     * @param style e.g. "Feminine"
     * @param layer whether it can be layered
     * @param warmthLevels the warmth levels picked on the weather slider, coldest first
     * @param imageSourcePath where the picked picture is on the computer (empty if none)
     */
    public AddClothingInputData(String name, String category, String subCategory, String color,
                                String style, boolean layer, List<Integer> warmthLevels,
                                String imageSourcePath) {
        this.name = name;
        this.category = category;
        this.subCategory = subCategory;
        this.color = color;
        this.style = style;
        this.layer = layer;
        this.warmthLevels = new ArrayList<>(warmthLevels);
        this.imageSourcePath = imageSourcePath;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
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

    public List<Integer> getWarmthLevels() {
        return new ArrayList<>(warmthLevels);
    }

    public String getImageSourcePath() {
        return imageSourcePath;
    }
}
