package interface_adapter.adopt_pet;

import java.util.ArrayList;
import java.util.List;

public class AdoptPetViewModel {

    public static final String VIEW_NAME = "adopt pet";

    private AdoptPetState state = new AdoptPetState();

    private final List<AdoptPetViewModelListener> listeners =
            new ArrayList<>();

    public AdoptPetState getState() {
        return state;
    }

    public void setState(AdoptPetState state) {
        this.state = state;
    }

    public void addListener(AdoptPetViewModelListener listener) {
        listeners.add(listener);
    }

    public void notifyStateChanged() {
        for (AdoptPetViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }
}