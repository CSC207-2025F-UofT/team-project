package data_access;

import entities.Deck;

import java.io.FileWriter;
import java.io.IOException;

public class FlashcardDAO implements FlashcardDataAccessInterface {
    @Override
    public void saveDeck(Deck deck) {
    }

    @Override
    public Deck loadDeck(String deckId) {
        return null;
    }
}
