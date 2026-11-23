package yuhangusecase.castspell;

import entity.PendingCast;
import entity.Player;
import entity.board.Board;

public interface CastGameDataAccess {
    Board getBoard();

    // Return the existing player instance, or null if no game is active.
    Player getCurrentPlayer();

    // Store the cast only if no cast is already pending.
    // Return false without changing state if another cast is pending.
    boolean trySavePendingCast(PendingCast pendingCast);
}
