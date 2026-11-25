package data_access;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import entity.Location;
import entity.SideQuest;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import use_case.find_nearby_quests.QuestLoadException;

class FileQuestLocationDataAccessTest {

    /** Stands in for the CSV storage so the test does not need a real file. */
    private static class FakeStorage extends FileSideQuestDataAccessObject {
        private final List<SideQuest> quests;
        private final RuntimeException failure;

        FakeStorage(List<SideQuest> quests, RuntimeException failure) {
            super("unused-in-tests.csv");
            this.quests = quests;
            this.failure = failure;
        }

        @Override
        public List<SideQuest> getAllQuests() {
            if (failure != null) {
                throw failure;
            }
            return quests;
        }
    }

    private static SideQuest quest(String id, Location location) {
        return new SideQuest(id, id, "false", "free", "under_60_minutes", 10,
                location, List.of("Food"));
    }

    @Test
    void keepsOnlyQuestsWithALocation() throws QuestLoadException {
        SideQuest withPlace = quest("park", new Location(43.6640, -79.3920));
        SideQuest noPlace = quest("online", null);
        FakeStorage storage = new FakeStorage(Arrays.asList(withPlace, noPlace, null), null);

        List<SideQuest> result = new FileQuestLocationDataAccess(storage).getQuestsWithLocation();

        assertEquals(List.of(withPlace), result);
    }

    @Test
    void emptyOrMissingListGivesNoQuests() throws QuestLoadException {
        assertTrue(new FileQuestLocationDataAccess(new FakeStorage(List.of(), null))
                .getQuestsWithLocation().isEmpty());
        assertTrue(new FileQuestLocationDataAccess(new FakeStorage(null, null))
                .getQuestsWithLocation().isEmpty());
    }

    @Test
    void storageFailureBecomesQuestLoadException() {
        FakeStorage broken = new FakeStorage(null, new IllegalStateException("bad row"));

        QuestLoadException e = assertThrows(QuestLoadException.class,
                () -> new FileQuestLocationDataAccess(broken).getQuestsWithLocation());

        assertEquals("bad row", e.getCause().getMessage());
    }
}
