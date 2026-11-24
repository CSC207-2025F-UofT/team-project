package use_case.add_pantry_item;

import java.time.LocalDate;

/**
 * Input data for adding an item to the pantry.
 */
public final class AddPantryItemInputData {
    private final String ingredientName;
    private final LocalDate expiryDate;

    /**
     * Creates input data.
     *
     * @param ingredientName the ingredient to add
     * @param expiryDate the optional expiry date
     */
    public AddPantryItemInputData(String ingredientName, LocalDate expiryDate) {
        this.ingredientName = ingredientName;
        this.expiryDate = expiryDate;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }
}
