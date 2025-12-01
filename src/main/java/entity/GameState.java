package entity;
import java.util.ArrayList;
import java.util.List;
// TODO: Rename it
public class GameState {
    private String currentLocation;
    private final String finalDestination;
    private final List<String> inventory;
    private boolean isCompleted;

    // Constructor for NEW GAME
    public GameState(String startingLocation, String finalDestination) {
        this.currentLocation = startingLocation;
        this.finalDestination = finalDestination;
        this.inventory = new ArrayList<>();
        this.isCompleted = false;
    }

    // Getter for current location
    public String getCurrentLocation() {
        return currentLocation;
    }

    // Update current location
    public void setCurrentLocation(String newLocation) {
        this.currentLocation = newLocation;

        // If we reach the final destination → game completed
        if (newLocation.equals(finalDestination)) {
            this.isCompleted = true;
        }
    }
    //public String getDestination() {
    //    return destination;
    //}

    public String getFinalDestination() {
        return finalDestination;
    }

    public List<String> getInventory() {
        return inventory;
    }

    public void addItem(String item) {
        inventory.add(item);
    }

    public boolean isCompleted() {
        return isCompleted;
    }
}
