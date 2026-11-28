package app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ApplicationDataFolderTest {

    @TempDir
    Path folder;

    @Test
    void applicationsAreKeptInTheDataFolder() throws IOException {
        final Path data = folder.resolve("data");
        Files.createDirectories(data);

        final Path file = AppBuilder.applicationFileIn(data, folder.resolve("start-a/applications.json"));

        assertEquals(data.resolve("applications.json"), file);
    }

    @Test
    void startingFromAnotherFolderReadsTheSameFile() throws IOException {
        final Path data = folder.resolve("data");
        Files.createDirectories(data);

        final Path first = AppBuilder.applicationFileIn(data, folder.resolve("start-a/applications.json"));
        final Path second = AppBuilder.applicationFileIn(data, folder.resolve("start-b/applications.json"));

        assertEquals(first, second);
    }

    @Test
    void anOldApplicationsFileIsCopiedOnceAndKept() throws IOException {
        final Path data = folder.resolve("data");
        Files.createDirectories(data);
        final Path old = folder.resolve("applications.json");
        Files.writeString(old, "[]");

        final Path file = AppBuilder.applicationFileIn(data, old);

        assertEquals("[]", Files.readString(file));
        assertTrue(Files.exists(old));
    }

    @Test
    void anExistingDataFileIsNotOverwritten() throws IOException {
        final Path data = folder.resolve("data");
        Files.createDirectories(data);
        Files.writeString(data.resolve("applications.json"), "[1]");
        final Path old = folder.resolve("applications.json");
        Files.writeString(old, "[]");

        final Path file = AppBuilder.applicationFileIn(data, old);

        assertEquals("[1]", Files.readString(file));
    }

    @Test
    void theDataFolderCanBeConfigured() {
        assertEquals(Path.of("/tmp/pawportal-demo"), Main.dataDirectory(" /tmp/pawportal-demo "));
        assertEquals(Path.of(System.getProperty("user.home"), ".pawportal"), Main.dataDirectory(null));
        assertEquals(Path.of(System.getProperty("user.home"), ".pawportal"), Main.dataDirectory(" "));
    }
}
