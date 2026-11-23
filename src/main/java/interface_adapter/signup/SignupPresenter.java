package interface_adapter.signup;

import interface_adapter.ViewManagerModel;
import use_case.signup.SignupOutputBoundary;
import use_case.signup.SignupOutputData;

/**
 * Presenter for the Signup use case.
 */
public class SignupPresenter implements SignupOutputBoundary {

    private final SignupViewModel signupViewModel;
    private final ViewManagerModel viewManagerModel;
    private final String loginViewName;

    public SignupPresenter(SignupViewModel signupViewModel,
                           ViewManagerModel viewManagerModel,
                           String loginViewName) {
        this.signupViewModel = signupViewModel;
        this.viewManagerModel = viewManagerModel;
        this.loginViewName = loginViewName;
    }

    @Override
    public void prepareSuccessView(SignupOutputData outputData) {
        SignupViewModel.SignupState state = signupViewModel.getState();

        state.setUsername(outputData.getUsername());
        state.setErrorMessage("");

        signupViewModel.setState(state);
        signupViewModel.firePropertyChanged();

        viewManagerModel.setState(loginViewName);
        viewManagerModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        SignupViewModel.SignupState state = signupViewModel.getState();

        state.setErrorMessage(errorMessage);

        signupViewModel.setState(state);
        signupViewModel.firePropertyChanged();
    }
}
