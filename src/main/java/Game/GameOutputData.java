package Game;

public class GameOutputData {
    private final int row;
    private final int column;
    private final int player;
    public GameOutputData(int row, int column, int player) {
        this.row = row;
        this.column = column;
        this.player = player;
    }
    public int getRow(){
        return row;
    }

    public int getColumn() {
        return column;
    }

    public int getPlayer() {
        return player;
    }
}
