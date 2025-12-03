package interface_adapter.signup;

import interface_adapter.ViewModel;

/**
 * The ViewModel for the Signup View.
 *
 * Manages the state of the signup form and notifies
 * observers when the state changes.
 */
public class SignupViewModel extends ViewModel<SignupState> {

    /**
     * Constructs a SignupViewModel.
     * Initializes with the view name "sign up" and an empty state.
     */
    public SignupViewModel() {
        super("sign up");  // The view name (must match SignupView's viewName!)
        setState(new SignupState());
    }
}