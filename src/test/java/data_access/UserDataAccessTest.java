
package data_access;

import entity.Document;
import entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class UserDataAccessTest {

    @TempDir
    Path tempDir;

    private UserDataAccess userDataAccess;
    private Path userFile;

    private User alice;
    private User bob;

    @BeforeEach
    void setUp() {
        userFile = tempDir.resolve("users.csv");
        userDataAccess = new UserDataAccess(userFile.toString());

        alice = new User("alice", new ArrayList<>(), new ArrayList<>());
        bob = new User("bob", new ArrayList<>(), new ArrayList<>());
    }

    @Test
    void saveUserAndRetrieveIt() {
        userDataAccess.save(alice);

        assertTrue(userDataAccess.existsByName("alice"));
        assertSame(alice, userDataAccess.get("alice"));
    }

    @Test
    void saveMultipleUsers() {
        userDataAccess.save(alice);
        userDataAccess.save(bob);

        assertTrue(userDataAccess.existsByName("alice"));
        assertTrue(userDataAccess.existsByName("bob"));

        assertSame(alice, userDataAccess.get("alice"));
        assertSame(bob, userDataAccess.get("bob"));
    }

    @Test
    void userDoesNotExistWhenNotSaved() {
        assertFalse(userDataAccess.existsByName("unknown"));
        assertNull(userDataAccess.get("unknown"));
    }

    @Test
    void loadRestoresUsersFromFile() {
        userDataAccess.save(alice);
        userDataAccess.save(bob);

        UserDataAccess loadedDataAccess =
                new UserDataAccess(userFile.toString());

        loadedDataAccess.load();

        assertTrue(loadedDataAccess.existsByName("alice"));
        assertTrue(loadedDataAccess.existsByName("bob"));

        assertNotNull(loadedDataAccess.get("alice"));
        assertNotNull(loadedDataAccess.get("bob"));

        assertEquals("alice", loadedDataAccess.get("alice").getName());
        assertEquals("bob", loadedDataAccess.get("bob").getName());
    }

    @Test
    void saveUserUpdatesExistingUserWithSameName() {
        userDataAccess.save(alice);

        User replacementAlice =
                new User("alice", new ArrayList<>(), new ArrayList<>());

        userDataAccess.save(replacementAlice);

        assertSame(replacementAlice, userDataAccess.get("alice"));
    }

}