package org.example.cocktailcraft.interface_adapter.favourite;

import java.util.HashMap;
import java.util.Map;

public class FavouriteState {
    private boolean isFavourite = false;
    private String favouriteError;

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean isFavourite) {
        this.isFavourite = isFavourite;
    }

    public String getFavouriteError() {
        return favouriteError;
    }

    public void setFavouriteError(String favouriteError) {
        this.favouriteError = favouriteError;
    }
}