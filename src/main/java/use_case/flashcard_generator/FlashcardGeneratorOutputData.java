package use_case.flashcard_generator;

import entities.Deck;

/**
 * OutputData for flashcard generator use case.
 */
public class FlashcardGeneratorOutputData {
    private final String title;
    private final Deck deck;

    public FlashcardGeneratorOutputData(String title, Deck deck){
        this.title = title;
        this.deck = deck;
    }

    /**
     * Retrieves the Deck
     * @return Deck Entity
     */
    public Deck getDeck() {return this.deck;}

    /**
     * Retrieved title of Deck
     * @return title of Deck Entity
     */
    public String getTitle() {return this.title;}
}
