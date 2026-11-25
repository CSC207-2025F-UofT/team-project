package use_case.find_nearby_quests;

import entity.Location;
import entity.SearchArea;
import entity.SideQuest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Runs Use Case 4: finds the side quests within the chosen radius of the user and
 * sends them, nearest first, to the presenter. Changing the radius reruns this use case.
 */
public class FindNearbyQuestsInteractor implements FindNearbyQuestsInputBoundary {
    private final QuestLocationDataAccessInterface questDataAccess;
    private final UserLocationDataAccessInterface userLocationDataAccess;
    private final FindNearbyQuestsOutputBoundary presenter;

    /**
     * Creates the interactor.
     *
     * @param questDataAccess source of quests that have a location
     * @param userLocationDataAccess source of the user's location
     * @param presenter receives the result
     */
    public FindNearbyQuestsInteractor(QuestLocationDataAccessInterface questDataAccess,
                                      UserLocationDataAccessInterface userLocationDataAccess,
                                      FindNearbyQuestsOutputBoundary presenter) {
        this.questDataAccess = questDataAccess;
        this.userLocationDataAccess = userLocationDataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(FindNearbyQuestsInputData inputData) {
        // Step 1: read the user's location and the radius.
        Location centre = userLocationDataAccess.getUserLocation();
        SearchArea area;
        try {
            area = new SearchArea(centre, inputData.getRadiusKm());
        } catch (IllegalArgumentException e) {
            // Extension 4b.
            presenter.prepareInvalidRadiusView(e.getMessage());
            return;
        }

        // Step 2: load the quests that have a location.
        List<SideQuest> quests;
        try {
            quests = questDataAccess.getQuestsWithLocation();
        } catch (QuestLoadException e) {
            // Extension 2a.
            presenter.prepareLoadFailView(
                    centre.getLatitude(), centre.getLongitude(), e.getMessage());
            return;
        }

        // Step 2: keep the ones inside the radius, nearest first.
        List<NearbyQuest> nearby = new ArrayList<>();
        for (SideQuest quest : quests) {
            Location where = quest.getLocation();
            if (where != null && area.contains(where)) {
                nearby.add(toNearbyQuest(quest, area.distanceFromCentre(where)));
            }
        }
        nearby.sort(Comparator.comparingDouble(NearbyQuest::getDistanceKm));

        // Step 3 (and 3a when nearby is empty). The edge points let the view draw the circle.
        presenter.prepareSuccessView(new FindNearbyQuestsOutputData(
                centre.getLatitude(), centre.getLongitude(), area.getRadiusKm(),
                area.northEdge().getLatitude(), area.eastEdge().getLongitude(), nearby));
    }

    private static NearbyQuest toNearbyQuest(SideQuest quest, double distanceKm) {
        Location where = quest.getLocation();
        return new NearbyQuest(quest.getId(), quest.getName(), quest.getInterests(),
                quest.getExp(), distanceKm, where.getLatitude(), where.getLongitude());
    }
}
