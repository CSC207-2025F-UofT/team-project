package org.example.cocktailcraft.interface_adapter.favourite;

import org.example.cocktailcraft.entity.User;
import org.example.cocktailcraft.use_case.favourite.FavouriteInputBoundary;
import org.example.cocktailcraft.use_case.favourite.FavouriteInputData;

public class FavouriteController {
    private final FavouriteInputBoundary favouriteUseCaseInteractor;

    public FavouriteController(FavouriteInputBoundary favouriteUseCaseInteractor) {
        this.favouriteUseCaseInteractor = favouriteUseCaseInteractor;
    }

    /**
     * Executes the Favourite Use Case.
     * @param user the user
     * @param cocktailID the cocktail of the user favourite
     */
    public void execute(User user, String cocktailID) {
        final FavouriteInputData favouriteInputData = new FavouriteInputData(user, cocktailID);

        favouriteUseCaseInteractor.execute(favouriteInputData);
    }
}
