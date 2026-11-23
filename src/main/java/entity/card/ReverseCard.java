package entity.card;

import entity.Game;

/**
 * Represents the reverse card in UNO. Reverse cards are colored.
 */
public class ReverseCard extends ColoredCard implements CardEffect {

    /**
     * Constructor of the ReverseCard.
     *
     * @param color the color of this card
     */
    public ReverseCard(CardColor color) {
        super(color);
    }

    /**
     * Apply the effect of the card in a game. ReverseCard flips the direction of the game.
     *
     * @param game The current Game that is running.
     */
    @Override
    public void applyEffect(Game game) {

    }

    /**
     * Getter for CardType.
     *
     * @return CardType.REVERSE
     */
    @Override
    public CardType getCardType() {
        return CardType.REVERSE;
    }
}
