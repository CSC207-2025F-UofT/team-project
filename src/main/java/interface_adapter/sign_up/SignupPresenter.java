package interface_adapter.sign_up;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import interface_adapter.ViewManagerModel;
import interface_adapter.user_login.UserLoginState;
import interface_adapter.user_login.UserLoginViewModel;
import use_case.signup.SignupOutputBoundary;
import use_case.signup.SignupOutputData;

/**
 * The Presenter for the Signup Use Case.
 */
public class SignupPresenter implements SignupOutputBoundary {

    private final SignupViewModel signupViewModel;
    private final UserLoginViewModel loginViewModel;
    private final ViewManagerModel viewManagerModel;

    public SignupPresenter(ViewManagerModel viewManagerModel,
                           SignupViewModel signupViewModel,
                           UserLoginViewModel loginViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.signupViewModel = signupViewModel;
        this.loginViewModel = loginViewModel;
    }

    @Override
    public void prepareSuccessView(SignupOutputData response) {
        // On success, switch to the login view.
        JOptionPane.showMessageDialog(
                null,
                "Sign up successful!\nPlease log in with your new account.",
                "Success",
                JOptionPane.INFORMATION_MESSAGE
        );
        SwingUtilities.invokeLater(() -> {
	        final UserLoginState loginState = loginViewModel.getState();
	        loginState.setUsername(response.getUsername());
	        loginViewModel.firePropertyChange();
	
	        viewManagerModel.setState("login"); 
	        viewManagerModel.setWindowTitle("User Login");
	        viewManagerModel.firePropertyChange();
        });
    }

    @Override
    public void prepareFailView(String error) {
        final SignupState signupState = signupViewModel.getState();
        signupState.setUsernameError(error);
        signupViewModel.firePropertyChange();
    }

    @Override
    public void switchToLoginView() {
        viewManagerModel.setState(loginViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareGoBackView(String viewName) {
        viewManagerModel.setState(viewName);
        viewManagerModel.setWindowTitle("Pet Adoption System");
        viewManagerModel.firePropertyChange();
    }
}
