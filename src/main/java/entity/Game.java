package entity;

import entity.card.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


/**
 * Represents a single UNO game and maintains its current state.
 *
 * <p>Stores the game configuration, participating players, draw pile,
 * discard pile, current player, turn direction, and active colour.
 * Players may be either human players or computer-controlled bots.
 *
 * <p>The players, deck, first discard, and initial colour are supplied
 * when the game is created. Each game receives a unique identifier.
 *
 * <p>Provides access to game state and supports advancing the turn
 * in the current direction. User interface interactions, bot decisions,
 * and persistent data storage are handled outside this class.
 */

public class Game {
    // Unique identifier for this game.
    private final String gameId;
    // Configuration for this game, such as bot count and initial hand size.
    private final gameSetting settings;
    // Players in seating order, including both humans and bots.
    private final List<Player> players;
    // Deck from which players draw cards.
    private final Deck drawPile;
    // Discarded cards; the last element is the top card.
    private final List<Card> discardPile;

    // Index of the player whose turn it currently is.
    private int currentPlayerIndex;
    // Current direction of play: clockwise or anticlockwise.
    private Direction direction;
    // Active color that cards must match, unless another rule allows play.
    private CardColor currentColor;
    // ID of the winning player; null while no winner has been recorded.
    private String winnerId;
    // Whether the next player should be skipped when the turn advances.
    private boolean skipPending;
    /**
     * Creates a game using the supplied players, deck and initial discard.
     * The first player starts the game in the clockwise direction.
     *
     * <p>Copies the configuration and player-list structure. The supplied
     * player objects, deck and first discard are shared rather than cloned.
     * Players should already have their initial hands before play begins.
     *
     * @param settings the configuration used to initialize the game
     * @param players the human and bot players in turn order
     * @param drawPile the deck from which players draw cards
     * @param firstDiscard the initial top card, already removed from the deck
     * @param initialColor the active colour established by the starting rules
     * @throws NullPointerException if a required argument or player is null
     * @throws IllegalArgumentException if there are fewer than two or more than four players
     */
public Game(gameSetting settings, List<Player> players, Deck drawPile, Card firstDiscard, CardColor initialColor){
    /*to make sure that the inputs are avalible.*/
    Objects.requireNonNull(settings, "Settings cannot be null.");
    Objects.requireNonNull(players, "Players cannot be null.");
    /*to make sure game start with min 2 max 4 players, both human and bot count as a player.*/
    if (players.size() < 2 || players.size() > 4) {
        throw new IllegalArgumentException(
                "A game needs 2 to 4 players.");
    }
    this.settings = new gameSetting(settings.getNumBots(), settings.getCardsDealt());
    this.players = List.copyOf(players);
    this.gameId = UUID.randomUUID().toString();

    this.drawPile = Objects.requireNonNull(drawPile, "Draw pile cannot be null.");

    this.discardPile = new ArrayList<>();
    this.discardPile.add(Objects.requireNonNull(firstDiscard, "First discard cannot be null"));

    this.currentPlayerIndex = 0;
    this.direction = Direction.CLOCKWISE;
    this.currentColor = Objects.requireNonNull(initialColor, "Initial color cannot be null.");

}

    /**
     * Returns the identifier assigned to this game.
     *
     * @return this game's identifier
     */
    public String getGameId() {
        return gameId;
    }

    /**
     * Returns an independent copy of this game's configuration.
     * Changes to the returned object do not change the stored configuration.
     *
     * @return a copy of the game settings
     */
    public gameSetting getSettings() {
        return new gameSetting(settings.getNumBots(),
                                settings.getCardsDealt());
    }

    /**
     * Returns the unmodifiable player list in turn order.
     * The player objects themselves remain shared and can change during play.
     *
     * @return the players participating in this game
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Returns the shared draw pile used by this game.
     *
     * @return the game's deck, not a copy
     */
    public Deck getDrawPile() {
        return drawPile;
    }

    /**
     * Returns an unmodifiable snapshot of the discard-pile list.
     * The last element is the top card; card objects are not cloned.
     *
     * @return a snapshot of the discarded cards
     */
    public List<Card> getDiscardPile() {
        return List.copyOf(discardPile);
    }

    /**
     * Returns the current player's zero-based position in the player list.
     *
     * @return the current player's index
     */
    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    /**
     * Returns the current direction of play.
     *
     * @return CLOCKWISE or ANTICLOCKWISE
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Returns the active colour used when checking card matches.
     * This may differ from the top card's own colour after a wild card is played.
     *
     * @return the current active colour
     */
    public CardColor getCurrentColor() {
        return currentColor;
    }

    /**
     * Returns the player whose turn it currently is.
     *
     * @return the current human or bot player
     */
    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public Player getNextPlayer() {
        // TODO: currently implemented by AI
        int offset = direction == Direction.CLOCKWISE ? 1 : -1;
        return players.get(Math.floorMod(currentPlayerIndex + offset, players.size()));
    }

    /**
     * Returns the top card of the discard pile without removing it.
     *
     * @return the most recently discarded card
     */
    public Card getTopDiscard() {
        return discardPile.get(discardPile.size() - 1);
    }

