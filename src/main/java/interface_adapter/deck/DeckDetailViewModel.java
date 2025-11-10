package interface_adapter.deck;

import entity.FlashcardDeck;
import entity.Flashcard;
import java.util.List;
//TODO: this viewmodel is incomplete and just skeleton, add more fields as necessary
public class DeckDetailViewModel {

    private FlashcardDeck currentDeck;
    private List<Flashcard> flashcards;

    public FlashcardDeck getCurrentDeck() {
        return currentDeck;
    }

    public void setCurrentDeck(FlashcardDeck deck) {
        this.currentDeck = deck;
    }

    public List<Flashcard> getFlashcards() {
        return flashcards;
    }

    public void setFlashcards(List<Flashcard> flashcards) {
        this.flashcards = flashcards;
    }
}
