package entity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
/**
 * The fixed lists of colors, styles and sub-categories the whole app uses.
 * Add Clothing fills its dropdowns from here, the questionnaire and closet
 * filters should too, so the names always match exactly.
 */
public class ClothingOptions {
    public static final List<String> COLORS = Arrays.asList(
            "Black", "White", "Grey", "Beige", "Brown", "Red", "Pink", "Yellow",
            "Green", "Light green", "Dark green", "Blue", "Light blue", "Dark blue", "Purple");

    public static final List<String> STYLES = Arrays.asList(
            "Casual", "Streetwear", "Sporty", "Goth", "Feminine", "Masculine",
            "Preppy", "Y2K", "Minimalist", "Formal");

    private static final List<String> TOP_TYPES = Arrays.asList(
            "T-shirt", "Cropped shirt", "Long-sleeved shirt", "Tank", "Sweater",
            "Cardigan", "Jacket", "Dress");

    private static final List<String> BOTTOM_TYPES = Arrays.asList(
            "Shorts", "Mini skirt", "Midi skirt", "Long skirt", "Leggings",
            "Baggy jeans", "Skinny jeans", "Pants");

    public static List<String> subCategoriesFor(Category category) {
        if (category == Category.TOP) {
            return TOP_TYPES;
        }
        return BOTTOM_TYPES;
    }
}
