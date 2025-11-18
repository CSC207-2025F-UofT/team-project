package use_case.DeleteFavouriteLocation;

import entity.location.Location;

import java.util.List;

/**
 * Output Data for the DeleteLocation Use Case.
 */
public class DeleteLocationOutputData {
    private final List<Location> remainingLocations;

    public DeleteLocationOutputData(List<Location> remainingLocations) {
        this.remainingLocations = remainingLocations;
    }

    public List<Location> getRemainingLocations() {
        return this.remainingLocations;
    }
}

