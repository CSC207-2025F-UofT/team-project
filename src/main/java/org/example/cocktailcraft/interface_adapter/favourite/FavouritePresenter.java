package org.example.cocktailcraft.interface_adapter.favourite;

import org.example.cocktailcraft.use_case.favourite.FavouriteOutputBoundary;
import org.example.cocktailcraft.use_case.favourite.FavouriteOutputData;

public class FavouritePresenter implements FavouriteOutputBoundary {
    private final FavouriteViewModel viewModel;

    public FavouritePresenter(FavouriteViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(FavouriteOutputData outputData) {
        // TODO(Wenqian): map real output to state and request navigation if needed.
        throw new UnsupportedOperationException("Success mapping is not implemented: Favourite");
    }

    @Override
    public void prepareFailView(String message) {
        viewModel.getState().setMessage(message);
        viewModel.notifyStateChanged();
    }
}
