package interface_adapter.opengame;

import entity.GameState;
import java.util.ArrayList;
import java.util.List;

public class OpenGameViewModel {

    private String message;
    private GameState state;

    // Listeners manually stored (no java.beans)
    private final List<Runnable> listeners = new ArrayList<>();

    public void addListener(Runnable listener) {
        listeners.add(listener);
    }

    public void firePropertyChange() {
        for (Runnable r : listeners) {
            r.run();
        }
    }

    // --- Getters ---
    public String getMessage() {
        return message;
    }

    public GameState getState() {
        return state;
    }

    // --- Setters ---
    public void setMessage(String message) {
        this.message = message;
    }

    public void setState(GameState state) {
        this.state = state;
    }
}
