package use_case.add_pantry_item;

import java.time.LocalDate;

import entity.StockLevel;

/**
 * Output data for adding an item to the pantry.
 */
public final class AddPantryItemOutputData {
    private final String ingredientName;
    private final LocalDate expiryDate;
    private final StockLevel stockLevel;
    private final String message;
    private final boolean useCaseFailed;

    /**
     * Creates output data.
     *
     * @param ingredientName the added ingredient, or {@code null} on failure
     * @param expiryDate the optional expiry date
     * @param stockLevel the initial stock level, or {@code null} on failure
     * @param message the message to present
     * @param useCaseFailed whether the add operation failed
     */
    public AddPantryItemOutputData(String ingredientName, LocalDate expiryDate,
                                   StockLevel stockLevel, String message,
                                   boolean useCaseFailed) {
        this.ingredientName = ingredientName;
        this.expiryDate = expiryDate;
        this.stockLevel = stockLevel;
        this.message = message;
        this.useCaseFailed = useCaseFailed;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public StockLevel getStockLevel() {
        return stockLevel;
    }

    public String getMessage() {
        return message;
    }

    public boolean isUseCaseFailed() {
        return useCaseFailed;
    }
}
