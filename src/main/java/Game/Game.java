package Game;

public class Game {
    private int turn;
    private final Board board;

    public Game(){
        board = new Board();
        turn = 1;
    }
    public int getTurn(){
        return turn;
    }
    public void playerInput(Move move){
        board.makeMove(move, turn);
        turn = 3-turn;
    }
}