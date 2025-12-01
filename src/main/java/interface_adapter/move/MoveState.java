package interface_adapter.move;

import entity.Monster;

import javax.swing.*;

public class MoveState {
    private String linearMap = "";
    private String currentLocationName = "";
    private ImageIcon staticMapImage = null;
    private boolean leftButtonEnabled = true;
    private boolean rightButtonEnabled = true;

    private Monster monster = null;
//    private Item item = null;

    public MoveState(MoveState copy) {
        this.linearMap = copy.linearMap;
        this.staticMapImage = copy.staticMapImage;
        this.currentLocationName = copy.currentLocationName;
        this.leftButtonEnabled = copy.leftButtonEnabled;
        this.rightButtonEnabled = copy.rightButtonEnabled;
        this.monster = copy.monster;
//        this.item = copy.item;
    }

    public MoveState() {}

    public String getLinearMap() {
        return linearMap;
    }

    public void setLinearMap(String linearMap) {
        this.linearMap = linearMap;
    }

    public ImageIcon getStaticMapImage() {
        return staticMapImage;
    }

    public void setStaticMapImage(ImageIcon staticMapImage) {
        this.staticMapImage = staticMapImage;
    }

    public String getCurrentLocationName() {
        return currentLocationName;
    }

    public void setCurrentLocationName(String currentLocationName) {
        this.currentLocationName = currentLocationName;
    }

    public boolean isLeftButtonEnabled() {
        return leftButtonEnabled;
    }

    public void setLeftButtonEnabled(boolean leftButtonEnabled) {
        this.leftButtonEnabled = leftButtonEnabled;
    }

    public boolean isRightButtonEnabled() {
        return rightButtonEnabled;
    }

    public void setRightButtonEnabled(boolean rightButtonEnabled) {
        this.rightButtonEnabled = rightButtonEnabled;
    }

    public Monster getMonster() {
        return monster;
    }

    public void setMonster(Monster monster) {
        this.monster = monster;
    }

//    public Item getItem() {
//        return item;
//    }
//
//    public void setItem(Item item) {
//        this.item = item;
//    }
}
