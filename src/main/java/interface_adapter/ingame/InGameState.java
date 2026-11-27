package interface_adapter.ingame;

import entity.Direction;
import entity.card.Card;

import java.util.List;

/**
 * The State information representing the game live in InGameView.
 */
public class InGameState {

    private String currentPlayerId;

    private List<Card> currentPlayerHand;
    private List<PlayerViewModel> otherPlayers;

    private Card topDiscardCard;
    private int drawPileCount;

    private Direction currentDirection;

    private String statusMessage;

    private boolean declareUnoVisible;
    private boolean gameFinished;
    private String winnerId;

    // ------- Getter & setter methods -------

    public String getCurrentPlayerId() {
        return currentPlayerId;
    }

    public void setCurrentPlayerId(String currentPlayerId) {
        this.currentPlayerId = currentPlayerId;
    }

    public List<Card> getCurrentPlayerHand() {
        return currentPlayerHand;
    }

    public void setCurrentPlayerHand(List<Card> currentPlayerHand) {
        this.currentPlayerHand = currentPlayerHand;
    }

    public List<PlayerViewModel> getOtherPlayers() {
        return otherPlayers;
    }

    public void setOtherPlayers(List<PlayerViewModel> otherPlayers) {
        this.otherPlayers = otherPlayers;
    }

    public Card getTopDiscardCard() {
        return topDiscardCard;
    }

    public void setTopDiscardCard(Card topDiscardCard) {
        this.topDiscardCard = topDiscardCard;
    }

    public int getDrawPileCount() {
        return drawPileCount;
    }

    public void setDrawPileCount(int drawPileCount) {
        this.drawPileCount = drawPileCount;
    }

    public Direction getCurrentDirection() {
        return currentDirection;
    }

    public void setCurrentDirection(Direction currentDirection) {
        this.currentDirection = currentDirection;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public boolean isDeclareUnoVisible() {
        return declareUnoVisible;
    }

    public void setDeclareUnoVisible(boolean declareUnoVisible) {
        this.declareUnoVisible = declareUnoVisible;
    }

    public boolean isGameFinished() {
        return gameFinished;
    }

    public void setGameFinished(boolean gameFinished) {
        this.gameFinished = gameFinished;
    }

    public String getWinnerId() {
        return winnerId;
    }

    public void setWinnerId(String winnerId) {
        this.winnerId = winnerId;
    }
}
