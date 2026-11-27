package use_case.playcard;

import entity.card.Card;
import entity.card.CardColor;

import java.util.List;

/**
 * Output Data for the PlayCard Use Case.
 */
public class PlayCardOutputData {

    private final Card playedCard;
    private final List<Card> updatedHand;
    private final String nextPlayerId;
    private final CardColor currentColor;
    private final String effectMessage;
    private final boolean gameFinished;
    private final String winnerId;

    public PlayCardOutputData(Card playedCard, List<Card> updatedHand, String nextPlayerId,
                              CardColor currentColor, String effectMessage,
                              boolean gameFinished, String winnerId) {
        this.playedCard = playedCard;
        this.updatedHand = updatedHand;
        this.nextPlayerId = nextPlayerId;
        this.currentColor = currentColor;
        this.effectMessage = effectMessage;
        this.gameFinished = gameFinished;
        this.winnerId = winnerId;
    }

    public Card getPlayedCard() {
        return playedCard;
    }

    public List<Card> getUpdatedHand() {
        return updatedHand;
    }

    public String getNextPlayerId() {
        return nextPlayerId;
    }

    public CardColor getCurrentColor() {
        return currentColor;
    }

    public boolean isGameFinished() {
        return gameFinished;
    }

    public String getEffectMessage() {
        return effectMessage;
    }

    public String getWinnerId() {
        return winnerId;
    }
}