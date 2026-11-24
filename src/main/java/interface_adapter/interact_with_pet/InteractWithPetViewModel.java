package interface_adapter.interact_with_pet;

import java.util.ArrayList;
import java.util.List;

public class InteractWithPetViewModel {

    public static final String VIEW_NAME = "interact with pet";

    private InteractWithPetState state = new InteractWithPetState();

    private final List<InteractWithPetViewModelListener> listeners =
            new ArrayList<>();

    public InteractWithPetState getState() {
        return state;
    }

    public void setState(InteractWithPetState state) {
        this.state = state;
    }

    public void addListener(InteractWithPetViewModelListener listener) {
        listeners.add(listener);
    }

    public void notifyStateChanged() {
        for (InteractWithPetViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }
}
