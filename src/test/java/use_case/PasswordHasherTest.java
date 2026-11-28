package use_case;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class PasswordHasherTest {

    @Test
    void hashesWithSha256AsLowercaseHex() {
        assertEquals("5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8",
                PasswordHasher.hash("password"));
    }

    @Test
    void keepsLeadingZerosSoEveryHashHas64Characters() {
        for (int i = 0; i < 50; i++) {
            assertEquals(64, PasswordHasher.hash("password" + i).length());
        }
    }

    @Test
    void differentPasswordsGiveDifferentHashes() {
        assertNotEquals(PasswordHasher.hash("password123"), PasswordHasher.hash("password124"));
    }

    @Test
    void unknownAlgorithmIsReported() {
        final IllegalStateException error = assertThrows(IllegalStateException.class,
                () -> PasswordHasher.hash("password", "NO-SUCH-ALGORITHM"));

        assertEquals("NO-SUCH-ALGORITHM not available", error.getMessage());
    }
}
