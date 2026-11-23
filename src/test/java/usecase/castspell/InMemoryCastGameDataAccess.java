package usecase.castspell;

import entity.PendingCast;
import entity.Player;
import entity.board.Board;
import yuhangusecase.castspell.CastGameDataAccess;

import java.util.Objects;

public class InMemoryCastGameDataAccess implements CastGameDataAccess {
    private final Board board;
    private final Player currentPlayer;
    private PendingCast pendingCast;

    public InMemoryCastGameDataAccess(Board board, Player currentPlayer) {
        this.board = board;
        this.currentPlayer = currentPlayer;
    }

    @Override
    public Board getBoard() {
        return board;
    }

    @Override
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    @Override
    public boolean trySavePendingCast(PendingCast pendingCast) {
        Objects.requireNonNull(pendingCast, "Pending cast is required.");

        if (this.pendingCast != null) {
            return false;
        }

        this.pendingCast = pendingCast;
        return true;
    }

    public PendingCast getPendingCast() {
        return pendingCast;
    }
}
