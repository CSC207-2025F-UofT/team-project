package use_case.deleteFavouriteLocation;

import java.util.List;

/**
 * Output Data for the DeleteLocation Use Case.
 */
public class DeleteLocationOutputData {
    private final List<String> remainingLocations;

    public DeleteLocationOutputData(List<String> remainingLocations) {
        this.remainingLocations = remainingLocations;
    }

    public List<String> getRemainingLocations() {
        return this.remainingLocations;
    }
}

