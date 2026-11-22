package data_access;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import entity.Application;
import entity.ApplicationStatus;

class FileApplicationDataAccessObjectTest {

    private static final String ADOPTER = "bob";
    private static final String FILE_NAME = "saved.json";

    @TempDir
    Path folder;

    @Test
    void applicationsAreKeptAfterRestart() {
        final String path = folder.resolve("applications.json").toString();
        final FileApplicationDataAccessObject first = new FileApplicationDataAccessObject(path);
        final Application application = new Application("a1", "adopter1", "mochi", "House", "Had two cats",
                "2 adults", "me@example.com", "I work from home", LocalDate.of(2026, 11, 16));
        application.updateStatus(ApplicationStatus.INTERVIEW, "Call us on Friday");
        first.save(application);

        final FileApplicationDataAccessObject reloaded = new FileApplicationDataAccessObject(path);
        final List<Application> loaded = reloaded.getApplicationsByAdopter("adopter1");
        assertEquals(1, loaded.size());
        assertEquals("mochi", loaded.get(0).getPetId());
        assertEquals(ApplicationStatus.INTERVIEW, loaded.get(0).getStatus());
        assertEquals("Call us on Friday", loaded.get(0).getAdminNote());
        assertEquals(LocalDate.of(2026, 11, 16), loaded.get(0).getSubmittedAt());
        assertTrue(reloaded.hasActiveApplication("adopter1", "mochi"));
        assertFalse(reloaded.hasActiveApplication("adopter1", "tofu"));
        assertTrue(reloaded.getApplicationsByAdopter("someone-else").isEmpty());
    }

    @Test
    void savedStatusChangeReplacesTheOldRecord() {
        final String path = folder.resolve(FILE_NAME).toString();
        final FileApplicationDataAccessObject dataAccess = new FileApplicationDataAccessObject(path);
        dataAccess.save(pendingApplication("first"));
        dataAccess.save(pendingApplication("second"));

        final Application changed = dataAccess.getApplicationsByAdopter(ADOPTER).get(0);
        changed.updateStatus(ApplicationStatus.APPROVED, "Welcome home");
        dataAccess.save(changed);

        final List<Application> loaded =
                new FileApplicationDataAccessObject(path).getApplicationsByAdopter(ADOPTER);
        assertEquals(2, loaded.size());
        assertEquals("first", loaded.get(0).getId());
        assertEquals(ApplicationStatus.APPROVED, loaded.get(0).getStatus());
        assertEquals("Welcome home", loaded.get(0).getAdminNote());
        assertEquals(ApplicationStatus.PENDING, loaded.get(1).getStatus());
    }

    private Application pendingApplication(String id) {
        return new Application(id, ADOPTER, "tama", "Condo", "One cat", "1 adult",
                "bob@example.com", "", LocalDate.of(2026, 11, 20));
    }
}
