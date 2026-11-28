package data_access;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import entity.UserFactory;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class FileUserDataAccessObjectLogoutTest {

    @TempDir
    Path folder;

    @Test
    void remembersAndForgetsTheCurrentUser() {
        final FileUserDataAccessObject users =
                new FileUserDataAccessObject(folder.resolve("users.json"), new UserFactory());
        assertNull(users.getCurrentUserId());

        users.setCurrentUserId("u1");
        assertEquals("u1", users.getCurrentUserId());

        users.setCurrentUserId(null);
        assertNull(users.getCurrentUserId());
    }
}
