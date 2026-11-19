package data_access;

import entities.Deck;

import java.io.IOException;

public interface FlashcardDataAccessInterface {
    public void saveDeck(Deck deck);

    public Deck loadDeck(String deckId);
}
