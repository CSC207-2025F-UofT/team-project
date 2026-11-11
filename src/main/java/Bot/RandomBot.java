package Bot;

import Game.Board;
import Game.Move;

import java.util.Random;

public class RandomBot implements Bot {
  int playerid;
  public RandomBot(int playerid) {
    this.playerid = playerid;
  }

  @Override
  public Move calculateMove(Board board) {
    int row = randomInt(0, board.BOARD_SIZE);
    int column = randomInt(0, board.BOARD_SIZE);
    while (board.getTile(row, column) != 0) {
      row = randomInt(0, board.BOARD_SIZE);
      column = randomInt(0, board.BOARD_SIZE);
    }
    return new Move(row, column);
  }

  private int randomInt(int min, int max){
    int random = new Random().nextInt((max - min) + 1) + min;
    return random;
  }
}
