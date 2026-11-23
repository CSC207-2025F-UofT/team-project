package data_access;

import entity.Player;
import entity.Round;
import use_case.shared.CurrentPlayerDataAccessInterface;
import use_case.shared.CurrentRoundDataAccessInterface;

/**
 * In-memory game state: the current player and the current round.
 * One instance is created in AppBuilder and passed to the interactors that need it.
 * Each interactor only sees it through the interface(s) it needs.
 */
public class GameDataAccessObject implements CurrentPlayerDataAccessInterface, CurrentRoundDataAccessInterface {
    private Player currentPlayer;
    private Round currentRound;

    @Override
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    @Override
    public void setCurrentPlayer(Player player) {
        this.currentPlayer = player;
    }

    @Override
    public Round getCurrentRound() {
        return currentRound;
    }

    @Override
    public void setCurrentRound(Round round) {
        this.currentRound = round;
    }
}
