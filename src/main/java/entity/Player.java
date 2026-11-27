package entity;

import entity.card.Card;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player in a UNO game.
 */
public class Player {
    private String id;
    private List<Card> hand;
    private boolean unoDeclared = false;

    /**
     * Creates a player with an ID and an initial hand.
     *
     * @param id the player's identifier
     * @param hand the cards initially held by the player
     */
    public Player(String id, List<Card> hand) {
        this.id = id;
        this.hand = new ArrayList<>(hand);
    }

    /**
     * Adds cards to this player's hand.
     *
     * @param cards the cards to add
     */
    public void takeCards(List<Card> cards) {
        hand.addAll(cards);
        unoDeclared = false;
    }

    /**
     * Removes and returns a card from this player's hand.
     *
     * @param index the position of the card in the hand
     * @return the card removed from the hand
     */
    public Card playCard(int index) {
        Card playedCard = hand.remove(index);
        unoDeclared = false;
        return playedCard;
    }

    /**
     * Gets this player's identifier.
     *
     * @return this player's identifier
     */
    public String getId() {
        return id;
    }

    /**
     * Gets a copy of this player's hand.
     *
     * @return a copy of the cards in this player's hand
     */
    public List<Card> getHand() {
        return new ArrayList<>(hand);
    }

    /**
     * Checks whether the player has declared UNO.
     *
     * @return true if the player has declared UNO, otherwise, return false.
     */
    public boolean hasDeclaredUno() {
        return unoDeclared;
    }

    /**
     * Records that this player has declared UNO.
     */
    public void declareUno() {
        unoDeclared = true;
    }
}
