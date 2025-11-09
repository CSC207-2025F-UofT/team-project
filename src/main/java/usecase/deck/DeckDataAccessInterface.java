package usecase.deck;

import entity.FlashcardDeck;

public interface DeckDataAccessInterface {
    void saveDeck(FlashcardDeck deck);

    boolean existsByTitleForUser(int userId, String title);

    java.util.List<FlashcardDeck> getDecksForUser(int userId);

    FlashcardDeck getDeckById(int deckId);
}
