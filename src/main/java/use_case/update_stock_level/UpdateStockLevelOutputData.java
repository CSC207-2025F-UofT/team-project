package use_case.update_stock_level;

import entity.StockLevel;

/**
 * Output data for changing a pantry item's stock level.
 */
public final class UpdateStockLevelOutputData {
    private final String ingredientName;
    private final StockLevel stockLevel;
    private final String message;
    private final boolean useCaseFailed;

    /**
     * Creates output data.
     *
     * @param ingredientName the updated ingredient, or {@code null} on failure
     * @param stockLevel the updated level, or {@code null} on failure
     * @param message the message to present
     * @param useCaseFailed whether the update failed
     */
    public UpdateStockLevelOutputData(String ingredientName, StockLevel stockLevel,
                                      String message, boolean useCaseFailed) {
        this.ingredientName = ingredientName;
        this.stockLevel = stockLevel;
        this.message = message;
        this.useCaseFailed = useCaseFailed;
    }

    public String getIngredientName() {
        return ingredientName;
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
