package entity.card;

import entity.Game;

/**
 * An interface for all cards that possess some effect. This includes draw card, reverse card
 * skip card, and wild card
 */
public interface CardEffect {

    /**
     * Applies the effect of the card in a game.
     *
     * @param game The current Game that is running.
     */
    void applyEffect(Game game);

}