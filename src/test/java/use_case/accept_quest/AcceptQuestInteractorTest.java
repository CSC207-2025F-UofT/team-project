package use_case.accept_quest;

import data_access.InMemoryUserQuestDataAccessObject;
import entity.QuestStatus;
import entity.UserQuest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.support.FakeSideQuestDataAccess;
import use_case.support.TestClock;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AcceptQuestInteractorTest {

    private static final LocalDateTime NOW = LocalDateTime.of(2026, 11, 24, 12, 0);

    private InMemoryUserQuestDataAccessObject userQuestData;
    private FakeSideQuestDataAccess sideQuestData;
    private FakePresenter presenter;

    @BeforeEach
    void setUp() {
        userQuestData = new InMemoryUserQuestDataAccessObject();
        sideQuestData = new FakeSideQuestDataAccess();
        presenter = new FakePresenter();
        sideQuestData.add("q1", FakeSideQuestDataAccess.quest("Try pho", 50));
    }

    private void run(String questId) {
        new AcceptQuestInteractor(userQuestData, sideQuestData, presenter, TestClock.at(NOW))
                .execute(new AcceptQuestInputData("daniel", questId));
    }

    @Test
    void successSavesInProgressQuestStartingNow() {
        run("q1");

        assertNull(presenter.error);
        assertEquals("q1", presenter.success.getQuestId());
        assertEquals("Try pho", presenter.success.getQuestName());

        UserQuest saved = userQuestData.getUserQuest("daniel", "q1");
        assertEquals(QuestStatus.IN_PROGRESS, saved.getStatus());
        assertEquals(NOW, saved.getStartedAt());
    }

    @Test
    void failsIfQuestDoesNotExist() {
        run("nope");
        assertEquals("This quest no longer exists.", presenter.error);
        assertNull(userQuestData.getUserQuest("daniel", "nope"));
    }

    @Test
    void failsIfAlreadyInProgress() {
        run("q1");
        presenter = new FakePresenter();
        run("q1");
        assertEquals("You're already doing this quest.", presenter.error);
    }

    @Test
    void failsIfAlreadyCompleted() {
        UserQuest done = new UserQuest("daniel", "q1", NOW.minusHours(2));
        done.complete(NOW.minusHours(1));
        userQuestData.saveUserQuest(done);

        run("q1");
        assertEquals("You've already completed this quest.", presenter.error);
        assertEquals(QuestStatus.COMPLETED, userQuestData.getUserQuest("daniel", "q1").getStatus());
    }

    @Test
    void cancelledQuestCanBeAcceptedAgain() {
        UserQuest gaveUp = new UserQuest("daniel", "q1", NOW.minusHours(2));
        gaveUp.cancel(NOW.minusHours(1));
        userQuestData.saveUserQuest(gaveUp);

        run("q1");
        assertNull(presenter.error);
        UserQuest saved = userQuestData.getUserQuest("daniel", "q1");
        assertEquals(QuestStatus.IN_PROGRESS, saved.getStatus());
        assertEquals(NOW, saved.getStartedAt());
    }

    private static class FakePresenter implements AcceptQuestOutputBoundary {
        AcceptQuestOutputData success;
        String error;

        @Override
        public void prepareSuccessView(AcceptQuestOutputData outputData) {
            success = outputData;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            error = errorMessage;
        }
    }
}
