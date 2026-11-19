package use_case.generate_shopping_list;

import entity.Pantry;

/**
 * Read-only access to the fridge. Implemented later by the pantry file DAO.
 */
public interface PantryReader {

    /**
     * Returns the current pantry contents.
     *
     * @return the pantry (never null)
     */
    Pantry getPantry();
}
