package Game;

public class Move {
  public final int row;
  public final int column;
  public Move(int row, int column) {
    this.row = row;
    this.column = column;
  }
  public Move copy(){
    return new Move(this.row, this.column);
  }
}
