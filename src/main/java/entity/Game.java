package entity;

import entity.board.Board;
import entity.deck.Deck;
import entity.deck.DeckFactory;

import java.util.ArrayList;
import java.util.HashMap;

public class Game {
    private Board board;
    private HashMap<String, Deck> drawPiles;
    private HashMap<String, Deck> discardPiles;
    private HashMap<String, Deck> handPiles;
    private int turn;
    private int maxTurn;

    /// Initializes a new Game
    /// @param playerNames An ArrayList of the names of the players that will be playing this game
    public Game(ArrayList<String> playerNames) {
        board = new Board();
        drawPiles = new HashMap<>();
        discardPiles = new HashMap<>();
        handPiles = new HashMap<>();

        turn = 0;
        maxTurn = playerNames.size();

        for (String playerName : playerNames) {
            discardPiles.put(playerName, new Deck());
            handPiles.put(playerName, new Deck());

            // The only non-empty deck should be the draw pile deck
            drawPiles.put(playerName, DeckFactory.createDeck(playerName));
        }
    }

    /// Ends the current turn
    /// TODO: Part of 'End Turn' Use Case
    public void endTurn() {}

    ///

}
