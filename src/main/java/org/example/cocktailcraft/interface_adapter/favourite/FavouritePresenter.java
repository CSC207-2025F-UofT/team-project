package org.example.cocktailcraft.interface_adapter.favourite;

import org.example.cocktailcraft.use_case.favourite.FavouriteOutputBoundary;
import org.example.cocktailcraft.use_case.favourite.FavouriteOutputData;

public class FavouritePresenter implements FavouriteOutputBoundary {
    private final FavouriteViewModel favouriteViewModel;
    public FavouritePresenter(FavouriteViewModel favouriteViewModel) {
        this.favouriteViewModel = favouriteViewModel;
    }

    public void prepareSuccessView(FavouriteOutputData outputData) {
        FavouriteState state = favouriteViewModel.getState();

        state.setFavourite(outputData.getIsFavourite());
        state.setFavouriteError(null);

        favouriteViewModel.setState(state);
        favouriteViewModel.notifyStateChanged();

    }

    public void prepareFailView(String errorMessage) {
        FavouriteState state = favouriteViewModel.getState();

        state.setFavouriteError(errorMessage);

        favouriteViewModel.setState(state);
        favouriteViewModel.notifyStateChanged();
    }
}
