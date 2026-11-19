package use_case.update_stock_level;

import entity.Pantry;

/**
 * Loads and saves the pantry when its stock levels change.
 */
public interface UpdateStockLevelDataAccessInterface {

    /**
     * Returns the current pantry.
     *
     * @return the pantry, never {@code null}
     */
    Pantry loadPantry();

    /**
     * Saves the updated pantry.
     *
     * @param pantry the pantry to save
     */
    void savePantry(Pantry pantry);
}
