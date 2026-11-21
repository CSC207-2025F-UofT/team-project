package interface_adapter.removeBook;

import java.util.ArrayList;
import java.util.List;

/**
 * The ViewModel for removing a book from the bookshelf.
 */
public class RemoveBookViewModel {
    private final List<RemoveBookViewModelListener> listeners = new ArrayList<>();

    private RemoveBookState state = new RemoveBookState();

    public RemoveBookState getState() {
        return this.state;
    }

    public void setState(RemoveBookState state) {
        this.state = state;
    }

    public void addListener(RemoveBookViewModelListener listener) {
        listeners.add(listener);
    }

    public void notifyStateChanged() {
        for (RemoveBookViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }
}
