package use_case.leaderboard;

import entity.LeaderboardEntry;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class LeaderboardInteractorTest {

    // Fake storage: just returns whatever list it was given.
    private static class InMemoryLeaderboardDataAccess implements LeaderboardDataAccessInterface {
        private final List<LeaderboardEntry> entries;

        InMemoryLeaderboardDataAccess(List<LeaderboardEntry> entries) {
            this.entries = entries;
        }

        @Override
        public List<LeaderboardEntry> getAllEntries() {
            return entries;
        }
    }

    // Fake presenter: remembers the output data it receives.
    private static class RecordingPresenter implements LeaderboardOutputBoundary {
        LeaderboardOutputData outputData;

        @Override
        public void prepareSuccessView(LeaderboardOutputData outputData) {
            this.outputData = outputData;
        }
    }

    @Test
    void entriesAreRankedByDeepestFloor() {
        // Arrange: three players, deliberately NOT in order
        InMemoryLeaderboardDataAccess dataAccess = new InMemoryLeaderboardDataAccess(Arrays.asList(
                new LeaderboardEntry("emily", 2),
                new LeaderboardEntry("darius", 5),
                new LeaderboardEntry("carter", 3)));
        RecordingPresenter presenter = new RecordingPresenter();
        LeaderboardInteractor interactor = new LeaderboardInteractor(dataAccess, presenter);

        // Act
        interactor.execute();

        // Assert: deepest floor first, and names stay matched with their floors
        assertEquals(Arrays.asList("darius", "carter", "emily"), presenter.outputData.getUsernames());
        assertEquals(Arrays.asList(5, 3, 2), presenter.outputData.getFloors());
    }

    @Test
    void emptyLeaderboard() {
        // Arrange: nobody has saved yet
        InMemoryLeaderboardDataAccess dataAccess = new InMemoryLeaderboardDataAccess(new ArrayList<>());
        RecordingPresenter presenter = new RecordingPresenter();
        LeaderboardInteractor interactor = new LeaderboardInteractor(dataAccess, presenter);

        // Act
        interactor.execute();

        // Assert: the presenter was still called, just with empty lists
        assertNotNull(presenter.outputData);
        assertTrue(presenter.outputData.getUsernames().isEmpty());
        assertTrue(presenter.outputData.getFloors().isEmpty());
    }
}