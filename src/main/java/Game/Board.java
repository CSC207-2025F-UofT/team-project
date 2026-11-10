package Game;

public class Board {
    private int[][] gameState;

    public Board(){
        gameState = new int[20][20];
    }

    public boolean inBounds(int row, int column){
        return (0 <= row && row < 20 && 0 <= column && column < 20);
    }
    public boolean move(int player, int row, int column){
        if (player != 1 && player !=2){return false;}
        if (!inBounds(row,column)){return false;}
        if (gameState[row][column] != 0){ return false;}

        gameState[row][column] = player;
        return true;
    }

    public boolean checkWin(int row, int column, int player){
        return(checkDirection(row, column, 0, 1, player) ||
            checkDirection(row, column, 1, 0, player) ||
            checkDirection(row, column, 1, 1, player) ||
            checkDirection(row, column, -1,1, player) );
    }

    public boolean checkDirection(int row, int column, int dx, int dy, int player){
        int counter = 0;
        for(int i = -4; i <= 4; i++){
            if (inBounds(row + i*dx,column + i*dy)){
                if (gameState[row + i*dx][column + i*dy] == player){
                    counter++;
                }
                else{
                    counter = 0;
                }
            }
            if (counter >= 5){
                return true;
            }
        }
        return false;
    }
}
