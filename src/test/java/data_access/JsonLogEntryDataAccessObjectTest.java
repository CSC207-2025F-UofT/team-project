package data_access;

import entities.LogEntry;
import entities.LogExerciseSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonLogEntryDataAccessObjectTest {

    @TempDir
    Path tempDir;

    @Test
    void logEntriesPersistAndQuery() {
        String path = tempDir.resolve("userdata/logs.json").toString();
        LocalDateTime t1 = LocalDateTime.of(2026, 11, 1, 9, 0);
        LocalDateTime t2 = LocalDateTime.of(2026, 11, 8, 9, 0);
        LogEntry later = new LogEntry("Legs", t2,
                List.of(new LogExerciseSet("squat", List.of(100.0), List.of(5))));
        LogEntry earlier = new LogEntry("Push Day", t1,
                List.of(new LogExerciseSet("barbell bench press", List.of(60.0, 60.0), List.of(10, 9))));

        JsonLogEntryDataAccessObject dao = new JsonLogEntryDataAccessObject(path);
        dao.save(later);
        dao.save(earlier);

        JsonLogEntryDataAccessObject reloaded = new JsonLogEntryDataAccessObject(path);
        List<LogEntry> all = reloaded.getAll();
        assertEquals(2, all.size());
        assertEquals(t1, all.get(0).getTime());
        assertEquals(List.of(10, 9), all.get(0).getExercises().get(0).getReps());

        assertEquals(1, reloaded.getBetween(t1, t1.plusDays(1)).size());
        assertEquals("Legs", reloaded.getByExerciseName("Squat").get(0).getTemplateName());

        assertTrue(reloaded.delete(t1));
        assertEquals(1, new JsonLogEntryDataAccessObject(path).getAll().size());
    }
}
