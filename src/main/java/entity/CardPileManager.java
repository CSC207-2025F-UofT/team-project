package entity;

import entity.card.Card;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Manages the draw pile and discard pile for one game.
 */
public final class CardPileManager {

    private final Deck drawPile;
    private final List<Card> discardPile;

    public CardPileManager(Deck drawPile, Card firstDiscard) {
        this.drawPile = Objects.requireNonNull(
                drawPile, "Draw pile cannot be null.");

        this.discardPile = new ArrayList<>();
        this.discardPile.add(Objects.requireNonNull(
                firstDiscard, "First discard cannot be null."));
    }
    /**
     * Returns the draw pile used by this game.
     */
    public Deck getDrawPile() {
        return drawPile;
    }

    /**
     * Returns an unmodifiable snapshot of the discard pile.
     */
    public List<Card> getDiscardPile() {
        return List.copyOf(discardPile);
    }

    /**
     * Returns the card at the top of the discard pile.
     */
    public Card getTopDiscard() {
        return discardPile.get(discardPile.size() - 1);
    }

    /**
     * Places a card on top of the discard pile.
     */
    public void addToDiscardPile(Card card) {
        discardPile.add(Objects.requireNonNull(
                card, "Card cannot be null."));
    }
}