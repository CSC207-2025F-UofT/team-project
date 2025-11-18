package use_case.SaveFavouriteLocation;

import entity.location.Location;

import java.util.List;

/**
 * Output Data for the SaveLocation Use Case.
 */
public class SaveLocationOutputData {
    private final List<Location> remainingLocations;

    public SaveLocationOutputData(List<Location> remainingLocations) {
        this.remainingLocations = remainingLocations;
    }

    public List<Location> getRemainingLocations() {
        return this.remainingLocations;
    }
}
