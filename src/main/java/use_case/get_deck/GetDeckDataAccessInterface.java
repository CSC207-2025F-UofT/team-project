package use_case.get_deck;

import entity.Deck;

/**
 * DAO interface for the GetDeck Use Case.
 */
public interface GetDeckDataAccessInterface {

    /**
     * Checks if a deck exists by the deck name.
     * @param name the name of the deck
     * @return true if the deck exists, false otherwise
     */
    boolean deckExists(String name);

    /**
     * Gives the deck with the given name
     * @param name the deck name
     * @return the Deck object
     */
    Deck getDeck(String name);
}
