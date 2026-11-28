package entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import entity.User.Role;

class UserFactoryTest {
    private static final String HASH = "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8";

    private final UserFactory factory = new UserFactory();

    @Test
    void createsAnAdopterWithTheGivenDetails() {
        final AdopterPreferences preference = new AdopterPreferences(false, true, false, true, false, Species.CAT);

        final User user = factory.create("Alice", "alice", "alice@example.com", HASH, Role.ADOPTER, preference);

        assertEquals("Alice", user.getName());
        assertEquals("alice", user.getId());
        assertEquals("alice@example.com", user.getEmail());
        assertEquals(HASH, user.getPasswordHash());
        assertEquals(Role.ADOPTER, user.getRole());
        assertSame(preference, user.getPreference());
    }

    @Test
    void createsAnAdminWithoutAPreference() {
        final User admin = factory.create("Admin", "admin1", "admin@example.com", HASH, Role.ADMIN, null);

        assertEquals(Role.ADMIN, admin.getRole());
        assertNull(admin.getPreference());
    }

    @Test
    void createsANewUserEachTime() {
        final User first = factory.create("Alice", "alice", "alice@example.com", HASH, Role.ADOPTER, null);
        final User second = factory.create("Alice", "alice", "alice@example.com", HASH, Role.ADOPTER, null);

        assertNotSame(first, second);
    }

    @Test
    void passesOnTheUserValidation() {
        assertThrows(IllegalArgumentException.class,
                () -> factory.create("", "alice", "alice@example.com", HASH, Role.ADOPTER, null));
        assertThrows(IllegalArgumentException.class,
                () -> factory.create("Admin", "admin1", "admin@example.com", HASH, Role.ADMIN,
                        new AdopterPreferences(false, false, false, false, false, null)));
    }
}
