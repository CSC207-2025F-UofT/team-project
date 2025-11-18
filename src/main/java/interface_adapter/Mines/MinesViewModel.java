
package interface_adapter.Mines;

import entity.MinesGame;

public class MinesViewModel {
    public void updateBoard(MinesGame game, boolean safe) {
        System.out.println(safe ? "Safe click!" : "Mine hit!");
    }
}