    /**
     * Advances to the next player in the current direction.
     * If a skip is pending, moves two positions and then clears the skip flag.
     *
     * <p>This method does not choose or play a card for the next player.
     *
     * @throws IllegalStateException if a winner has already been recorded
     */
    public void advanceTurn() {
        if (isFinished()) {
            throw new IllegalStateException(
                    "Cannot advance the turn after the game has finished.");
        }

        // Move two positions for Skip; otherwise move one.
        int steps = skipPending ? 2 : 1;


        int offset = direction == Direction.CLOCKWISE ? 1 : -1;

        currentPlayerIndex = Math.floorMod(
                currentPlayerIndex + offset * steps,
                players.size());

        skipPending = false;
    }

    /**
     * Records the current player as the winner if their hand is empty.
     * An already recorded winner is left unchanged.
     *
     * <p>Call after a completed card play and its effects, before advancing
     * the turn. Initial hands must already have been dealt.
     *
     * @throws NullPointerException if the winning player's ID is null
     */
    public void checkForWinner() {
        if (isFinished()) {
            return;
        }
        Player currentPlayer = getCurrentPlayer();
        if (currentPlayer.getHand().isEmpty()) {
            winnerId = Objects.requireNonNull(
                    currentPlayer.getId(),
                    "Winner ID cannot be null.");
        }
    }

    /**
     * Returns whether a winner has been recorded.
     *
     * @return true if a winner has been recorded, otherwise false
     */
    public boolean isFinished() {
        return winnerId != null;
    }

    /**
     * Returns the recorded winning player's identifier.
     *
     * @return the winner's ID, or null if no winner has been recorded
     */
    public String getWinnerId(){
        return winnerId;
    }

    /**
     * Marks one player to be skipped on the next call to advanceTurn().
     * The current player is not changed immediately. Repeated calls before
     * advancing still mark only one player to be skipped.
     *
     * @throws IllegalStateException if a winner has already been recorded
     */
    public void skipNextPlayer() {
        if (isFinished()) {
            throw new IllegalStateException(
                    "Cannot skip a player after the game has finished.");
        }

        skipPending = true;
    }

    /**
     * Reverses the direction of play without immediately advancing the turn.
     * In a two-player game, also marks the opponent to be skipped according
     * to this game's adopted Reverse rule.
     *
     * @throws IllegalStateException if a winner has already been recorded
     */
    public void reverseDirection() {
        if (isFinished()) {
            throw new IllegalStateException(
                    "Cannot reverse direction after the game has finished.");
        }
        direction = direction == Direction.CLOCKWISE
                ? Direction.ANTICLOCKWISE
                : Direction.CLOCKWISE;

        if (players.size() == 2) {
            skipNextPlayer();
        }
    }

    /**
     * Updates the active color used for matching cards.
     * Does not modify the top card or advance the turn.
     *
     * @param newColor the new active color
     * @throws IllegalStateException if a winner has already been recorded
     * @throws NullPointerException if newColor is null
     */
    public void changeColor(CardColor newColor) {
        if (isFinished()) {
            throw new IllegalStateException(
                    "Cannot change colour after the game has finished.");
        }

        currentColor = Objects.requireNonNull(
                newColor, "New colour cannot be null.");
    }

    /**
     * Adds an already played card to the discard pile.
     * A colored card becomes the source of the active color. For a wild
     * card, the active color is left unchanged until changeColor() is called.
     *
     * <p>The caller must first validate the play and remove the card from
     * the player's hand. This method does not apply special effects,
     * check for a winner or advance the turn.
     *
     * @param card the card to add to the discard pile
     * @throws IllegalStateException if a winner has already been recorded
     * @throws NullPointerException if card or a colored card's color is null
     */
    public void addToDiscardPile(Card card) {
        if (isFinished()) {
            throw new IllegalStateException(
                    "Cannot discard a card after the game has finished.");
        }

        Objects.requireNonNull(card, "Card cannot be null.");

        CardColor nextColor = currentColor;

        if (card instanceof ColoredCard) {
            nextColor = Objects.requireNonNull(
                    ((ColoredCard) card).getColor(),
                    "Card colour cannot be null.");
        }

        discardPile.add(card);
        currentColor = nextColor;
    }

    /**
     * Checks whether the current player can play the given card.
     *
     * @param card the card to check
     * @return true if the card is playable
     */
    public boolean canPlayCard(Card card) {
        Objects.requireNonNull(card, "Card cannot be null.");

        Card topCard = getTopDiscard();

        if (card instanceof ColoredCard) {
            ColoredCard coloredCard = (ColoredCard) card;
            if (coloredCard.getColor() == currentColor) {
                return true;
            }
        }

        if (card instanceof NumberCard && topCard instanceof NumberCard) {
            return ((NumberCard) card).getNumber()
                    == ((NumberCard) topCard).getNumber();
        }

        if (card instanceof ReverseCard && topCard instanceof ReverseCard) {
            return true;
        }

        if (card instanceof SkipCard && topCard instanceof SkipCard) {
            return true;
        }

        return card instanceof WildCard;
    }

    /**
     * Draws cards for the current player without advancing the turn.
     *
     * @param count the number of cards to draw
     */
    public void drawCardsForCurrentPlayer(int count) {
        // TODO: Draw cards from the deck and give them to the current player.
        throw new UnsupportedOperationException(
                "Drawing cards is not implemented yet.");
    }

    /**
     * Draws cards for the next player without advancing the turn.
     * Intended for draw-two and draw-four effects.
     *
     * @param count the number of cards to draw
     */
    public void drawCardsForNextPlayer(int count) {
        // TODO: Find the next player in the current direction and draw cards.
        throw new UnsupportedOperationException(
                "Penalty drawing is not implemented yet.");
    }

}