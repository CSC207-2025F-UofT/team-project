package use_case.move;

import entity.Monster;

public class MoveOutputData {
    private final String currentLocationName;
    private final double latitude;
    private final double longitude;

    private final int currentIndex;
    private final int mapSize;

    private final boolean canMoveLeft;
    private final boolean canMoveRight;

    private final Monster monster;
    // private final Item item;

    public MoveOutputData(String currentLocationName, double latitude, double longitude, int currentIndex, int mapSize,
                          boolean canMoveLeft, boolean canMoveRight, Monster monster) {
//        if (monster != null && item != null) {
//            throw new IllegalArgumentException("Location cannot contain both a Monster and an Item.");
//        }
        this.currentLocationName = currentLocationName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.currentIndex = currentIndex;
        this.mapSize = mapSize;
        this.canMoveLeft = canMoveLeft;
        this.canMoveRight = canMoveRight;
        this.monster = monster;

    }

    public String getCurrentLocationName() {
        return currentLocationName;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public boolean isCanMoveLeft() {
        return canMoveLeft;
    }

    public boolean isCanMoveRight() {
        return canMoveRight;
    }

    public Monster getMonster() {
        return monster;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public int getMapSize() {
        return mapSize;
    }

//    public Item getItem() {
//        return item;
//    }
}
