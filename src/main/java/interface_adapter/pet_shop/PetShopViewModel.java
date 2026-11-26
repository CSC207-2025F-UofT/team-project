package interface_adapter.pet_shop;

import java.util.ArrayList;
import java.util.List;

public class PetShopViewModel {
    public static final String VIEW_NAME = "pet shop";

    private PetShopState state = new PetShopState();

    private final List<PetShopViewModelListener> listeners =
            new ArrayList<>();

    public PetShopState getState() {
        return state;
    }

    public void setState(PetShopState state) {
        this.state = state;
    }

    public void addListener(PetShopViewModelListener listener) {
        listeners.add(listener);
    }

    public void notifyStateChanged() {
        for (PetShopViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }
}