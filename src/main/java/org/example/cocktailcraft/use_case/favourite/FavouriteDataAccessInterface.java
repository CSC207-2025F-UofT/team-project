package org.example.cocktailcraft.use_case.favourite;

import org.example.cocktailcraft.entity.User;

public interface FavouriteDataAccessInterface {
    /**
     * Saves the user.
     * @param user the user to save
     */
    void save(User user);

    /**
     * Returns the user with the given username.
     * @param userId the username to look up
     * @return the user with the given userId
     */
    User get(String userId);
}
