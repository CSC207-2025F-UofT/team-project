package interface_adapter.register;

import use_case.register.RegisterOutputBoundary;

public class RegisterPresenter implements RegisterOutputBoundary {

    private final RegisterViewModel registerViewModel;

    public RegisterPresenter(RegisterViewModel registerViewModel) {
        this.registerViewModel = registerViewModel;
    }

    public void prepareFailView(String error){
        RegisterState registerState = registerViewModel.getState();
        registerState.setErrorMessage(error);

        registerViewModel.notifyStateChanged();
    }
}
