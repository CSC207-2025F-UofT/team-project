package interface_adapter.find_nearby_quests;

import use_case.find_nearby_quests.FindNearbyQuestsInputBoundary;
import use_case.find_nearby_quests.FindNearbyQuestsInputData;

/** Turns the radius the user picked into input data and starts the use case. */
public class FindNearbyQuestsController {
    private final FindNearbyQuestsInputBoundary interactor;

    /**
     * Creates the controller.
     *
     * @param interactor the use case to run
     */
    public FindNearbyQuestsController(FindNearbyQuestsInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Finds the quests within the given radius of the user.
     *
     * @param radiusKm the search radius in kilometres
     */
    public void execute(double radiusKm) {
        interactor.execute(new FindNearbyQuestsInputData(radiusKm));
    }
}
