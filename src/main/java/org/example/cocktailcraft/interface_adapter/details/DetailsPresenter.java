package org.example.cocktailcraft.interface_adapter.details;

import org.example.cocktailcraft.use_case.details.DetailsOutputBoundary;
import org.example.cocktailcraft.use_case.details.DetailsOutputData;

public class DetailsPresenter implements DetailsOutputBoundary {
    private final DetailsViewModel viewModel;

    public DetailsPresenter(DetailsViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(DetailsOutputData outputData) {
        // TODO(Wenqian): map real output to state and request navigation if needed.
        throw new UnsupportedOperationException("Success mapping is not implemented: Details");
    }

    @Override
    public void prepareFailView(String message) {
        viewModel.getState().setMessage(message);
        viewModel.notifyStateChanged();
    }
}
