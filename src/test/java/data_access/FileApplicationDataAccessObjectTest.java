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
}
