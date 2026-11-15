package interface_adapter.get_all_decks;

import java.util.ArrayList;
import java.util.List;

/**
 * The View Model for the GetAllDecks View.
 */
public class GetAllDecksViewModel {

    private final List<GetAllDecksViewModelListener> listeners = new ArrayList<>();

    public static final String VIEW_NAME = "get all decks";

    private GetAllDecksState state = new GetAllDecksState();

    public GetAllDecksState getState() {
        return this.state;
    }

    public void setState(GetAllDecksState state) {
        this.state = state;
    }

    public void addListener(GetAllDecksViewModelListener listener) {
        listeners.add(listener);
    }

    public void notifyStateChanged() {
        for (GetAllDecksViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }

}
