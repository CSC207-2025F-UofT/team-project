package interface_adapter.adopter_signup;

import interface_adapter.ViewManagerModel;
import interface_adapter.adopter_login.AdopterLoginState;
import interface_adapter.adopter_login.AdopterLoginViewModel;
import use_case.adopter_signup.AdopterSignupOutputBoundary;
import use_case.adopter_signup.AdopterSignupOutputData;

/**
 * On success: goes to the login screen with the new username filled in.
 * On failure: shows the error on the signup screen.
 */
public class AdopterSignupPresenter implements AdopterSignupOutputBoundary {
    private final ViewManagerModel viewManagerModel;
    private final AdopterSignupViewModel signupViewModel;
    private final AdopterLoginViewModel loginViewModel;

    public AdopterSignupPresenter(ViewManagerModel viewManagerModel,
                                  AdopterSignupViewModel signupViewModel,
                                  AdopterLoginViewModel loginViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.signupViewModel = signupViewModel;
        this.loginViewModel = loginViewModel;
    }

    @Override
    public void prepareSuccessView(AdopterSignupOutputData outputData) {
        final AdopterLoginState loginState = loginViewModel.getState();
        loginState.setUsername(outputData.getUsername());
        loginState.setError(null);
        loginState.setNotice("Account created! Please log in.");
        loginViewModel.firePropertyChanged();

        final AdopterSignupState signupState = signupViewModel.getState();
        signupState.setUsername("");
        signupState.setError(null);
        signupViewModel.firePropertyChanged();

        viewManagerModel.switchTo(loginViewModel.getViewName());
    }

    @Override
    public void prepareFailView(String errorMessage) {
        final AdopterSignupState state = signupViewModel.getState();
        state.setError(errorMessage);
        signupViewModel.firePropertyChanged();
    }
}
