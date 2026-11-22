package data_access;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import entity.Application;
import entity.ApplicationStatus;

class InMemoryApplicationDataAccessObjectTest {

    private static final String ADOPTER = "bob";

    @Test
    void savingAgainReplacesTheOldRecord() {
        final InMemoryApplicationDataAccessObject dataAccess = new InMemoryApplicationDataAccessObject();
        final Application application = pendingApplication();
        dataAccess.save(application);

        application.withdraw();
        dataAccess.save(application);

        assertEquals(1, dataAccess.getApplicationsByAdopter(ADOPTER).size());
        assertEquals(ApplicationStatus.WITHDRAWN, dataAccess.getApplicationsByAdopter(ADOPTER).get(0).getStatus());
    }

    private Application pendingApplication() {
        return new Application("a1", ADOPTER, "mochi", "House", "Had two cats",
                "2 adults", "me@example.com", "", LocalDate.of(2026, 11, 16));
    }
}
