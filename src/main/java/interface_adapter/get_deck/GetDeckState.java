package interface_adapter.get_deck;

import java.util.List;
import java.util.Map;

/**
 * The state for the GetDeck View Model.
 */
public class GetDeckState {
    private String deckName = "";
    private List<Map<String, String>> flashcards;
    private String deckError;
    private String viewName = "deckView";

    public String getDeckName() { return deckName; }

    public List<Map<String, String>> getFlashcards() { return flashcards; }

    public String getDeckError() { return deckError; }

    public String getViewName() { return viewName; }

    public void setDeckName(String deckName) { this.deckName = deckName; }

    public void setFlashcards(List<Map<String, String>> flashcards) { this.flashcards = flashcards; }

    public void setDeckError(String deckError) { this.deckError = deckError; }

    @Override
    public String toString() {
        return "GetDeckState{"
                + "deckName='" + deckName + '\''
                + "flashcards='" + flashcards + '\''
                + '}';
    }

    public void setViewName(String viewName) { this.viewName = viewName; }
}
