package interface_adapter.signup;

import interface_adapter.ViewManagerModel;
import interface_adapter.login.LoginViewModel;
import use_case.signup.SignupOutputBoundary;
import use_case.signup.SignupOutputData;

/**
 * Presenter for the signup use case
 */
public class SignupPresenter implements SignupOutputBoundary {
    private final SignupViewModel signupViewModel;
    private final LoginViewModel loginViewModel;
    private final ViewManagerModel viewManagerModel;

    /**
     * Declare view models
     * @param signupViewModel trivial
     * @param loginViewModel trivial
     * @param viewManagerModel use view manager model
     */
    public SignupPresenter(ViewManagerModel viewManagerModel,
                           SignupViewModel signupViewModel,
                           LoginViewModel loginViewModel) {
        this.signupViewModel = signupViewModel;
        this.loginViewModel = loginViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    /**
     * Implement use case when it's a successful sign up
     * @param signupOutputData the output data
     */
    @Override
    public void prepareSuccessView(SignupOutputData signupOutputData) {
        signupViewModel.setState(new SignupState());
        final SignupState signupState = signupViewModel.getState();
        signupState.setSuccessMessage("Account Successfully Registered!");
        signupViewModel.firePropertyChange();
    }

    /**
     * Implement use case when it's not a successful sign up
     * @param errorMessage the explanation of the failure
     */
    @Override
    public void prepareFailView(String errorMessage) {
        final SignupState signupState = signupViewModel.getState();

        // determine which error it should be
        if (errorMessage.toLowerCase().contains("username") || errorMessage.toLowerCase().contains("user")) {
            signupState.setUsernameError(errorMessage);
            signupState.setPasswordError(" ");
        }
        else {
            signupState.setUsernameError(" ");
            signupState.setPasswordError(errorMessage);
        }
        signupState.setSuccessMessage(" ");
        signupViewModel.firePropertyChange();
    }

}
