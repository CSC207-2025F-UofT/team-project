package data_access;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import entity.Adopter;

class FileDataAccessTest {
    private final Sha256PasswordHasher hasher = new Sha256PasswordHasher();

    @Test
    void adoptersSurviveRestart() throws IOException {
        final Path dir = Files.createTempDirectory("pets");
        final String csv = dir.resolve("adopters.csv").toString();

        new FileAdopterDataAccessObject(csv).save(new Adopter("amy", hasher.hash("secret123")));

        // A brand-new object reads the same file, like reopening the program.
        final FileAdopterDataAccessObject reloaded = new FileAdopterDataAccessObject(csv);
        assertTrue(reloaded.existsByUsername("amy"));
        assertTrue(hasher.matches("secret123", reloaded.get("amy").getPasswordHash()));
        assertFalse(Files.readString(Path.of(csv)).contains("secret123"));
    }

    @Test
    void adminFileCreatedFromDefaultPassword() throws IOException {
        final Path file = Files.createTempDirectory("pets").resolve("admin.txt");

        final FileAdminDataAccessObject dao = new FileAdminDataAccessObject(file.toString(), "admin123", hasher);
        assertTrue(Files.exists(file));
        assertTrue(hasher.matches("admin123", dao.getAdmin().getPasswordHash()));

        // Second start: the existing file is used, the default is ignored.
        final FileAdminDataAccessObject again = new FileAdminDataAccessObject(file.toString(), "other", hasher);
        assertTrue(hasher.matches("admin123", again.getAdmin().getPasswordHash()));
    }

    @Test
    void samePasswordGetsDifferentHashes() {
        final String a = hasher.hash("secret123");
        final String b = hasher.hash("secret123");
        assertNotEquals(a, b);
        assertTrue(hasher.matches("secret123", a));
        assertFalse(hasher.matches("secret124", a));
        assertFalse(hasher.matches("secret123", "garbage"));
    }
}
