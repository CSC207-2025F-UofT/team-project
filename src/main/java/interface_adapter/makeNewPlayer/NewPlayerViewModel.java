package interface_adapter.makeNewPlayer;

import java.util.ArrayList;
import java.util.List;

public class NewPlayerViewModel {
    public static final String TITLE_LABEL = "Sign Up View";
    public static final String USERNAME_LABEL = "Choose username";

    public static final String SIGNUP_BUTTON_LABEL = "Sign up";
    public static final String CANCEL_BUTTON_LABEL = "Cancel";

    public static final String TO_LOGIN_BUTTON_LABEL = "Go to Login";

    private final List<NewPlayerViewModelListener> listeners = new ArrayList<>();

    public static final String VIEW_NAME = "sign up";

    private NewPlayerState state = new NewPlayerState();

    public NewPlayerState getState() {
        return this.state;
    }

    public void setState(NewPlayerState state) {
        this.state = state;
    }

    public void addListener(NewPlayerViewModelListener listener) {
        listeners.add(listener);
    }

    public void notifyStateChanged() {
        for (NewPlayerViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }
}
