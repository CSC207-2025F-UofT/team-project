package data_access;

import entity.FlashcardDeck;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryDeckDataAccess extends DeckDataAccess {

    private final Map<Integer, List<FlashcardDeck>> decksByUser;
    private final Map<Integer, Integer> dontKnowDeckIdByUser;
    private int nextDeckId;

    public InMemoryDeckDataAccess() {
        this.decksByUser = new HashMap<>();
        this.dontKnowDeckIdByUser = new HashMap<>();
        this.nextDeckId = 1;
    }

    @Override
    public synchronized boolean existsByTitleForUser(int userId, String title) {
        List<FlashcardDeck> list = decksByUser.get(userId);
        if (list == null) {
            return false;
        }
        for (FlashcardDeck deck : list) {
            if (deck.getTitle().equals(title)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public synchronized int nextDeckId() {
        int id = nextDeckId;
        nextDeckId = nextDeckId + 1;
        return id;
    }

    @Override
    public synchronized void save(FlashcardDeck deck) {
        int userId = deck.getUserId();
        List<FlashcardDeck> list = decksByUser.get(userId);
        if (list == null) {
            list = new ArrayList<>();
            decksByUser.put(userId, list);
        }
        list.add(deck);
    }

    @Override
    public synchronized List<FlashcardDeck> findByUser(int userId) {
        List<FlashcardDeck> list = decksByUser.get(userId);
        if (list == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(list);
    }

    @Override
    public synchronized FlashcardDeck findById(int deckId) {
        for (List<FlashcardDeck> list : decksByUser.values()) {
            for (FlashcardDeck deck : list) {
                if (deck.getId() == deckId) {
                    return deck;
                }
            }
        }
        return null;
    }

    @Override
    public synchronized int findOrCreateDontKnowDeckId(int userId) {
        Integer cached = dontKnowDeckIdByUser.get(userId);
        if (cached != null) {
            return cached;
        }
        int newId = nextDeckId();
        FlashcardDeck dk = new FlashcardDeck(newId, "Don't know deck", userId);
        save(dk);
        dontKnowDeckIdByUser.put(userId, newId);
        return newId;
    }
}
