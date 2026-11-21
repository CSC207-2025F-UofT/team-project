package interface_adapters.flashcard_studying;

import java.util.ArrayList;
import java.util.List;

/**
 * The View Model for the FlashcardStudying View.
 */
public class FlashcardStudyingViewModel {

    private final List<FlashcardStudyingViewModelListener> listeners = new ArrayList<>();

    public static final String VIEW_NAME = "log in";

    private FlashcardStudyingState state = new FlashcardStudyingState();

    public FlashcardStudyingState getState() {
        return this.state;
    }

    public void setState(FlashcardStudyingState state) {
        this.state = state;
    }

    public void addListener(FlashcardStudyingViewModelListener listener) {
        listeners.add(listener);
    }

    public void notifyStateChanged() {
        for (FlashcardStudyingViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }

}
