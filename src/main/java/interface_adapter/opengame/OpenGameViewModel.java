package interface_adapter.opengame;
import entity.GameState;

import java.util.ArrayList;
import java.util.List;
// TODO: change the view model
// TODO: not sure if u need the state for open game

public class OpenGameViewModel {
    private String message;
    private GameState state;
    private final List<Runnable> listeners = new ArrayList<>();

    // Add a listener (the View will register itself)
    public void addListener(Runnable listener) {
        listeners.add(listener);
    }
    // Notify ALL listeners that data changed
    private void notifyListeners() {
        for (Runnable r : listeners) {
            r.run();
        }
    }

    public String getMessage() {
        return message;
    }

    public GameState getState() {
        return state;
    }

    public void setMessage(String message) {
        this.message = message;
        notifyListeners();
    }

    public void setState(GameState state) {
        this.state = state;
        notifyListeners();
    }

}
