package Bot;
import Game.Move;
import Game.Board;

public interface Bot {
  public Move calculateMove(Board board);
}
