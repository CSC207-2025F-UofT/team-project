package Game;

public class Move {
  public final int row;
  public final int column;
  public final int player;
  public Move(int row, int column, int player) {
    this.row = row;
    this.column = column;
    this.player = player;
  }
  public Move copy(){
    return new Move(this.row, this.column, this.player);
  }
}
