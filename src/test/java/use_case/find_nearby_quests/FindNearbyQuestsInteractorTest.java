package use_case.find_nearby_quests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import data_access.InMemorySideQuestDataAccessObject;
import entity.Location;
import entity.SideQuest;
import java.util.List;
import org.junit.jupiter.api.Test;

class FindNearbyQuestsInteractorTest {
    private static final Location UOFT = new Location(43.6629, -79.3957);

    /** Records what the interactor sent, instead of updating a real view model. */
    private static class RecordingPresenter implements FindNearbyQuestsOutputBoundary {
        private FindNearbyQuestsOutputData success;
        private String loadError;
        private double loadFailLatitude;
        private String radiusError;

        @Override
        public void prepareSuccessView(FindNearbyQuestsOutputData outputData) {
            success = outputData;
        }

        @Override
        public void prepareLoadFailView(double centreLatitude, double centreLongitude,
                                        String errorMessage) {
            loadError = errorMessage;
            loadFailLatitude = centreLatitude;
        }

        @Override
        public void prepareInvalidRadiusView(String errorMessage) {
            radiusError = errorMessage;
        }
    }

    private static SideQuest quest(String name, double latitude, double longitude) {
        return new SideQuest(name, "desc", "false", 0, 1, 10,
                new Location(latitude, longitude), List.of("Food"));
    }

    private final RecordingPresenter presenter = new RecordingPresenter();

    private FindNearbyQuestsInteractor interactorWith(QuestLocationDataAccessInterface quests) {
        return new FindNearbyQuestsInteractor(quests, () -> UOFT, presenter);
    }

    @Test
    void keepsQuestsInsideRadiusNearestFirst() {
        InMemorySideQuestDataAccessObject dao = new InMemorySideQuestDataAccessObject(List.of(
                quest("Far (CN Tower, about 2.4 km)", 43.6426, -79.3871),
                quest("Near (Queen's Park, about 0.3 km)", 43.6640, -79.3920),
                quest("Middle (Kensington, about 1 km)", 43.6547, -79.4005)));

        interactorWith(dao).execute(new FindNearbyQuestsInputData(2));

        List<NearbyQuest> nearby = presenter.success.getQuests();
        assertEquals(2, nearby.size());
        assertTrue(nearby.get(0).getTitle().startsWith("Near"));
        assertTrue(nearby.get(1).getTitle().startsWith("Middle"));
        assertTrue(nearby.get(0).getDistanceKm() < nearby.get(1).getDistanceKm());
        assertEquals(2, presenter.success.getRadiusKm());
        assertEquals(UOFT.getLatitude(), presenter.success.getCentreLatitude());
    }

    @Test
    void largerRadiusIncludesMoreQuests() {
        InMemorySideQuestDataAccessObject dao = InMemorySideQuestDataAccessObject.withSampleQuests();

        interactorWith(dao).execute(new FindNearbyQuestsInputData(2));
        int within2 = presenter.success.getQuests().size();
        interactorWith(dao).execute(new FindNearbyQuestsInputData(10));
        int within10 = presenter.success.getQuests().size();

        assertTrue(within10 > within2);
    }

    @Test
    void noQuestsInsideRadiusIsStillSuccess() {
        InMemorySideQuestDataAccessObject dao = new InMemorySideQuestDataAccessObject(List.of(
                quest("Scarborough Bluffs", 43.7056, -79.2316)));

        interactorWith(dao).execute(new FindNearbyQuestsInputData(0.5));

        assertNotNull(presenter.success);
        assertTrue(presenter.success.getQuests().isEmpty());
        assertNull(presenter.loadError);
    }

    @Test
    void invalidRadiusGoesToInvalidRadiusViewWithoutLoading() {
        boolean[] loaded = {false};
        QuestLocationDataAccessInterface dao = () -> {
            loaded[0] = true;
            return List.of();
        };

        interactorWith(dao).execute(new FindNearbyQuestsInputData(11));

        assertNotNull(presenter.radiusError);
        assertNull(presenter.success);
        assertFalse(loaded[0]);
    }

    @Test
    void loadFailureGoesToLoadFailViewWithCentre() {
        QuestLocationDataAccessInterface dao = () -> {
            throw new QuestLoadException("Could not read quests.", null);
        };

        interactorWith(dao).execute(new FindNearbyQuestsInputData(2));

        assertEquals("Could not read quests.", presenter.loadError);
        assertEquals(UOFT.getLatitude(), presenter.loadFailLatitude);
        assertNull(presenter.success);
    }
}
