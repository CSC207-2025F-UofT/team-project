package data_access;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/** Shared file-writing helper for the JSON data access objects. */
final class JsonFiles {
    private JsonFiles() {
    }

    /** Writes the text to the file, creating parent directories (e.g. userdata/) if needed. */
    static void write(Path filePath, String content) {
        try {
            Path parent = filePath.toAbsolutePath().getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            Files.writeString(filePath, content, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException("Could not write " + filePath, e);
        }
    }
}
