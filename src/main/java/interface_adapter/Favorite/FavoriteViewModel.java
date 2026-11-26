package interface_adapter.Favorite;

import java.util.ArrayList;
import java.util.List;

/** Holds the result state that the view displays after a favourite request. */
public class FavoriteViewModel {
    private FavoriteState state = new FavoriteState("", 0, "", false);
    private final List<FavoriteViewModelListener> listeners = new ArrayList<>();

    public FavoriteState getState() {
        return state;
    }

    public void setState(FavoriteState state) {
        if (state == null) {
            throw new IllegalArgumentException("Favorite state cannot be null");
        }
        this.state = state;
        for (FavoriteViewModelListener listener : new ArrayList<>(listeners)) {
            listener.onFavoriteStateChanged(state);
        }
    }

    public void addListener(FavoriteViewModelListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeListener(FavoriteViewModelListener listener) {
        listeners.remove(listener);
    }
}
