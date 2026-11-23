package entity.card;

import entity.Game;

/**
 * Represents a wild draw four card in UNO, which is a type of wild card.
 */
public class WildDrawFourCard extends WildCard implements CardEffect {

    /**
     * Applying a WildDrawFourCard changes the currentColor of the game to the choice of
     * the currentPlayer and forces the nextPlayer to draw four cards from the draw pile.
     *
     * @param game The current Game that is running.
     */
    @Override
    public void applyEffect(Game game) {
        super.applyEffect(game); // ths call to super changes the color of current game
    }

    /**
     * Getter for CardType.
     *
     * @return CardType.WILD_DRAW_FOUR
     */
    @Override
    public CardType getCardType() {
        return CardType.WILD_DRAW_FOUR;
    }
}
