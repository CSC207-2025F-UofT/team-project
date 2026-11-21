package entity.card;

/**
 * Represents a number card in UNO, which are colored.
 */
public class NumberCard extends ColoredCard {

    private int number;

    /**
     * Constructor of the NumberCard.
     *
     * @param color Color of the card
     * @param number The number on the card, out of integers 0-9
     */
    public NumberCard(CardColor color, int number) {
        super(color);
        this.number = number;
    }

    /**
     * Getter method for card number.
     *
     * @return the number on the card.
     */
    public int getNumber() {
        return number;
    }
}