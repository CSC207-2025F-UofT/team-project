package interface_adapter.ingame.InGameTestView;

import entity.card.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Mutable presentation data for {@link InGameTestView}. This class deliberately contains no UNO
 * rules: card values are resource IDs understood only by the Swing view.
 */
public final class GameTestViewState {
    private String topOpponentName = "Maya";
    private int topOpponentCardCount;
    private String leftOpponentName = "Noah";
    private int leftOpponentCardCount;
    private String rightOpponentName = "Ava";
    private int rightOpponentCardCount;
    private String currentPlayerName = "YOU";
    private List<Card> currentPlayerHand = new ArrayList<Card>();
    private int drawPileCardCount;
    private Card discardTopCard;

    /** Creates an empty table state with the demo player names. */
    public GameTestViewState() {
    }

    /** Copies display data so a view can safely retain a submitted update. */
    public GameTestViewState(GameTestViewState source) {
        Objects.requireNonNull(source, "source must not be null");
        topOpponentName = source.topOpponentName;
        topOpponentCardCount = source.topOpponentCardCount;
        leftOpponentName = source.leftOpponentName;
        leftOpponentCardCount = source.leftOpponentCardCount;
        rightOpponentName = source.rightOpponentName;
        rightOpponentCardCount = source.rightOpponentCardCount;
        currentPlayerName = source.currentPlayerName;
        currentPlayerHand = List.copyOf(source.currentPlayerHand);
        drawPileCardCount = source.drawPileCardCount;
        discardTopCard = source.discardTopCard;
    }

    /** Provides the visual-only sample state shown by {@link UnoApplication}. */
    public static GameTestViewState demo() {
        List<Card> currentPlayerHand = new ArrayList<Card>(List.of(new NumberCard(CardColor.RED, 7),
                new SkipCard(CardColor.YELLOW), new NumberCard(CardColor.GREEN, 2),
                new ReverseCard(CardColor.BLUE), new DrawTwoCard(CardColor.RED),
                new NumberCard(CardColor.YELLOW, 9), new NumberCard(CardColor.BLUE, 5)));

        return new GameTestViewState()
                .setTopOpponent("Maya", 20)
                .setLeftOpponent("Noah", 1)
                .setRightOpponent("Ava", 0)
                .setCurrentPlayer("YOU", currentPlayerHand)
                .setDrawPileCardCount(24)
                .setDiscardTopCard(new NumberCard(CardColor.RED, 1));
    }

    public String getTopOpponentName() { return topOpponentName; }
    public int getTopOpponentCardCount() { return topOpponentCardCount; }

    public GameTestViewState setTopOpponent(String name, int cardCount) {
        topOpponentName = requireName(name);
        topOpponentCardCount = requireCardCount(cardCount);
        return this;
    }

    public GameTestViewState setTopOpponentCardCount(int cardCount) {
        topOpponentCardCount = requireCardCount(cardCount);
        return this;
    }

    public String getLeftOpponentName() { return leftOpponentName; }
    public int getLeftOpponentCardCount() { return leftOpponentCardCount; }

    public GameTestViewState setLeftOpponent(String name, int cardCount) {
        leftOpponentName = requireName(name);
        leftOpponentCardCount = requireCardCount(cardCount);
        return this;
    }

    public GameTestViewState setLeftOpponentCardCount(int cardCount) {
        leftOpponentCardCount = requireCardCount(cardCount);
        return this;
    }

    public String getRightOpponentName() { return rightOpponentName; }
    public int getRightOpponentCardCount() { return rightOpponentCardCount; }

    public GameTestViewState setRightOpponent(String name, int cardCount) {
        rightOpponentName = requireName(name);
        rightOpponentCardCount = requireCardCount(cardCount);
        return this;
    }

    public GameTestViewState setRightOpponentCardCount(int cardCount) {
        rightOpponentCardCount = requireCardCount(cardCount);
        return this;
    }

    public String getCurrentPlayerName() { return currentPlayerName; }
    public List<Card> getCurrentPlayerHand() { return currentPlayerHand; }

    public GameTestViewState setCurrentPlayer(String name, List<Card> cards) {
        currentPlayerName = requireName(name);
        return setCurrentPlayerHand(cards);
    }

    public GameTestViewState setCurrentPlayerHand(List<Card> cards) {
        List<Card> copy = new ArrayList<>();

        for (Card card : cards) {
            copy.add(card.copy());
        }

        currentPlayerHand = copy;
        return this;
    }

    public int getDrawPileCardCount() { return drawPileCardCount; }

    public GameTestViewState setDrawPileCardCount(int cardCount) {
        drawPileCardCount = requireCardCount(cardCount);
        return this;
    }

    public Card getDiscardTopCard() { return discardTopCard; }

    /** Sets the resource ID of the visible discard card, or {@code null} for an empty pile. */
    public GameTestViewState setDiscardTopCard(Card card) {
        discardTopCard = card;
        return this;
    }

    private static String requireName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Player name must not be blank");
        }
        return name;
    }

    private static int requireCardCount(int cardCount) {
        if (cardCount < 0) {
            throw new IllegalArgumentException("Card count must not be negative");
        }
        return cardCount;
    }

    private static String requireCardId(String cardId) {
        if (cardId == null || cardId.isBlank() || cardId.startsWith("/") || cardId.contains("..")) {
            throw new IllegalArgumentException("Card ID must be a relative asset ID");
        }
        return cardId;
    }
}
