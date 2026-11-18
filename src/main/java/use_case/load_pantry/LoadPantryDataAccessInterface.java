package use_case.load_pantry;

import entity.Pantry;

/**
 * Loads the user's saved pantry.
 */
public interface LoadPantryDataAccessInterface {

    /**
     * Returns the current pantry.
     *
     * @return the saved pantry, never {@code null}
     */
    Pantry loadPantry();
}
