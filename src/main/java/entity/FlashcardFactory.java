package entity;

/**
 * Factory for creating Flashcard objects.
 */
public class FlashcardFactory {

    public Flashcard create(String question, String answer) { return new Flashcard(question, answer); }
}
