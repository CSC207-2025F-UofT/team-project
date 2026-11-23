package entity.card;

import entity.Game;

/**
 * Class representing draw two cards in UNO, which are colored.
 */
public class DrawTwoCard extends ColoredCard implements CardEffect {


    /**
     * Constructor of the DrawTwoCard.
     *
     * @param color the color of this card
     */
    public DrawTwoCard(CardColor color) {
        super(color);
    }

    /**
     * Apply the effect of the card in a game. DrawTwoCard forces the next player
     * to draw two cards from drawPile.
     *
     * @param game The current Game that is running.
     */
    @Override
    public void applyEffect(Game game) {

    }

    /**
     * Getter for CardType.
     *
     * @return CardType.DRAW_TWO
     */
    @Override
    public CardType getCardType() {
        return CardType.DRAW_TWO;
    }
}
