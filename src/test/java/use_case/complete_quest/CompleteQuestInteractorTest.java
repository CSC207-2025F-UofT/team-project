package use_case.complete_quest;

import data_access.InMemoryUserQuestDataAccessObject;
import entity.QuestStatus;
import entity.User;
import entity.UserQuest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.support.FakeSideQuestDataAccess;
import use_case.support.FakeUserDataAccess;
import use_case.support.TestClock;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CompleteQuestInteractorTest {

    private static final LocalDateTime ACCEPTED = LocalDateTime.of(2026, 11, 24, 12, 0);

    private InMemoryUserQuestDataAccessObject userQuestData;
    private FakeSideQuestDataAccess sideQuestData;
    private FakeUserDataAccess userData;
    private FakePresenter presenter;

    /** Runs before every test, so each test starts fresh. */
    @BeforeEach
    void setUp() {
        userQuestData = new InMemoryUserQuestDataAccessObject();
        sideQuestData = new FakeSideQuestDataAccess();
        userData = new FakeUserDataAccess();
        presenter = new FakePresenter();

        userData.saveUser(new User("daniel", null, 90, "pw"));   // 10 EXP from level 2
        sideQuestData.add("q1", FakeSideQuestDataAccess.quest("Try pho", 50));
        userQuestData.saveUserQuest(new UserQuest("daniel", "q1", ACCEPTED));
    }

    private void run(LocalDateTime now, String username, String questId) {
        new CompleteQuestInteractor(userQuestData, sideQuestData, userData, presenter,
                TestClock.at(now))
                .execute(new CompleteQuestInputData(username, questId));
    }

    private void run(LocalDateTime now, String questId) {
        run(now, "daniel", questId);
    }

    @Test
    void successAwardsExpLevelsUpAndPromptsJournal() {
        run(ACCEPTED.plusMinutes(30), "q1");

        assertNull(presenter.error);
        CompleteQuestOutputData out = presenter.success;
        assertEquals("q1", out.getQuestId());
        assertEquals("Try pho", out.getQuestName());
        assertEquals(50, out.getExpGained());
        assertEquals(140, out.getNewTotalExp());
        assertEquals(2, out.getNewLevel());
        assertTrue(out.isLeveledUp());
        assertEquals("q1", presenter.journalPromptFor);

        assertEquals(QuestStatus.COMPLETED, userQuestData.getUserQuest("daniel", "q1").getStatus());
        assertEquals(140, userData.getUser("daniel").getExp());
    }

    @Test
    void successWithoutLevelUp() {
        userData.saveUser(new User("daniel", "pw"));
        run(ACCEPTED.plusMinutes(30), "q1");
        assertFalse(presenter.success.isLeveledUp());
    }

    @Test
    void zeroExpQuestCompletesWithoutCrashing() {
        sideQuestData.add("free", FakeSideQuestDataAccess.quest("Free walk", 0));
        userQuestData.saveUserQuest(new UserQuest("daniel", "free", ACCEPTED));
        run(ACCEPTED.plusMinutes(30), "free");
        assertEquals(0, presenter.success.getExpGained());
        assertEquals(90, userData.getUser("daniel").getExp());
    }

    @Test
    void failsIfUserNotFound() {
        run(ACCEPTED.plusMinutes(30), "ghost_user", "q1");
        assertEquals("User not found.", presenter.error);
        assertNull(presenter.success);
    }

    @Test
    void failsIfNotAccepted() {
        run(ACCEPTED.plusMinutes(30), "nope");
        assertEquals("You haven't accepted this quest.", presenter.error);
    }

    @Test
    void failsIfAlreadyCompleted() {
        run(ACCEPTED.plusMinutes(30), "q1");
        presenter = new FakePresenter();
        run(ACCEPTED.plusMinutes(60), "q1");
        assertEquals("This quest is already completed.", presenter.error);
    }

    @Test
    void failsIfQuestWasDeleted() {
        userQuestData.saveUserQuest(new UserQuest("daniel", "ghost", ACCEPTED));
        run(ACCEPTED.plusMinutes(30), "ghost");
        assertEquals("This quest no longer exists.", presenter.error);
    }

    @Test
    void failsIfTooSoonPlural() {
        run(ACCEPTED.plusMinutes(3), "q1");
        assertEquals("Too soon! Wait 7 more minutes before completing.", presenter.error);
        assertTrue(userQuestData.getUserQuest("daniel", "q1").isInProgress());
        assertEquals(90, userData.getUser("daniel").getExp());
    }

    @Test
    void failsIfTooSoonSingular() {
        run(ACCEPTED.plusMinutes(9), "q1");
        assertEquals("Too soon! Wait 1 more minute before completing.", presenter.error);
    }

    @Test
    void failsAtDailyCap() {
        LocalDateTime today = ACCEPTED.plusHours(2);
        for (int i = 0; i < CompleteQuestInteractor.MAX_COMPLETIONS_PER_DAY; i++) {
            UserQuest done = new UserQuest("daniel", "done" + i, ACCEPTED);
            done.complete(today);
            userQuestData.saveUserQuest(done);
        }
        run(today, "q1");
        assertEquals("You've hit today's limit of 5 quests. Come back tomorrow!", presenter.error);
    }

    /** Records what the interactor sent, instead of updating a real UI. */
    private static class FakePresenter implements CompleteQuestOutputBoundary {
        CompleteQuestOutputData success;
        String error;
        String journalPromptFor;

        @Override
        public void prepareSuccessView(CompleteQuestOutputData outputData) {
            success = outputData;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            error = errorMessage;
        }

        @Override
        public void promptJournal(String questId) {
            journalPromptFor = questId;
        }
    }
}
