package entity.card;

import entity.Game;
import entity.Player;

/**
 * Represents the wild card in UNO.
 */
public class WildCard extends Card implements CardEffect {

    /**
     * Lets the player choose a color out of red, yellow, green, and blue.
     * Not implemented yet. Always return RED for now.
     *
     * @return Color choice from player.
     */
    public CardColor chooseColor(Player player) {
        return CardColor.RED;
    }

    /**
     * Applying a WildCard changes the currentColor of the game to the choice of the player, who
     * played a WildCard
     *
     * @param game The current Game that is running.
     */
    @Override
    public void applyEffect(Game game) {

    }

    /**
     * @return CardType of this card
     */
    @Override
    public CardType getCardType() {
        return CardType.WILD;
    }
}
