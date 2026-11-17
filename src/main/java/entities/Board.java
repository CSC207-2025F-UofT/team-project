package entities;

public class Board {
    public static final int SIZE = 6;
    private final Character[][] grid;

    public Board(){
        this.grid = new Character[SIZE][SIZE];
    }

    public Character get(int row, int col){
        return grid[row][col];
    }

    public void set(int row, int col, Character c){
        grid[row][col] = c;
    }

    public boolean isEmpty(int row, int col){
        return grid[row][col] == null;
    }
}
