package entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * The fixed lists the whole app shares: colours, styles, sub-categories,
 * and the warmth levels on the weather slider (-10 to 10).
 * Add Clothing fills its dropdowns from here; the questionnaire and closet
 * filters should too, so the names always match exactly.
 */
public final class ClothingOptions {
    public static final List<String> COLORS = Arrays.asList(
            "Black", "White", "Grey", "Beige", "Brown", "Red", "Pink", "Yellow",
            "Green", "Light green", "Dark green", "Blue", "Light blue", "Dark blue", "Purple");

    public static final List<String> STYLES = Arrays.asList(
            "Casual", "Streetwear", "Sporty", "Goth", "Feminine", "Masculine",
            "Preppy", "Y2K", "Minimalist", "Formal");

    public static final List<Integer> WARMTH_LEVELS = Arrays.asList(
            -10, -9, -8, -7, -6, -5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

    private static final List<String> TOP_TYPES = Arrays.asList(
            "T-shirt", "Cropped shirt", "Long-sleeved shirt", "Tank", "Sweater",
            "Cardigan", "Jacket", "Dress");

    private static final List<String> BOTTOM_TYPES = Arrays.asList(
            "Shorts", "Mini skirt", "Midi skirt", "Long skirt", "Leggings",
            "Baggy jeans", "Skinny jeans", "Pants");

    private ClothingOptions() {
    }

    /**
     * The sub-category choices for one category.
     *
     * @param category TOP or BOTTOM
     * @return the choices, in display order
     */
    public static List<String> subCategoriesFor(Category category) {
        if (category == Category.TOP) {
            return TOP_TYPES;
        }
        return BOTTOM_TYPES;
    }

    /**
     * Every warmth level from one slider handle to the other, both included.
     * Example: stops 12 to 15 (levels 2 to 5) give [2, 3, 4, 5].
     *
     * @param lowStop the left handle's position (0 = level -10)
     * @param highStop the right handle's position
     * @return the warmth levels in that range, coldest first
     */
    public static List<Integer> warmthLevelsBetween(int lowStop, int highStop) {
        return new ArrayList<>(WARMTH_LEVELS.subList(lowStop, highStop + 1));
    }

}
