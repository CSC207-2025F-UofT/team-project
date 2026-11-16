package org.example.cocktailcraft.interface_adapter.logout;

import org.example.cocktailcraft.use_case.logout.LogoutOutputBoundary;
import org.example.cocktailcraft.use_case.logout.LogoutOutputData;

public class LogoutPresenter implements LogoutOutputBoundary {
    private final LogoutViewModel viewModel;

    public LogoutPresenter(LogoutViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(LogoutOutputData outputData) {
        // TODO(Jiaqi): map real output to state and request navigation if needed.
        throw new UnsupportedOperationException("Success mapping is not implemented: Logout");
    }

    @Override
    public void prepareFailView(String message) {
        viewModel.getState().setMessage(message);
        viewModel.notifyStateChanged();
    }
}
