package data_access;

import entity.Card;
import entity.Deck;
import use_case.add_card.AddCardDataAccessInterface;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class InMemoryDeckRepository implements AddCardDataAccessInterface{

    private final Map<Integer, Deck> decks = new HashMap<>();
    private int nextCardId = 1;

    public InMemoryDeckRepository(List<Deck> initialDecks) {
        for (Deck deck : initialDecks) {
            if (decks.putIfAbsent(deck.getId(), deck) != null) {
                throw new IllegalArgumentException("Duplicate deck id " + deck.getId());
            }

            for (Card card : deck.getCards()) {
                nextCardId = Math.max(nextCardId, card.getId() + 1);
            }
        }
    }

    @Override
    public boolean deckExists(int deckId) {
        return decks.containsKey(deckId);
    }

    @Override
    public int generateCardId() {
        return nextCardId++;
    }

    @Override
    public void saveCard(int deckId, Card card) {
        Deck deck = decks.get(deckId);

        if (deck == null) {
            throw new IllegalArgumentException("Deck with id " + deckId + " does not exist");
        }

        for (Deck existingDeck : decks.values()) {
            for (Card existingCard : existingDeck.getCards()) {
                if (existingCard.getId() == card.getId()) {
                    throw new IllegalArgumentException("Duplicate card id " + card.getId());
                }
            }
        }

        deck.add_card(card);

    }
}
