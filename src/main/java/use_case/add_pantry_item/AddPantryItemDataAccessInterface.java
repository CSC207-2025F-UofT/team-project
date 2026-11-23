package use_case.add_pantry_item;

import entity.Pantry;

/**
 * Loads and saves the pantry when an item is added.
 */
public interface AddPantryItemDataAccessInterface {

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
