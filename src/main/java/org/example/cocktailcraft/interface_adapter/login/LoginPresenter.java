package org.example.cocktailcraft.interface_adapter.login;

import org.example.cocktailcraft.use_case.login.LoginOutputBoundary;
import org.example.cocktailcraft.use_case.login.LoginOutputData;

public class LoginPresenter implements LoginOutputBoundary {
    private final LoginViewModel viewModel;

    public LoginPresenter(LoginViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(LoginOutputData outputData) {
        // TODO(Jiaqi): map real output to state and request navigation if needed.
        throw new UnsupportedOperationException("Success mapping is not implemented: Login");
    }

    @Override
    public void prepareFailView(String message) {
        viewModel.getState().setMessage(message);
        viewModel.notifyStateChanged();
    }
}
