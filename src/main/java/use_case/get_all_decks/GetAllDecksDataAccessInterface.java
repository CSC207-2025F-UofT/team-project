package use_case.get_all_decks;

import entity.Deck;

import java.util.List;

/**
 * DAO interface fo
 */
public interface GetAllDecksDataAccessInterface {

    /**
     * @return a list of all available flashcard decks
     */
    List<Deck> getAllDecks();

    /**
     * Checks if there are any available flashcard decks.
     * @return true if there are available flashcard decks and false otherwise.
     */
    boolean isEmpty();
}
