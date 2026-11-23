package interface_adapters.flashcard_generator;

import java.util.ArrayList;
import java.util.List;

/**
 * View model for the flashcard generator use case.
 */
public class FlashcardGeneratorViewModel {

    public static final String VIEW_NAME = "flashcard generator";
    private FlashcardGeneratorState state;
    private final List<FlashcardGeneratorViewModelListener> listeners;

    public FlashcardGeneratorViewModel(){
        this.state = new FlashcardGeneratorState("", false);
        this.listeners = new ArrayList<>();

    }

    /**
     * Sets the state for view model in flashcard generator use case
     * @param state the state we set view model to
     */
    public void setState(FlashcardGeneratorState state){
        this.state = state;

    }

    /**
     * Returns the state for view model in flashcard generator use case
     */
    public FlashcardGeneratorState getState(){
        return this.state;
    }

    /**
     * Add new listener to listeners in ViewModel
     */
    public void addListener(FlashcardGeneratorViewModelListener listener){
        listeners.add(listener);
    }

    /**
     * Notifies ViewModel that state changed
     */
    public void notifyStateChanged(){
        for (FlashcardGeneratorViewModelListener listener: listeners) {
            listener.stateChanged();
        }

    }
}
