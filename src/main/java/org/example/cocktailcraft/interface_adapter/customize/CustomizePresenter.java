package org.example.cocktailcraft.interface_adapter.customize;

import org.example.cocktailcraft.use_case.customize.CustomizeOutputBoundary;
import org.example.cocktailcraft.use_case.customize.CustomizeOutputData;

public class CustomizePresenter implements CustomizeOutputBoundary {
    private final CustomizeViewModel viewModel;

    public CustomizePresenter(CustomizeViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(CustomizeOutputData outputData) {
        // TODO(Enhao): map real output to state and request navigation if needed.
        throw new UnsupportedOperationException("Success mapping is not implemented: Customize");
    }

    @Override
    public void prepareFailView(String message) {
        viewModel.getState().setMessage(message);
        viewModel.notifyStateChanged();
    }
}
