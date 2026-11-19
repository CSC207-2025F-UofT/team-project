package data_access;

import entities.Deck;

public interface FlashcardDataAccessInterface {
    public void saveDeck(Deck deck);

    public Deck loadDeck(String deckId);
}
