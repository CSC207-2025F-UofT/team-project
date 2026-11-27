package data_access;

import use_case.add_clothing.ImageStore;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

/** Copies chosen images into a local folder so they survive the original being moved/deleted. */
public final class LocalImageStore implements ImageStore {
    private final Path directory;

    public LocalImageStore(Path directory) { this.directory = directory; }

    @Override public String store(String sourcePath) {
        String unreadable = "The image could not be read. Please choose another image.";
        Path source;
        try {
            source = Path.of(sourcePath);
            if (!Files.isRegularFile(source) || ImageIO.read(source.toFile()) == null) {
                throw new IllegalArgumentException(unreadable);
            }
        } catch (IOException ex) {
            throw new IllegalArgumentException(unreadable);
        }
        String fileName = source.getFileName().toString();
        int dot = fileName.lastIndexOf('.');
        String extension = dot >= 0 ? fileName.substring(dot).toLowerCase() : "";
        try {
            Files.createDirectories(directory);
            Path destination = directory.resolve(UUID.randomUUID() + extension);
            Files.copy(source, destination);
            return destination.toAbsolutePath().toString();
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }
}
