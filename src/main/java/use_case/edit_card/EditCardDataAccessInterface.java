package use_case.edit_card;

import entity.Card;

import java.util.Optional;

public interface EditCardDataAccessInterface {
    boolean deckExists(int deckId);

    // if card is not found
    Optional<Card> findCard(int deckId, int cardId);

    /**
     *      ensure replacement edit
     *      update an existing card, but will not insert a new card into deck.
     *      Throw EditCardDataAccessException if cannot finish the update
     **/

    void updateCard(int deckId, Card card);
}
