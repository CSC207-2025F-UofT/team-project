package entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class ApplicationTest {

    private Application newApplication() {
        return new Application("a1", "adopter1", "pet1", "House", "Had two cats", "2 adults",
                "me@example.com", "", LocalDate.of(2026, 11, 16));
    }

    @Test
    void newApplicationIsPendingAndActive() {
        final Application application = newApplication();
        assertEquals(ApplicationStatus.PENDING, application.getStatus());
        assertTrue(application.isActive());
    }

    @Test
    void missingRequiredFieldIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Application("a1", "adopter1", "pet1", " ",
                "Had two cats", "2 adults", "me@example.com", "", LocalDate.of(2026, 11, 16));
        });
    }

    @Test
    void withdrawOnlyWhilePending() {
        final Application application = newApplication();
        application.withdraw();
        assertEquals(ApplicationStatus.WITHDRAWN, application.getStatus());
        assertFalse(application.isActive());
        assertThrows(IllegalStateException.class, application::withdraw);
    }

    @Test
    void updateStatusRecordsNote() {
        final Application application = newApplication();
        application.updateStatus(ApplicationStatus.INTERVIEW, "Call us on Friday");
        assertTrue(application.isActive());
        assertEquals("Call us on Friday", application.getAdminNote());
        application.updateStatus(ApplicationStatus.REJECTED, null);
        assertFalse(application.isActive());
        assertEquals("", application.getAdminNote());
    }
}
