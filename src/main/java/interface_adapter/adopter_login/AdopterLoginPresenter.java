package interface_adapter.adopter_login;

import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import use_case.adopter_login.AdopterLoginOutputBoundary;
import use_case.adopter_login.AdopterLoginOutputData;

/**
 * On success: goes to the logged-in screen as an adopter.
 * On failure: shows the error on the login screen.
 */
public class AdopterLoginPresenter implements AdopterLoginOutputBoundary {
    private final ViewManagerModel viewManagerModel;
    private final AdopterLoginViewModel loginViewModel;
    private final LoggedInViewModel loggedInViewModel;

    public AdopterLoginPresenter(ViewManagerModel viewManagerModel,
                                 AdopterLoginViewModel loginViewModel,
                                 LoggedInViewModel loggedInViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.loginViewModel = loginViewModel;
        this.loggedInViewModel = loggedInViewModel;
    }

    @Override
    public void prepareSuccessView(AdopterLoginOutputData outputData) {
        final LoggedInState loggedInState = loggedInViewModel.getState();
        loggedInState.setUsername(outputData.getUsername());
        loggedInState.setRole(LoggedInState.ROLE_ADOPTER);
        loggedInViewModel.firePropertyChanged();

        final AdopterLoginState loginState = loginViewModel.getState();
        loginState.setUsername("");
        loginState.setError(null);
        loginState.setNotice(null);
        loginViewModel.firePropertyChanged();

        viewManagerModel.switchTo(loggedInViewModel.getViewName());
    }

    @Override
    public void prepareFailView(String errorMessage) {
        final AdopterLoginState state = loginViewModel.getState();
        state.setError(errorMessage);
        state.setNotice(null);
        loginViewModel.firePropertyChanged();
    }
}
