package entity;

import entity.board.Board;
import entity.deck.Deck;
import entity.deck.DeckFactory;

import java.util.ArrayList;
import java.util.HashMap;

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

    /// Ends the current turn
    /// TODO: Part of 'End Turn' Use Case
    public void endTurn() {}

    ///

}
