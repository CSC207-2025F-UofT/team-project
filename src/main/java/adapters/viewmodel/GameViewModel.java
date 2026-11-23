package adapters.viewmodel;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Presentation snapshot from the local player's perspective. The board is stored
 * in row-major order, and the hand is in display order.
 */
public record GameViewModel(
        String gameStatus,
        int remainingTurnSeconds,
        List<TileViewData> board,
        List<CardViewData> hand,
        int drawCount,
        int discardCount,
        CardViewData topDiscard
) {
    public static final int BOARD_SIZE = 6;

    public GameViewModel {
        Objects.requireNonNull(gameStatus);
        board = List.copyOf(board);
        if (board.size() != BOARD_SIZE * BOARD_SIZE) {
            throw new IllegalArgumentException("Expected " + (BOARD_SIZE * BOARD_SIZE) + " board tiles in row-major order.");
        }
        hand = List.copyOf(hand);
    }

    public static GameViewModel empty() {
        return new GameViewModel("Your turn", 0, emptyBoard(), List.of(), 0, 0, null);
    }

    public static List<TileViewData> emptyBoard() {
        return Collections.nCopies(BOARD_SIZE * BOARD_SIZE, new TileViewData(null, List.of()));
    }

    public static int tileIndex(int column, int row) {
        Objects.checkIndex(column, BOARD_SIZE);
        Objects.checkIndex(row, BOARD_SIZE);
        return row * BOARD_SIZE + column;
    }

    public TileViewData tileAt(int column, int row) {
        return board.get(tileIndex(column, row));
    }

    public GameViewModel withRemainingTurnSeconds(int seconds) {
        return new GameViewModel(gameStatus, seconds, board, hand, drawCount, discardCount, topDiscard);
    }

    public GameViewModel withGameStatus(String status) {
        return new GameViewModel(status, remainingTurnSeconds, board, hand, drawCount, discardCount, topDiscard);
    }

    /** A tile may have an occupant and attached spells controlled by either player. */
    public record TileViewData(CardViewData occupant, List<CardViewData> spells, boolean opponentControlled) {
        public TileViewData {
            spells = List.copyOf(spells);
        }

        public TileViewData(CardViewData occupant, List<CardViewData> spells) {
            this(occupant, spells, false);
        }

        public boolean isEmpty() {
            return occupant == null && spells.isEmpty();
        }
    }

    /** Display text, artwork category, and current HP when available for a character. */
    public record CardViewData(String name, CardType type, String description, Integer currentHp) {
        public CardViewData {
            Objects.requireNonNull(name);
            Objects.requireNonNull(type);
            Objects.requireNonNull(description);
        }

        public CardViewData(String name, CardType type, String description) {
            this(name, type, description, null);
        }

        public String accessibleDescription() {
            String details = name + " · " + description;
            return currentHp == null || type != CardType.CHARACTER ? details : details + " · " + currentHp + " HP";
        }
    }

    public enum CardType {
        CHARACTER, SPELL, SORCERY, OTHER
    }
}
