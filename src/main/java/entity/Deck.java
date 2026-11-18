package entity;

import java.util.ArrayList;
import java.util.List;

/**
 * An entity representing a Deck. Decks have a name and flashcards.
 */
public class Deck {

    private String name;
    private ArrayList<Flashcard> flashcards;

    /**
     * Creates a new deck with the given unique name, and empty flashcards list by default
     *
     * @param name the name of the deck
     * @throws IllegalArgumentException if the name is empty
     */
    public Deck(String name) throws IllegalArgumentException {
        if ("".equals(name)) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
        this.name = name;
        this.flashcards = new ArrayList<>();
    }

    public void addFlashcard(Flashcard flashcard) {
        flashcards.add(flashcard);
    }

    public void removeFlashcard(String question) {
        flashcards.removeIf(flashcard -> flashcard.getQuestion().equals(question));
    }

    public void updateDeckName(String name) {
        if ("".equals(name)) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
        this.name = name;
    }

    public List<Flashcard> getFlashcards() {
        return new ArrayList<>(flashcards);
    }

    public int getFlashcardCount() {
        return flashcards.size();
    }

    public String getName() {
        return name;
    }

}
