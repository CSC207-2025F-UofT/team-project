package data_access;

import use_case.add_clothing.ImageStorageInterface;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Locale;

/**
 * Copies a picture the user picked into the app's own folder
 * (data/images/{username}/{itemId}.png) and returns that relative path
 * to store in the JSON. Copying means the picture still works if the user
 * later moves or deletes the original.
 */
public class UserImages implements ImageStorageInterface {
    private static final String DEFAULT_FOLDER = "data/images";

    private final String imagesFolder;

    /** Saves pictures under data/images. */
    public UserImages() {
        this(DEFAULT_FOLDER);
    }

    /**
     * Saves pictures under the given folder (tests use a temporary folder).
     *
     * @param imagesFolder where pictures go, e.g. "data/images"
     */
    public UserImages(String imagesFolder) {
        this.imagesFolder = imagesFolder;
    }

    @Override
    public String saveImage(String sourcePath, String username, String itemId) throws IOException {
        final Path source = Paths.get(sourcePath);
        if (!Files.isRegularFile(source)) {
            throw new IOException("Picture not found: " + sourcePath);
        }

        final String extension = extensionOf(source.getFileName().toString());
        final Path userFolder = Paths.get(imagesFolder, username);
        Files.createDirectories(userFolder);

        final String fileName = itemId + extension;
        Files.copy(source, userFolder.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);

        // Always "/" so the same path works on Mac and Windows.
        return imagesFolder + "/" + username + "/" + fileName;
    }

    /**
     * Returns the file's extension in lower case, e.g. ".png" ("" if it has none).
     */
    private static String extensionOf(String fileName) {
        final int dot = fileName.lastIndexOf('.');
        if (dot < 0) {
            return "";
        }
        return fileName.substring(dot).toLowerCase(Locale.ROOT);
    }
}
