package interface_adapter.get_deck.study_view;

import java.util.List;
import java.util.Map;

/**
 * The state for the GetStudy View Model.
 */
public class StudyState {
    private String deckName = "";
    private List<Map<String, String>> flashcards;
    private int currentIndex = 0;
    private boolean isFlipped = false;

    private String viewName = "studyView";

    private String deckError;

    // getters
    public String getDeckName() { return deckName; }
    public List<Map<String, String>> getFlashcards() { return flashcards; }
    public String getViewName() { return viewName; }
    public String getDeckError() { return deckError; }
    public boolean getIsFlipped() { return isFlipped; }

    // setters
    public void setDeckName(String deckName) { this.deckName = deckName; }
    public void setFlashcards(List<Map<String, String>> flashcards) { this.flashcards = flashcards; }
    public void setDeckError(String deckError) { this.deckError = deckError; }
    public void setViewName(String viewName) { this.viewName = viewName; }

    @Override
    public String toString() {
        return "GetDeckState{"
                + "deckName='" + deckName + '\''
                + "flashcards='" + flashcards + '\''
                + '}';
    }

    // update state of the flashcards
    public void flipCard() { isFlipped = !isFlipped; }

    public void nextCard() {
        currentIndex = (currentIndex + 1) % flashcards.size();
        isFlipped = false;
    }

    public void lastCard() {
        currentIndex = (currentIndex - 1 + flashcards.size()) % flashcards.size();
        isFlipped = false;
    }

    // reset studying flashcards
    public void resetStudy() {
        currentIndex = 0;
        isFlipped = false;
    }

    // computes what the view should display
    public String getWhichSideLabel() { return isFlipped ? "ANSWER" : "QUESTION"; }

    public String getCurrentCardText() {
        // TODO: make this a class instead of using a map
        Map<String, String> current = flashcards.get(currentIndex);
        Map.Entry<String, String> entry = current.entrySet().iterator().next();
        return isFlipped ? entry.getValue() : entry.getKey();
    }

    public String getProgressText() {
        return String.format("Card %d of %d", currentIndex + 1, flashcards.size());
    }

    public String getCardCountText() { return flashcards.size() + " cards"; }

}
