package entity;

import entity.board.Board;
import entity.deck.Deck;
import entity.deck.DeckFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Game {
    private Board board;
    private HashMap<Player, Deck> drawPiles;
    private HashMap<Player, Deck> discardPiles;
    private HashMap<Player, Deck> handPiles;
    private int turn;
    private int maxTurn;

    /// Initializes a new Game
    /// @param playerNames An ArrayList of the names of the players that will be playing this game
    public Game(ArrayList<Player> playerNames) {
        board = new Board();
        drawPiles = new HashMap<>();
        discardPiles = new HashMap<>();
        handPiles = new HashMap<>();

        turn = 0;
        maxTurn = playerNames.size();

        for (Player player : playerNames) {
            discardPiles.put(player, new Deck());
            handPiles.put(player, new Deck());

            // The only non-empty deck should be the draw pile deck
            drawPiles.put(player, DeckFactory.createDeck(player));
        }
    }
    public GameResult finish() {
        List<Player> players = new ArrayList<>(drawPiles.keySet());
        Player p1 = players.get(0);
        Player p2 = players.get(1);

        int p1Score = board.calculateScore(p1);
        int p2Score = board.calculateScore(p2);

        String winnerName;
        if (p1Score > p2Score) winnerName = p1.getName();
        else if (p2Score > p1Score) winnerName = p2.getName();
        else winnerName = null;

        return new GameResult(
                p1.getName(), p2.getName(),
                p1Score, p2Score,
                winnerName, LocalDateTime.now()
        );
    }
    /// Ends the current turn
    /// TODO: Part of 'End Turn' Use Case
    public void endTurn() {}

    ///

}
