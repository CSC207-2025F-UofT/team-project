package interface_adapter.create_teams;

import java.util.ArrayList;
import java.util.List;

/**
 * The View Model for the Game Running View.
 */
public class MainMenuViewModel {
    private final List<MainMenuViewModelListener> listeners = new ArrayList<>();

    public static final String VIEW_NAME = "main menu";

    private MainMenuState state = new MainMenuState();

    public MainMenuState getState() {
        return state;
    }

    public void setState(MainMenuState state) {
        this.state = state;
    }

    public void addListener(MainMenuViewModelListener listener) {
        listeners.add(listener);
    }

    public void notifyStateChanged() {
        for (MainMenuViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }
}
