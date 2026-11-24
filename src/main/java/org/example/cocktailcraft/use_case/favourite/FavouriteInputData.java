package org.example.cocktailcraft.use_case.favourite;

import org.example.cocktailcraft.entity.User;

public class FavouriteInputData {

    private final String cocktailID;
    private final User user;

    public FavouriteInputData(User user, String cocktailID) {
        this.user = user;
        this.cocktailID = cocktailID;
    }

    String getCocktailID() {
        return cocktailID;
    }

    User getUser() {return user;}
}
