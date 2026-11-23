package use_case.add_deck;

import entity.Deck;
import entity.Card;

public interface AddDeckDataAccessInterface {
  /**
   * Saves the deck to the data persistence.
   *
   * @param deckId The ID of the deck
   */
  boolean deckExists(int deckId);

  int generateDeckId();

  void saveDeck(Deck deck);
}
