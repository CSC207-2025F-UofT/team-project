package Game;

public class Game {
    private int turn;
    private final Board board;

    public Game(boolean computer){
        board = new Board();
        turn = 1;
    }

    public void playerInput(Move move){
        board.makeMove(move);
        turn = 3-turn;
    }
}