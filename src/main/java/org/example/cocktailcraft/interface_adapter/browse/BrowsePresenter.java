package org.example.cocktailcraft.interface_adapter.browse;

import org.example.cocktailcraft.use_case.browse.BrowseOutputBoundary;
import org.example.cocktailcraft.use_case.browse.BrowseOutputData;

public class BrowsePresenter implements BrowseOutputBoundary {
    private final BrowseViewModel viewModel;

    public BrowsePresenter(BrowseViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(BrowseOutputData outputData) {
        // TODO(Wenqian): map real output to state and request navigation if needed.
        throw new UnsupportedOperationException("Success mapping is not implemented: Browse");
    }

    @Override
    public void prepareFailView(String message) {
        viewModel.getState().setMessage(message);
        viewModel.notifyStateChanged();
    }
}
