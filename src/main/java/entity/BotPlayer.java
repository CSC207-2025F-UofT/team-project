package entity;

import entity.card.Card;
import entity.card.CardColor;
import entity.card.ColoredCard;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a bot player in a UNO game.
 */
public class BotPlayer extends Player {

    /**
     * Creates a bot player with and ID and an initial hand.
     *
     * @param id the bot player's identifier
     * @param hand the cards initially held by the bot player
     */
    public BotPlayer(String id, List<Card> hand) {
        super(id, hand);
    }

    /**
     * Choose a playable card for this bot.
     *
     * @param playableCards the cards this bot can play
     * @return the first playable card
     * @throws IllegalArgumentException if there are no playable cards
     */
    public Card chooseCard(List<Card> playableCards) {
        if (playableCards.isEmpty()) {
            throw new IllegalArgumentException("There are no playable cards.");
        }
        return playableCards.get(0);
    }

    /**
     * Chooses the colour the bot has the most cards of.
     * If there is a tie, chooses the colour declared first in CardColor.
     *
     * @return the chosen colour
     */
    public CardColor chooseColour() {
        Map<CardColor, Integer> counts = new EnumMap<>(CardColor.class);

        for (CardColor color : CardColor.values()) {
            counts.put(color, 0);
        }

        for (Card card : getHand()) {
            if (card instanceof ColoredCard) {
                CardColor color = ((ColoredCard) card).getColor();
                counts.put(color, counts.get(color) + 1);
            }
        }

        CardColor chosenColor = CardColor.values()[0];
        int maxCount = -1;

        for (CardColor color : CardColor.values()) {
            if (counts.get(color) > maxCount) {
                maxCount = counts.get(color);
                chosenColor = color;
            }
        }

        return chosenColor;
    }
}
