package entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import entity.User.Role;

class UserTest {
    private static final String HASH = "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8";

    @Test
    void adopterKeepsAllItsDetailsAndPreference() {
        final AdopterPreferences preference = new AdopterPreferences(true, false, true, false, false, Species.CAT);

        final User user = new User("Alice", "alice", "alice@example.com", HASH, Role.ADOPTER, preference);

        assertEquals("Alice", user.getName());
        assertEquals("alice", user.getId());
        assertEquals("alice@example.com", user.getEmail());
        assertEquals(HASH, user.getPasswordHash());
        assertEquals(Role.ADOPTER, user.getRole());
        assertSame(preference, user.getPreference());
    }

    @Test
    void adopterMayStartWithoutAPreference() {
        final User user = new User("Alice", "alice", "alice@example.com", HASH, Role.ADOPTER, null);

        assertNull(user.getPreference());
    }

    @Test
    void adminHasNoPreference() {
        final User admin = new User("Admin", "admin1", "admin@example.com", HASH, Role.ADMIN, null);

        assertEquals(Role.ADMIN, admin.getRole());
        assertNull(admin.getPreference());
    }

    @Test
    void adminGivenAPreferenceIsRejected() {
        final AdopterPreferences preference = new AdopterPreferences(false, false, false, false, false, null);

        final IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> new User("Admin", "admin1", "admin@example.com", HASH, Role.ADMIN, preference));

        assertEquals("ADMIN should not have a preference", error.getMessage());
    }

    @Test
    void emptyRequiredFieldsAreRejected() {
        assertEquals("Username cannot be empty", assertThrows(IllegalArgumentException.class,
                () -> new User("", "alice", "alice@example.com", HASH, Role.ADOPTER, null)).getMessage());
        assertEquals("id cannot be empty", assertThrows(IllegalArgumentException.class,
                () -> new User("Alice", "", "alice@example.com", HASH, Role.ADOPTER, null)).getMessage());
        assertEquals("email cannot be empty", assertThrows(IllegalArgumentException.class,
                () -> new User("Alice", "alice", "", HASH, Role.ADOPTER, null)).getMessage());
        assertEquals("password cannot be empty", assertThrows(IllegalArgumentException.class,
                () -> new User("Alice", "alice", "alice@example.com", "", Role.ADOPTER, null)).getMessage());
    }

    @Test
    void missingRoleIsRejected() {
        final IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> new User("Alice", "alice", "alice@example.com", HASH, null, null));

        assertEquals("role cannot be empty", error.getMessage());
    }
}
