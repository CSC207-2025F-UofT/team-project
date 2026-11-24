package data_access;

import entity.UserFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileUserDataAccessObjectTest {
    @TempDir
    Path directory;

    @Test
    void migratesLegacyAccountsAndReloadsChangedPassword() throws IOException {
        Path csv = directory.resolve("users.csv");
        Files.writeString(csv, "username,password\nalex,old-password\n");

        UserFactory factory = new UserFactory();
        FileUserDataAccessObject accounts = new FileUserDataAccessObject(csv.toString(), factory);
        assertTrue(accounts.passwordMatches("alex", "old-password"));
        assertFalse(Files.readString(csv).contains("old-password"));

        accounts.changePassword(factory.create("alex", "new,password"));
        FileUserDataAccessObject reloaded = new FileUserDataAccessObject(csv.toString(), factory);
        assertFalse(reloaded.passwordMatches("alex", "old-password"));
        assertTrue(reloaded.passwordMatches("alex", "new,password"));
        assertFalse(Files.readString(csv).contains("new,password"));
    }
}
