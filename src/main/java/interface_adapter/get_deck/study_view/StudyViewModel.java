package interface_adapter.get_deck.study_view;

import interface_adapter.get_deck.deck_view.GetDeckViewModelListener;

import java.util.ArrayList;
import java.util.List;

/**
 * The View Model for the Study View.
 */
public class StudyViewModel {

    private final List<StudyViewModelListener> listeners = new ArrayList<>();

    public static final String VIEW_NAME = "study view";
    private StudyState state = new StudyState();

    public StudyState getState() { return this.state; }
    public void setState(StudyState state) { this.state = state; }

    public void addListener(StudyViewModelListener listener) { listeners.add(listener); }

    public void notifyStateChanged() {
        for (StudyViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }

    // GUI state changes
    public void flipCard() {
        state.flipCard();
        notifyStateChanged();
    }

    public void nextCard() {
        state.nextCard();
        notifyStateChanged();
    }

    public void lastCard() {
        state.lastCard();
        notifyStateChanged();
    }

    public void reset() {
        state.resetStudy();
        notifyStateChanged();
    }
}
