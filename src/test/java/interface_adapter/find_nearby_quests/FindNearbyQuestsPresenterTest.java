package interface_adapter.find_nearby_quests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;
import use_case.find_nearby_quests.FindNearbyQuestsOutputData;
import use_case.find_nearby_quests.NearbyQuest;

class FindNearbyQuestsPresenterTest {
    private final FindNearbyQuestsViewModel viewModel = new FindNearbyQuestsViewModel(2);
    private final FindNearbyQuestsPresenter presenter = new FindNearbyQuestsPresenter(viewModel);
    private int fired;

    FindNearbyQuestsPresenterTest() {
        viewModel.addPropertyChangeListener(event -> fired++);
    }

    private static FindNearbyQuestsOutputData oneQuestAt(double radiusKm) {
        NearbyQuest quest = new NearbyQuest("id-1", "Food crawl", List.of("Food", "Outdoors"),
                50, 1.26, 43.65, -79.40);
        return new FindNearbyQuestsOutputData(43.66, -79.39, radiusKm, 43.68, -79.37,
                List.of(quest));
    }

    @Test
    void successFormatsPreviewText() {
        presenter.prepareSuccessView(oneQuestAt(2));

        FindNearbyQuestsState state = viewModel.getState();
        FindNearbyQuestsState.PinState pin = state.getPins().get(0);
        assertEquals("Food crawl", pin.getTitle());
        assertEquals("Food, Outdoors", pin.getTheme());
        assertEquals("50 XP", pin.getXpText());
        assertEquals("1.3 km", pin.getDistanceText());
        assertEquals("id-1", pin.getQuestId());
        assertTrue(state.hasCentre());
        assertTrue(state.hasCircle());
        assertEquals(43.68, state.getCircleNorthLatitude());
        assertEquals(-79.37, state.getCircleEastLongitude());
        assertNull(state.getInfoMessage());
        assertNull(state.getErrorMessage());
        assertEquals(1, fired);
    }

    @Test
    void emptyResultShowsTryLargerRadiusMessage() {
        presenter.prepareSuccessView(
                new FindNearbyQuestsOutputData(43.66, -79.39, 0.5, 43.665, -79.385, List.of()));
        assertEquals("No side quests within 0.5 km. Try a larger radius.",
                viewModel.getState().getInfoMessage());

        presenter.prepareSuccessView(
                new FindNearbyQuestsOutputData(43.66, -79.39, 2, 43.68, -79.37, List.of()));
        assertEquals("No side quests within 2 km. Try a larger radius.",
                viewModel.getState().getInfoMessage());
    }

    @Test
    void invalidRadiusKeepsPreviousPinsAndRadius() {
        presenter.prepareSuccessView(oneQuestAt(2));

        presenter.prepareInvalidRadiusView("Radius must be between 0.5 and 10.0 km.");

        FindNearbyQuestsState state = viewModel.getState();
        assertEquals(1, state.getPins().size());
        assertEquals(2, state.getRadiusKm());
        assertEquals("Radius must be between 0.5 and 10.0 km.", state.getErrorMessage());
        assertFalse(state.isRetryAvailable());
    }

    @Test
    void loadFailureClearsPinsKeepsCentreAndOffersRetry() {
        presenter.prepareSuccessView(oneQuestAt(2));

        presenter.prepareLoadFailView(43.66, -79.39, "File missing.");

        FindNearbyQuestsState state = viewModel.getState();
        assertTrue(state.getPins().isEmpty());
        assertFalse(state.hasCircle());
        assertEquals(43.66, state.getCentreLatitude());
        assertTrue(state.isRetryAvailable());
        assertEquals("Couldn't load side quests. File missing.", state.getErrorMessage());
    }

    @Test
    void successAfterFailureClearsError() {
        presenter.prepareLoadFailView(43.66, -79.39, "File missing.");
        presenter.prepareSuccessView(oneQuestAt(2));

        assertNull(viewModel.getState().getErrorMessage());
        assertFalse(viewModel.getState().isRetryAvailable());
    }
}
