package use_case.add_card;

import entity.Card;

public interface AddCardDataAccessInterace {
    /**
     * Saves the card to the database.
     *
     * @param deckId The ID of the deck to which the card belongs.
     * @param card The card to be saved.
     */
    boolean deckExists(int deckId);

    int generateCardId();

    void saveCard(int deckId, Card card);
}