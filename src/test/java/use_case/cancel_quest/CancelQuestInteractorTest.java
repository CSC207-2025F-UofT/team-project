package use_case.cancel_quest;

import data_access.InMemoryUserQuestDataAccessObject;
import entity.QuestStatus;
import entity.UserQuest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.support.TestClock;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CancelQuestInteractorTest {

    private static final LocalDateTime ACCEPTED = LocalDateTime.of(2026, 11, 24, 12, 0);
    private static final LocalDateTime NOW = ACCEPTED.plusMinutes(5);

    private InMemoryUserQuestDataAccessObject userQuestData;
    private FakePresenter presenter;

    @BeforeEach
    void setUp() {
        userQuestData = new InMemoryUserQuestDataAccessObject();
        presenter = new FakePresenter();
        userQuestData.saveUserQuest(new UserQuest("daniel", "q1", ACCEPTED));
    }

    private void run(String questId) {
        new CancelQuestInteractor(userQuestData, presenter, TestClock.at(NOW))
                .execute(new CancelQuestInputData("daniel", questId));
    }

    @Test
    void successMarksQuestCancelled() {
        run("q1");

        assertNull(presenter.error);
        assertEquals("q1", presenter.success.getQuestId());
        UserQuest saved = userQuestData.getUserQuest("daniel", "q1");
        assertEquals(QuestStatus.CANCELLED, saved.getStatus());
        assertEquals(NOW, saved.getCancelledAt());
    }

    @Test
    void failsIfNotAccepted() {
        run("nope");
        assertEquals("You haven't accepted this quest.", presenter.error);
        assertNull(presenter.success);
    }

    @Test
    void failsIfAlreadyCancelled() {
        run("q1");
        presenter = new FakePresenter();
        run("q1");
        assertEquals("This quest is already cancelled.", presenter.error);
    }

    private static class FakePresenter implements CancelQuestOutputBoundary {
        CancelQuestOutputData success;
        String error;

        @Override
        public void prepareSuccessView(CancelQuestOutputData outputData) {
            success = outputData;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            error = errorMessage;
        }
    }
}
