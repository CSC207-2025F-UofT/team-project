package entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserTest {
    @ParameterizedTest
    @ValueSource(strings = {"abc", "new_user_1", "aaaaaaaaaaaaaaaaaaaaaaaa"})
    void acceptsCanonicalUsernames(String username) {
        assertTrue(User.isValidUsername(username));
    }

    @ParameterizedTest
    @ValueSource(strings = {"ab", "aaaaaaaaaaaaaaaaaaaaaaaaa", "New_User", "new-user", "new user", "Kelvin"})
    void rejectsUsernamesOutsideTheCanonicalForm(String username) {
        assertFalse(User.isValidUsername(username));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " \n ", " ", "\t \u0085"})
    void rejectsBlankDisplayNamesIncludingUnicodeWhitespace(String displayName) {
        assertFalse(User.isValidDisplayName(displayName));
    }

    @Test
    void cannotBeCreatedOrRenamedInAnInvalidState() {
        final UUID id = UUID.randomUUID();
        assertThrows(IllegalArgumentException.class, () -> new User(id, "Bad Name", "Sam"));
        assertThrows(IllegalArgumentException.class, () -> new User(id, "sam", " "));

        final User user = new User(id, "sam", "Sam");
        assertThrows(IllegalArgumentException.class, () -> user.setDisplayName(" "));
        assertEquals("Sam", user.getDisplayName());
    }
}
