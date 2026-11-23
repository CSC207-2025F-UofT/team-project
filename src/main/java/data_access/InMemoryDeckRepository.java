package data_access;

import entity.Card;
import entity.Deck;
import use_case.add_card.AddCardDataAccessInterface;
import use_case.edit_card.EditCardDataAccessInterface;
import use_case.edit_card.EditCardDataAccessException;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Optional;

public class InMemoryDeckRepository implements AddCardDataAccessInterface, EditCardDataAccessInterface {

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
    public Optional<Card> findCard(int deckId, int cardId) {
        Deck deck = decks.get(deckId);

        if (deck == null) {
            return Optional.empty();
        }

        for (Card card : deck.getCards()) {
            if (card.getId() == cardId) {
                return Optional.of(new Card(
                        card.getId(),
                        card.getQuestion(),
                        card.getAnswer()
                ));
            }
        }

        return Optional.empty();

    }

    @Override
    public void updateCard(int deckId,Card updatedCard) {
        Deck deck = decks.get(deckId);

        if (deck == null) {
            throw new EditCardDataAccessException("Deck with id " + deckId + " not found");
        }

        for (Card existingCard : deck.getCards()) {
            if (existingCard.getId() == updatedCard.getId()) {
                existingCard.updateContent(
                        updatedCard.getQuestion(),
                        updatedCard.getAnswer()
                );
                return;
            }
        }

        throw new EditCardDataAccessException("Card with id " + updatedCard.getId() + " not found in deck");

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
