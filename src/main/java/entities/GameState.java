package entities;
import java.io.Serializable;

public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Board board;
    private final Player player1;
    private final Player player2;
    private int currentTurn; // 0 = player1, 1 = player2

    public GameState(Player p1, Player p2){
        this.board = new Board();
        this.player1 = p1;
        this.player2 = p2;
        this.currentTurn = 0;
    }

    public Board getBoard(){
        return board;
    }

    public Player getPlayer1(){
        return player1;
    }

    public Player getPlayer2(){
        return player2;
    }

    public int getCurrentTurn(){
        return currentTurn;
    }

    public void nextTurn(){
        currentTurn = 1 - currentTurn; // when currentTurn = 0, Player1 plays. When he finishes, currentTurn = 1 - 0 = 1, Player2 plays.
    }
}
