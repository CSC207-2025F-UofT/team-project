package org.example.cocktailcraft.use_case.favourite;

public class FavouriteOutputData {

    private final boolean isFavourite;

    public FavouriteOutputData(boolean isFavourite) {
        this.isFavourite = isFavourite;
    }

    public boolean getIsFavourite() {
        return isFavourite;
    }
}
