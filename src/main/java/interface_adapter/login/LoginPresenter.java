package interface_adapter.login;

import use_case.login.LoginOutputBoundary;
import use_case.login.LoginOutputData;

public final class LoginPresenter implements LoginOutputBoundary {
    private final LoginViewModel viewModel;
    public LoginPresenter(LoginViewModel viewModel) { this.viewModel = viewModel; }
    @Override
    public void prepareSuccessView(LoginOutputData outputData) {
        viewModel.setSuccess(outputData.getUserProfileId(), outputData.getDisplayName());
    }
    @Override
    public void prepareFailView(String error) { viewModel.setFailure(error); }
}
