package interface_adapter.add_clothing;

import entity.Category;
import entity.ClothingOptions;
import interface_adapter.ViewModel;
import interface_adapter.ViewNames;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Holds the Add Clothing screen's state, plus the dropdown choices and weather
 * slider labels it shows, and tells the View to redraw when something changes.
 */
public class AddClothingViewModel extends ViewModel<AddClothingState> {
    public static final String CHOOSE = "Choose!";
    public static final String TOP = "Top";
    public static final String BOTTOM = "Bottom";

    /** Starts with an empty state. */
    public AddClothingViewModel() {
        super(ViewNames.ADD_CLOTHING);
        setState(new AddClothingState());
    }

    /**
     * Category dropdown choices, with "Choose!" first.
     *
     * @return the choices
     */
    public List<String> getCategoryChoices() {
        return Arrays.asList(CHOOSE, TOP, BOTTOM);
    }

    /**
     * Type dropdown choices for the picked category, with "Choose!" first.
     *
     * @param categoryLabel "Top" or "Bottom" (anything else gives just "Choose!")
     * @return the choices
     */
    public List<String> getSubCategoryChoices(String categoryLabel) {
        final List<String> choices = new ArrayList<>();
        choices.add(CHOOSE);
        if (TOP.equals(categoryLabel)) {
            choices.addAll(ClothingOptions.subCategoriesFor(Category.TOP));
        }
        else if (BOTTOM.equals(categoryLabel)) {
            choices.addAll(ClothingOptions.subCategoriesFor(Category.BOTTOM));
        }
        return choices;
    }

    /**
     * Colour dropdown choices, with "Choose!" first.
     *
     * @return the choices
     */
    public List<String> getColorChoices() {
        return withChoose(ClothingOptions.COLORS);
    }

    /**
     * Style dropdown choices, with "Choose!" first.
     *
     * @return the choices
     */
    public List<String> getStyleChoices() {
        return withChoose(ClothingOptions.STYLES);
    }

    /**
     * The label under each weather slider tick: the warmth level on every tick,
     * and its temperature underneath on every other tick (-10, -8, ..., 10).
     *
     * @return one label per tick, coldest first
     */
    public List<String> getWeatherLabels() {
        final List<String> labels = new ArrayList<>();
        for (int level : ClothingOptions.WARMTH_LEVELS) {
            final String celsius;
            if (level % 2 == 0) {
                celsius = celsiusFor(level) + "°C";
            }
            else {
                celsius = "&nbsp;";
            }
            labels.add("<html><center>" + level + "<br>" + celsius + "</center></html>");
        }
        return labels;
    }

    /**
     * The sentence under the slider for the chosen range,
     * e.g. "Warmth 2 to 10 (about 14°C to 30°C)".
     *
     * @param lowStop the left handle's position
     * @param highStop the right handle's position
     * @return the description
     */
    public String describeRange(int lowStop, int highStop) {
        final int low = ClothingOptions.WARMTH_LEVELS.get(lowStop);
        final int high = ClothingOptions.WARMTH_LEVELS.get(highStop);
        if (low == high) {
            return "Warmth " + low + " (about " + celsiusFor(low) + "°C)";
        }
        return "Warmth " + low + " to " + high
                + " (about " + celsiusFor(low) + "°C to " + celsiusFor(high) + "°C)";
    }

    /**
     * Roughly what temperature a warmth level means, only to help the user choose:
     * each level is 2°C and level 0 is 10°C (-10 → -10°C, 5 → 20°C, 10 → 30°C).
     * Nothing outside this screen uses °C; the warmth level is what gets saved.
     */
    private static int celsiusFor(int level) {
        return 10 + 2 * level;
    }

    private static List<String> withChoose(List<String> options) {
        final List<String> choices = new ArrayList<>();
        choices.add(CHOOSE);
        choices.addAll(options);
        return choices;
    }
}
