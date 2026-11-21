package entity.card;

/**
 * Colored cards. That's all cards except wild cards.
 */
public abstract class ColoredCard extends Card {

    private CardColor color;

    /**
     * Constructor of the ColoredCard.
     *
     * @param color the color of this card
     */
    public ColoredCard(CardColor color) {
        super();
        this.color = color;
    }

    /**
     * getter method for card color.
     *
     * @return Color of the card
     */
    public CardColor getColor() {
        return color;
    }
}
