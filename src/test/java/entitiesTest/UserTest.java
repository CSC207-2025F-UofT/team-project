package entitiesTest;

import entities.RegularUser;
import entities.Researcher;
import entities.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    void regularUserHasUsernameAndRole() {
        RegularUser user = new RegularUser("jonny");

        assertEquals("jonny", user.getUsername());
        assertEquals("Regular User", user.getRole());
    }

    @Test
    void researcherHasUsernameAndRole() {
        Researcher user = new Researcher("dr_smith");

        assertEquals("dr_smith", user.getUsername());
        assertEquals("Researcher", user.getRole());
    }

    @Test
    void rolesAreCorrectWhenUsedThroughUserReference() {
        User regular = new RegularUser("a");
        User researcher = new Researcher("b");

        assertEquals("Regular User", regular.getRole());
        assertEquals("Researcher", researcher.getRole());
        assertNotEquals(regular.getRole(), researcher.getRole());
    }

    @Test
    void usernameIsStoredExactlyAsGiven() {
        assertEquals("  spaced  ", new RegularUser("  spaced  ").getUsername());
    }
}
