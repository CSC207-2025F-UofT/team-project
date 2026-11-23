package interface_adapter.admin_signup;

import java.util.ArrayList;
import java.util.List;

/**
 * The ViewModel for the Admin Signup View.
 */
public class AdminSignupViewModel {

    public static final String TITLE_LABEL = "Sign Up View";
    public static final String USERNAME_LABEL = "Choose username";
    public static final String ID_LABEL = "Choose ID";
    public static final String EMAIL_LABEL = "Choose email";
    public static final String PASSWORD_LABEL = "Choose password";
    public static final String REPEAT_PASSWORD_LABEL = "Enter password again";
    public static final String INVITE_CODE_LABEL = "Enter invite code";

    public static final String SIGNUP_BUTTON_LABEL = "Sign up";
    public static final String CANCEL_BUTTON_LABEL = "Cancel";

    public static final String TO_LOGIN_BUTTON_LABEL = "Go to Login";

    private final List<AdminSignupViewModelListener> listeners = new ArrayList<>();

    public static final String VIEW_NAME = "admin sign up";

    private AdminSignupState state = new AdminSignupState();

    public AdminSignupState getState() {
        return this.state;
    }

    public void setState(AdminSignupState state) {
        this.state = state;
    }

    public void addListener(AdminSignupViewModelListener listener) {
        listeners.add(listener);
    }

    public void notifyStateChanged() {
        for (AdminSignupViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }
}
