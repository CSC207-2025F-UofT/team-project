package entity;

import java.util.List;

public class GameMap {

    private final List<Location> locations;
    private int currentLocationIndex;

    public GameMap(List<Location> locations, int currentLocationIndex) {
        this.locations = locations;
        if (currentLocationIndex < 0 || currentLocationIndex >= locations.size()) {
            throw new IllegalArgumentException("Invalid starting index");
        }
        this.currentLocationIndex = currentLocationIndex;
    }

    public Location getCurrentLocation() {
        return locations.get(this.currentLocationIndex);
    }

    public int getCurrentLocationIndex() {
        return this.currentLocationIndex;
    }

    public boolean canMove(Direction direction) {
        switch (direction) {
            case LEFT:
                return this.currentLocationIndex > 0;
            case RIGHT:
                return this.currentLocationIndex < this.locations.size() - 1;
        }
        return false;
    }

    public boolean move(Direction direction) {
        boolean success = canMove(direction);

        if (success) {
            switch (direction) {
                case LEFT:
                    this.currentLocationIndex--;
                    break;
                case RIGHT:
                    this.currentLocationIndex++;
                    break;
            }
        }

        return success;
    }

    public int getMapSize() {
        return this.locations.size();
    }
}