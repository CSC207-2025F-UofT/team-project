package org.example.cocktailcraft.use_case.click;

import org.example.cocktailcraft.entity.Cocktail;

public interface ClickDataAccessInterface {
    /**
     * Returns the user with the given username.
     * @param cocktailId the username to look up
     * @return the cocktail with the given cocktailId
     */
    Cocktail get(String cocktailId);

    /**
     * Checks if the given cocktailId exists.
     * @param cocktailId the cocktailId to look for
     * @return true if a user with the given cocktailId exists; false otherwise
     */
    boolean existsByID(String cocktailId);
}
