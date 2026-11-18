package interface_adapter.get_deck.deck_view;

import java.util.ArrayList;
import java.util.List;

/**
 * The View Model for the Deck View.
 */
public class GetDeckViewModel {

    private final List<GetDeckViewModelListener> listeners = new ArrayList<>();

    public static final String VIEW_NAME = "deck view";

    private GetDeckState state = new GetDeckState();

    public GetDeckState getState() { return this.state; }
    public void setState(GetDeckState state) { this.state = state; }

    public void addListener(GetDeckViewModelListener listener) { listeners.add(listener); }

    public void notifyStateChanged() {
        for (GetDeckViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }
}
