package use_case.view_dashboard;

import data_access.InMemoryUserQuestDataAccessObject;
import entity.UserQuest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.support.FakeSideQuestDataAccess;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ViewDashboardInteractorTest {

    private static final LocalDateTime T = LocalDateTime.of(2026, 11, 24, 12, 0);

    private InMemoryUserQuestDataAccessObject userQuestData;
    private FakeSideQuestDataAccess sideQuestData;
    private FakePresenter presenter;

    @BeforeEach
    void setUp() {
        userQuestData = new InMemoryUserQuestDataAccessObject();
        sideQuestData = new FakeSideQuestDataAccess();
        presenter = new FakePresenter();
        sideQuestData.add("pho", FakeSideQuestDataAccess.quest("Try pho", 50));
        sideQuestData.add("park", FakeSideQuestDataAccess.quest("Visit a park", 25));
        sideQuestData.add("museum", FakeSideQuestDataAccess.quest("Visit the ROM", 100));
        sideQuestData.add("climb", FakeSideQuestDataAccess.quest("Try bouldering", 100));
    }

    private void run(String username) {
        new ViewDashboardInteractor(userQuestData, sideQuestData, presenter)
                .execute(new ViewDashboardInputData(username));
    }

    @Test
    void groupsByStatusNewestFirstAndTotalsExp() {
        userQuestData.saveUserQuest(new UserQuest("daniel", "climb", T));

        UserQuest pho = new UserQuest("daniel", "pho", T);
        pho.complete(T.plusHours(1));
        userQuestData.saveUserQuest(pho);

        UserQuest park = new UserQuest("daniel", "park", T);
        park.complete(T.plusHours(3));
        userQuestData.saveUserQuest(park);

        UserQuest museum = new UserQuest("daniel", "museum", T);
        museum.cancel(T.plusHours(2));
        userQuestData.saveUserQuest(museum);

        // Someone else's quest must not show up.
        userQuestData.saveUserQuest(new UserQuest("lucy", "pho", T));

        run("daniel");

        ViewDashboardOutputData out = presenter.success;
        assertNull(presenter.error);
        assertFalse(out.isEmpty());

        assertEquals(1, out.getInProgress().size());
        assertEquals("Try bouldering", out.getInProgress().get(0).getName());

        assertEquals(2, out.getTotalCompleted());
        QuestRow newest = out.getCompleted().get(0);
        assertEquals("park", newest.getQuestId());
        assertEquals("Visit a park", newest.getName());
        assertEquals(25, newest.getExp());
        assertEquals(T.plusHours(3), newest.getDate());
        assertEquals("pho", out.getCompleted().get(1).getQuestId());

        assertEquals(1, out.getCancelled().size());
        assertEquals("museum", out.getCancelled().get(0).getQuestId());

        assertEquals(75, out.getTotalExpEarned());   // cancelled quests earn nothing
    }

    @Test
    void emptyDashboardForNewUser() {
        run("daniel");
        assertTrue(presenter.success.isEmpty());
        assertEquals(0, presenter.success.getTotalCompleted());
        assertEquals(0, presenter.success.getTotalExpEarned());
    }

    @Test
    void skipsQuestsDeletedFromCatalogue() {
        userQuestData.saveUserQuest(new UserQuest("daniel", "deleted", T));
        userQuestData.saveUserQuest(new UserQuest("daniel", "pho", T));
        run("daniel");
        assertEquals(1, presenter.success.getInProgress().size());
        assertEquals("pho", presenter.success.getInProgress().get(0).getQuestId());
    }

    @Test
    void failsWithoutUsername() {
        run(" ");
        assertEquals("Please log in to see your dashboard.", presenter.error);
        presenter = new FakePresenter();
        run(null);
        assertEquals("Please log in to see your dashboard.", presenter.error);
    }

    private static class FakePresenter implements ViewDashboardOutputBoundary {
        ViewDashboardOutputData success;
        String error;

        @Override
        public void prepareSuccessView(ViewDashboardOutputData outputData) {
            success = outputData;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            error = errorMessage;
        }
    }
}
