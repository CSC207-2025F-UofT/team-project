package interface_adapter.LoggedIn;

import user_case.change_password.ChangePasswordOutputBoundary;
import user_case.change_password.ChangePasswordOutputData;

public class ChangePasswordPresenter implements ChangePasswordOutputBoundary {
    private final LoggedInViewModel loggedInViewModel;

    public ChangePasswordPresenter(LoggedInViewModel loggedInViewModel){
        this.loggedInViewModel = loggedInViewModel;
    }
    @Override
    public void PrepareSuccessView(ChangePasswordOutputData outputData){
        loggedInViewModel.getState().setPassword("");
        loggedInViewModel.getState().setPasswordError(null);
        loggedInViewModel.notifyPasswordChangeUpdate();
    }
    @Override
    public void PrepareFailView(String error){
        loggedInViewModel.getState().setPasswordError(error);
        loggedInViewModel.notifyPasswordChangeUpdate();
    }
}