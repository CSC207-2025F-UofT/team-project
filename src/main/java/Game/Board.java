package Game;
import Game.Move;

import java.util.ArrayList;

public class Board {
    public final int BOARD_SIZE = 20;
    public final int WIN_THRESHOLD = 5;
    private int[][] boardState;
    private int winner = 0;
    private ArrayList<Move> moves;
    /*
    0 is no player / empty tile
    >1 means there is a player move at that tile
    */

    public Board(){
        winner = 0;
        moves = new ArrayList<>();
        boardState = new int[BOARD_SIZE][BOARD_SIZE];
        for (int y = 0; y < BOARD_SIZE; y++) {
            for (int x = 0; x < BOARD_SIZE; x++) {
                boardState[x][y] = 0;
            }
        }
    }

    public boolean inBounds(int row, int column){
        return (0 <= row && row < BOARD_SIZE && 0 <= column && column < BOARD_SIZE);
    }

    public int getWinner(){
        return winner;
    }

    public int getTile(int row, int column) {
        return boardState[row][column];
    }

    public boolean makeMove(Move move){
        if (move.player != 1 && move.player != 2){
            return false;
        }
        if (!inBounds(move.row, move.column)){
            return false;
        }
        if (boardState[move.row][move.column] != 0){
            return false;
        }
        boardState[move.row][move.column] = move.player;
        if (checkWin(move.row, move.column, move.player)){
            winner = move.player;
        }
        moves.add(move);
        return true;
    }

  /**
   * @param row the players last move
   * @param column the players last move's column
   * @param player the player
   * @return whether player has a winning combination with the specified tile.
   */
    public boolean checkWin(int row, int column, int player){
        if (checkDirection(row, column, 0, 1, player) ||
                checkDirection(row, column, 1, 0, player) ||
                checkDirection(row, column, 1, 1, player) ||
                checkDirection(row, column, -1,1, player) ) {
            return true;
        }
        return false;
    }

    public boolean checkDirection(int row, int column, int dx, int dy, int player){
        int counter = 0;
        for(int i = -WIN_THRESHOLD; i <= WIN_THRESHOLD; i++) {
            if (inBounds(row + i * dx, column + i * dy)) {
                if (boardState[row + i * dx][column + i * dy] == player) {
                    counter++;
                } else {
                    counter = 0;
                }
            }
            if (counter >= WIN_THRESHOLD) {
                return true;
            }
        }
        return false;
    }

  /**
   * @return return a copy of move history (mutability safe)
   */
    public ArrayList<Move> getMoveHistory() {
        ArrayList<Move> moves_copy = new ArrayList<>();
        for (Move move : moves) {
            moves_copy.add(move.copy());
        }
        return moves_copy;
    }
}
