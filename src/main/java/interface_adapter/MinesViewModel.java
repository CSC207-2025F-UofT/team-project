
package interface_adapter;

import entity.MinesGame;

public class MinesViewModel {
    public void updateBoard(MinesGame game, boolean safe) {
        System.out.println(safe ? "Safe click!" : "Mine hit!");
    }
}