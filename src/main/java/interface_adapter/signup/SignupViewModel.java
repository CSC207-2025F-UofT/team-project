package interface_adapter.signup;

import interface_adapter.ViewModel;

/**
 * View model for the signup screen.
 */
public class SignupViewModel extends ViewModel<SignupState> {
    public static final String VIEW_NAME = "sign up";

    public SignupViewModel() {
        super(VIEW_NAME);
        setState(new SignupState());
    }
}
