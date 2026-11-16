package org.example.cocktailcraft.interface_adapter.favourite;

import org.example.cocktailcraft.use_case.favourite.FavouriteInputBoundary;
import org.example.cocktailcraft.use_case.favourite.FavouriteInputData;

public class FavouriteController {
    private final FavouriteInputBoundary interactor;

    public FavouriteController(FavouriteInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(String cocktailId) {
        interactor.execute(new FavouriteInputData(cocktailId));
    }
}
