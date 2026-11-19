package use_case.update_stock_level;

import entity.StockLevel;

/**
 * Input data for changing a pantry item's stock level.
 */
public final class UpdateStockLevelInputData {
    private final String ingredientName;
    private final StockLevel stockLevel;

    /**
     * Creates input data.
     *
     * @param ingredientName the ingredient to update
     * @param stockLevel the new stock level
     */
    public UpdateStockLevelInputData(String ingredientName, StockLevel stockLevel) {
        this.ingredientName = ingredientName;
        this.stockLevel = stockLevel;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public StockLevel getStockLevel() {
        return stockLevel;
    }
}
