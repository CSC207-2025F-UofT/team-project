package entity;

import entity.card.Card;
import entity.card.CardColor;

import java.util.List;

/**
 * Factory for creating game entities.
 */
public class GameFactory {

    public Game create(gameSetting settings, List<Player> players, Deck drawPile,
                       Card firstDiscard, CardColor initialColor) {
        return new Game(settings, players, drawPile, firstDiscard, initialColor);
    }
}
