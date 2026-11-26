package interface_adapter.add_clothing;

import entity.ClothingOptions;
import use_case.add_clothing.AddClothingInputBoundary;
import use_case.add_clothing.AddClothingInputData;

import java.util.Locale;

/**
 * Called by the Add Clothing screen when the user clicks Save, Cancel or Home.
 * Turns the raw form values into AddClothingInputData and starts the Interactor.
 */
public class AddClothingController {
    private final AddClothingInputBoundary interactor;

    /**
     * Connects the controller to the interactor.*/
    public AddClothingController(AddClothingInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Save was clicked.
     *
     * @param name text in the Name box
     * @param categoryLabel "Top", "Bottom", "Shoes" or "Choose…"
     * @param subCategory picked type, or "Choose…"
     * @param color picked colour, or "Choose…"
     * @param style picked style, or "Choose…"
     * @param layer Layer: Yes
     * @param lowStop the weather slider's left handle (0 = coldest stop)
     * @param highStop the weather slider's right handle
     * @param imageSourcePath the picked picture's path ("" if none)
     */
    public void execute(String name, String categoryLabel, String subCategory, String color,
                        String style, boolean layer, int lowStop, int highStop,
                        String imageSourcePath) {
        interactor.execute(new AddClothingInputData(
                name,
                clean(categoryLabel).toUpperCase(Locale.ROOT),
                clean(subCategory),
                clean(color),
                clean(style),
                layer,
                ClothingOptions.warmthLevelsBetween(lowStop, highStop),
                imageSourcePath == null ? "" : imageSourcePath));
    }

    /** Cancel or Home was clicked. */
    public void switchToWelcomeView() {
        interactor.switchToWelcomeView();
    }

    /** Turns the "Choose…" placeholder into an empty answer. */
    private static String clean(String value) {
        if (value == null || AddClothingViewModel.CHOOSE.equals(value)) {
            return "";
        }
        return value;
    }
}
