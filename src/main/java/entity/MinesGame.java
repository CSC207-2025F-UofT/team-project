
package entity;

import java.util.*;

public class MinesGame {
    private final int size;
    private final boolean[][] mines;
    private final boolean[][] revealed;
    private final Random rand = new Random();
    private final int mineCount;

    public MinesGame(int size, int mineCount) {
        this.size = size;
        this.mineCount = mineCount;
        mines = new boolean[size][size];
        revealed = new boolean[size][size];
        generateMines();
    }

    private void generateMines() {
        int placed = 0;
        while (placed < mineCount) {
            int x = rand.nextInt(size);
            int y = rand.nextInt(size);
            if (!mines[x][y]) {
                mines[x][y] = true;
                placed++;
            }
        }
    }

    public boolean reveal(int x, int y) {
        revealed[x][y] = true;
        return !mines[x][y];
    }

    public boolean[][] getRevealed() { return revealed; }
    public boolean[][] getMines() { return mines; }
}