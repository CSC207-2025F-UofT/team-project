package interface_adapter.enter_name.welcomeMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * The view model for the main menu (MainMenuView): holds the greeting's username.
 */
public class WelcomeViewModel {
    private final List<WelcomeViewModelListener> listeners = new ArrayList<>();

    public static final String VIEW_NAME = "main menu";

    private WelcomeState state = new WelcomeState();

    public WelcomeState getState() {
        return this.state;
    }

    public void setState(WelcomeState state) {
        this.state = state;
    }

    public void addListener(WelcomeViewModelListener listener) {
        listeners.add(listener);
    }

    public void notifyStateChanged() {
        for (WelcomeViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }
}
