package entity.card;

/**
 * Represent an individual UNO card.
 */
public abstract class Card {

    /**
     * Getter for CardType
     *
     * @return CardType
     */
    public abstract CardType getCardType();

    /**
     * Returns a copy of this card.
     *
     * @return A copy of this.
     */
    public abstract Card copy();

}