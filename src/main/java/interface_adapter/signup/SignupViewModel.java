package interface_adapter.signup;

import interface_adapter.ViewModel;

/**
 * Stores the state of the signup screen.
 */
public class SignupViewModel extends ViewModel<SignupViewModel.SignupState> {

    public static final String VIEW_NAME = "signup";

    public SignupViewModel() {
        super(VIEW_NAME);
        setState(new SignupState());
    }

    public static class SignupState {

        private String username = "";
        private String errorMessage = "";

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }
}
