package entity.card;

import entity.Game;

/**
 * Represents a skip card in UNO. Skip cards are colored.
 */
public class SkipCard extends ColoredCard implements CardEffect {

    /**
     * Constructor of a SkipCard.
     *
     * @param color the color of this card
     */
    public SkipCard(CardColor color) {
        super(color);
    }

    /**
     * Apply the effect of the card in a game. Playing this card skips the next player.
     *
     * @param game The current Game that is running.
     */
    @Override
    public void applyEffect(Game game) {

    }
}
