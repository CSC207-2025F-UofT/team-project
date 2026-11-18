package interface_adapter.get_deck.deck_view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The state for the GetDeck View Model.
 */
public class GetDeckState {
    private String deckName = "";
    private List<Map<String, String>> flashcards = new ArrayList<>();

    private String viewName = "deckView";

    private String deckError;
    private String deckEmptyError;

    // getters
    public String getDeckName() {
        return deckName;
    }

    public List<Map<String, String>> getFlashcards() {
        return flashcards;
    }

    public String getViewName() {
        return viewName;
    }

    public String getDeckError() {
        return deckError;
    }

    public String getDeckEmptyError() {
        return deckEmptyError;
    }

    // setters
    public void setDeckName(String deckName) {
        this.deckName = deckName;
    }

    public void setFlashcards(List<Map<String, String>> flashcards) {
        this.flashcards = flashcards;
    }

    public void setDeckError(String deckError) {
        this.deckError = deckError;
    }

    public void setEmptyDeckError(String deckEmptyError) {
        this.deckEmptyError = deckEmptyError;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    @Override
    public String toString() {
        return "GetDeckState{"
                + "deckName='" + deckName + '\''
                + "flashcards='" + flashcards + '\''
                + '}';
    }

    public String getFlashcardCountText() {
        return flashcards.size() + " cards";
    }
}
