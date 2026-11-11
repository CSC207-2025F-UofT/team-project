package Game;

public class GameOutputData {
    private final int row;
    private final int column;
    private final int player;
    private final int winner;
    public GameOutputData(int row, int column, int player, int winner) {
        this.row = row;
        this.column = column;
        this.player = player;
        this.winner = winner;
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

    public int getWinner() {
        return winner;
    }
}
