package interface_adapter.add_clothing;

import use_case.add_clothing.AddClothingInputBoundary;
import use_case.add_clothing.AddClothingInputData;

import java.util.Locale;
import java.util.Objects;

/**
 * Called by the Add Clothing screen when the user clicks Save, Cancel or Home.
 * Turns the raw form values into AddClothingInputData and starts the Interactor.
 */
public class AddClothingController {
    private final AddClothingInputBoundary interactor;

    /**
     * Connects the controller to the interactor which does the ACTUAL work
     */
    public AddClothingController(AddClothingInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Save was clicked after
     * name text in the Name box
     * categoryLabel "Top", "Bottom", "Shoes" or "Choose…"
     * subCategory picked type, or "Choose…"
     * color picked colour, or "Choose…"
     * style picked style, or "Choose…"
     * Layer: Yes
     * weatherRating slider value, -10 to 10
     * imageSourcePath the picked picture's path ("" if none)
     */
    public void execute(String name, String categoryLabel, String subCategory, String color,
                        String style, boolean layer, int weatherRating, String imageSourcePath) {
        interactor.execute(new AddClothingInputData(
                name,
                clean(categoryLabel).toUpperCase(), // cuz the enum values are upper case
                clean(subCategory),
                clean(color),
                clean(style),
                layer,
                weatherRating,
                Objects.requireNonNullElse(imageSourcePath, ""))); // we can change this line
    }

    /** Cancel or Home was clicked. */
    public void switchToWelcomeView() {
        interactor.switchToWelcomeView();
    }

    /** Turns the "Choose…" placeholder into an empty answer. */
    private static String clean(String value) {
        if (value == null || Objects.equals(value, AddClothingViewModel.CHOOSE)) {
            return "";
        }
        return value;
    }
}
