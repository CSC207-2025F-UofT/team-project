package interface_adapter.add_clothing;

import entity.Category;
import entity.ClothingOptions;
import interface_adapter.ViewModel;
import interface_adapter.ViewNames;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Holds the Add Clothing screen's state, plus the dropdown choices it shows,
 * and tells the View to redraw when something changes.
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
     * picking category from dropdown and choices within category
     */
    public List<String> getCategoryChoices() {
        return Arrays.asList(CHOOSE, TOP, BOTTOM);
    }

    /**
     * Type dropdown choices for the picked category, with "Choose…" first. and then the choices inside the category
     *
     *  categoryLabel "Top", "Bottom" or "Shoes" (anything else gives just "Choose…")
     *  the choices
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
     * colour dropdown choices, with "Choose…" first hehe
     */
    public List<String> getColorChoices() {
        return withChoose(ClothingOptions.COLORS);
    }

    /**
     * Style dropdown choices, with "Choose…" first.
     *
     * @return the choices
     */
    public List<String> getStyleChoices() {
        return withChoose(ClothingOptions.STYLES);
    }

    private static List<String> withChoose(List<String> options) {
        final List<String> choices = new ArrayList<>();
        choices.add(CHOOSE);
        choices.addAll(options);
        return choices;
    }
}
