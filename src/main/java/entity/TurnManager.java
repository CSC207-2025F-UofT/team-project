package entity;

/**
 * Manages turn order, direction and pending skip effects.
 */
public final class TurnManager {

    private final int playerCount;
    private int currentPlayerIndex;
    private Direction direction;
    private boolean skipPending;

    public TurnManager(int playerCount) {
        if (playerCount < 2 || playerCount > 4) {
            throw new IllegalArgumentException(
                    "A game needs 2 to 4 players.");
        }

        this.playerCount = playerCount;
        this.currentPlayerIndex = 0;
        this.direction = Direction.CLOCKWISE;
        this.skipPending = false;
    }
    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public Direction getDirection() {
        return direction;
    }

    /**
     * Returns the adjacent player's index in the current direction.
     */
    public int getNextPlayerIndex() {
        int offset = direction == Direction.CLOCKWISE ? 1 : -1;

        return Math.floorMod(
                currentPlayerIndex + offset, playerCount);
    }

    /**
     * Marks the next player to be skipped when the turn advances.
     */
    public void skipNextPlayer() {
        skipPending = true;
    }

    /**
     * Reverses direction. With two players, Reverse acts as Skip.
     */
    public void reverseDirection() {
        direction = direction == Direction.CLOCKWISE
                ? Direction.ANTICLOCKWISE
                : Direction.CLOCKWISE;

        if (playerCount == 2) {
            skipNextPlayer();
        }
    }

    /**
     * Advances the turn and consumes any pending skip effect.
     */
    public void advanceTurn() {
        int steps = skipPending ? 2 : 1;
        int offset = direction == Direction.CLOCKWISE ? 1 : -1;

        currentPlayerIndex = Math.floorMod(
                currentPlayerIndex + offset * steps,
                playerCount);

        skipPending = false;
    }
}