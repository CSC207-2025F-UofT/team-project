package entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserQuestTest {

    private static final LocalDateTime START = LocalDateTime.of(2026, 11, 24, 12, 0);

    @Test
    void newQuestIsInProgress() {
        UserQuest uq = new UserQuest("daniel", "q1", START);
        assertEquals("daniel", uq.getUsername());
        assertEquals("q1", uq.getQuestId());
        assertEquals(START, uq.getStartedAt());
        assertEquals(QuestStatus.IN_PROGRESS, uq.getStatus());
        assertTrue(uq.isInProgress());
        assertNull(uq.getCompletedAt());
        assertNull(uq.getCancelledAt());
        assertEquals(START, uq.getLastUpdated());
    }

    @Test
    void minimumTimeNotMetRightAfterStarting() {
        UserQuest uq = new UserQuest("daniel", "q1", START);
        assertFalse(uq.meetsMinimumTime(START.plusMinutes(3)));
        assertEquals(7, uq.minutesUntilCompletable(START.plusMinutes(3)));
    }

    @Test
    void minimumTimeMetAfterTenMinutes() {
        UserQuest uq = new UserQuest("daniel", "q1", START);
        assertTrue(uq.meetsMinimumTime(START.plusMinutes(10)));
        assertEquals(0, uq.minutesUntilCompletable(START.plusHours(5)));
    }

    @Test
    void completeSetsStatusAndTime() {
        UserQuest uq = new UserQuest("daniel", "q1", START);
        uq.complete(START.plusMinutes(30));
        assertEquals(QuestStatus.COMPLETED, uq.getStatus());
        assertEquals(START.plusMinutes(30), uq.getCompletedAt());
        assertEquals(START.plusMinutes(30), uq.getLastUpdated());
        assertFalse(uq.isInProgress());
    }

    @Test
    void cancelSetsStatusAndTime() {
        UserQuest uq = new UserQuest("daniel", "q1", START);
        uq.cancel(START.plusMinutes(1));
        assertEquals(QuestStatus.CANCELLED, uq.getStatus());
        assertEquals(START.plusMinutes(1), uq.getCancelledAt());
        assertEquals(START.plusMinutes(1), uq.getLastUpdated());
    }

    @Test
    void cannotCompleteTwice() {
        UserQuest uq = new UserQuest("daniel", "q1", START);
        uq.complete(START.plusMinutes(30));
        assertThrows(IllegalStateException.class, () -> uq.complete(START.plusMinutes(40)));
    }

    @Test
    void cannotCancelCompletedQuest() {
        UserQuest uq = new UserQuest("daniel", "q1", START);
        uq.complete(START.plusMinutes(30));
        assertThrows(IllegalStateException.class, () -> uq.cancel(START.plusMinutes(40)));
    }

    @Test
    void rejectsMissingValues() {
        assertThrows(IllegalArgumentException.class, () -> new UserQuest(null, "q1", START));
        assertThrows(IllegalArgumentException.class, () -> new UserQuest(" ", "q1", START));
        assertThrows(IllegalArgumentException.class, () -> new UserQuest("daniel", null, START));
        assertThrows(IllegalArgumentException.class, () -> new UserQuest("daniel", "", START));
        assertThrows(IllegalArgumentException.class, () -> new UserQuest("daniel", "q1", null));
        assertThrows(IllegalArgumentException.class, () ->
                new UserQuest("daniel", "q1", null, START, null, null));
    }
}
