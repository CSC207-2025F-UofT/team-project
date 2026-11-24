package data_access;

import entities.RegularUser;
import entities.Researcher;
import entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryUserDataAccessObjectTest {

    private InMemoryUserDataAccessObject dao;

    @BeforeEach
    void setUp() {
        dao = new InMemoryUserDataAccessObject();
    }

    @Test
    void emptyDaoHasNoUsers() {
        assertFalse(dao.existsByName("jonny"));
    }

    @Test
    void getUnknownUserReturnsNull() {
        assertNull(dao.get("ghost"));
    }

    @Test
    void savedUserExistsAndCanBeRetrieved() {
        User user = new RegularUser("jonny");

        dao.save(user);

        assertTrue(dao.existsByName("jonny"));
        assertSame(user, dao.get("jonny"));
    }

    @Test
    void savedResearcherKeepsRole() {
        dao.save(new Researcher("dr_smith"));

        assertEquals("Researcher", dao.get("dr_smith").getRole());
    }

    @Test
    void savingTwoUsersKeepsBoth() {
        dao.save(new RegularUser("a"));
        dao.save(new Researcher("b"));

        assertTrue(dao.existsByName("a"));
        assertTrue(dao.existsByName("b"));
        assertEquals("Regular User", dao.get("a").getRole());
        assertEquals("Researcher", dao.get("b").getRole());
    }

    @Test
    void savingSameUsernameOverwritesPreviousUser() {
        dao.save(new RegularUser("jonny"));
        User replacement = new Researcher("jonny");

        dao.save(replacement);

        assertSame(replacement, dao.get("jonny"));
    }

    @Test
    void usernameLookupIsCaseSensitive() {
        dao.save(new RegularUser("jonny"));

        assertFalse(dao.existsByName("Jonny"));
        assertNull(dao.get("JONNY"));
    }
}
