package interface_adapter.skills_list;

import java.util.ArrayList;
import java.util.List;

/**
 * The ViewModel for the Signup View.
 */
public class SkillListViewModel {

    public static final String TITLE_LABEL = "Sign Up View";
    public static final String USERNAME_LABEL = "Choose username";
    public static final String PASSWORD_LABEL = "Choose password";
    public static final String REPEAT_PASSWORD_LABEL = "Enter password again";

    public static final String SIGNUP_BUTTON_LABEL = "Sign up";
    public static final String CANCEL_BUTTON_LABEL = "Cancel";

    public static final String TO_LOGIN_BUTTON_LABEL = "Go to Login";

    private final List<SkillListViewModelListener> listeners = new ArrayList<>();

    public static final String VIEW_NAME = "sign up";

    private SkillListState state = new SkillListState();

    public SkillListState getState() {
        return this.state;
    }

    public void setState(SkillListState state) {
        this.state = state;
    }

    public void addListener(SkillListViewModelListener listener) {
        listeners.add(listener);
    }

    public void notifyStateChanged() {
        for (SkillListViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }
}
