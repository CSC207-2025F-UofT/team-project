package data_access;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import entity.AdopterPreferences;

class FileAdopterPreferencesDataAccessObjectTest {
    @TempDir
    Path folder;

    private final AdopterPreferences prefs = new AdopterPreferences(true, false, true, false, true, null);

    @Test
    void nothingSavedYetIsEmpty() {
        final FileAdopterPreferencesDataAccessObject dao =
                new FileAdopterPreferencesDataAccessObject(folder.resolve("p.json"));

        assertEquals(Optional.empty(), dao.findByAdopterId("a1"));
    }

    @Test
    void savedPreferencesComeBackAfterReopeningTheFile() {
        final Path file = folder.resolve("p.json");
        new FileAdopterPreferencesDataAccessObject(file).save("a1", prefs);

        final FileAdopterPreferencesDataAccessObject reopened = new FileAdopterPreferencesDataAccessObject(file);

        assertEquals(Optional.of(prefs), reopened.findByAdopterId("a1"));
    }

    @Test
    void savingAgainReplacesTheOldValue() {
        final FileAdopterPreferencesDataAccessObject dao =
                new FileAdopterPreferencesDataAccessObject(folder.resolve("p.json"));
        dao.save("a1", prefs);
        final AdopterPreferences changed = new AdopterPreferences(false, false, false, false, false, null);

        dao.save("a1", changed);

        assertEquals(Optional.of(changed), dao.findByAdopterId("a1"));
    }

    @Test
    void adoptersAreKeptApart() {
        final FileAdopterPreferencesDataAccessObject dao =
                new FileAdopterPreferencesDataAccessObject(folder.resolve("p.json"));
        dao.save("a1", prefs);

        assertTrue(dao.findByAdopterId("a2").isEmpty());
    }

    @Test
    void anUnreadableFileIsTreatedAsNothingSaved() throws IOException {
        final Path file = folder.resolve("p.json");
        Files.writeString(file, "not json");

        assertTrue(new FileAdopterPreferencesDataAccessObject(file).findByAdopterId("a1").isEmpty());
    }
}
