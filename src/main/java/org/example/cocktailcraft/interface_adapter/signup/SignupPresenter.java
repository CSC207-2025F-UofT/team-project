package org.example.cocktailcraft.interface_adapter.signup;

import org.example.cocktailcraft.use_case.signup.SignupOutputBoundary;
import org.example.cocktailcraft.use_case.signup.SignupOutputData;

public class SignupPresenter implements SignupOutputBoundary {
    private final SignupViewModel viewModel;

    public SignupPresenter(SignupViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(SignupOutputData outputData) {
        // TODO(Jiaqi): map real output to state and request navigation if needed.
        throw new UnsupportedOperationException("Success mapping is not implemented: Signup");
    }

    @Override
    public void prepareFailView(String message) {
        viewModel.getState().setMessage(message);
        viewModel.notifyStateChanged();
    }
}
