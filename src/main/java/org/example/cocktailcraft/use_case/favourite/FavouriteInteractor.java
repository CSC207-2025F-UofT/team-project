package org.example.cocktailcraft.use_case.favourite;

import org.example.cocktailcraft.entity.User;

public class FavouriteInteractor implements FavouriteInputBoundary{
    private final FavouriteDataAccessInterface favouriteDataAccessObject;
    private final FavouriteOutputBoundary favouritePresenter;

    public FavouriteInteractor(FavouriteDataAccessInterface favouriteDataAccessObject, FavouriteOutputBoundary favouritePresenter) {
        this.favouriteDataAccessObject = favouriteDataAccessObject;
        this.favouritePresenter = favouritePresenter;
    }

    @Override
    public void execute(FavouriteInputData favouriteInputData) {
        final User user = favouriteInputData.getUser();
        final String cocktailID = favouriteInputData.getCocktailID();
        if (!favouriteDataAccessObject.existsByID(cocktailID)) {
            favouritePresenter.prepareFailView(cocktailID + ": Cocktail does not exist.");
        }
        else {
            user.favourite(cocktailID);
            favouriteDataAccessObject.save(user);

            final FavouriteOutputData favouriteOutputData = new FavouriteOutputData(user.isFavourite(cocktailID));
            favouritePresenter.prepareSuccessView(favouriteOutputData);
            }
        }
    }
