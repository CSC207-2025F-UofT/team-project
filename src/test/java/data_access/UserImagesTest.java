package data_access;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests that UserImages copies pictures into the user's folder, named by item id.
 */
class UserImagesTest {

    @TempDir
    Path temp;

    @Test
    void copiesPictureAndReturnsRelativePath() throws IOException {
        final Path original = temp.resolve("IMG_1234.PNG");
        Files.write(original, new byte[] {1, 2, 3});
        final String imagesFolder = temp.resolve("images").toString().replace('\\', '/');

        final String saved = new UserImages(imagesFolder).saveImage(original.toString(), "ginni", "c001");

        assertEquals(imagesFolder + "/ginni/c001.png", saved);
        assertTrue(Files.exists(Paths.get(saved)));
        assertArrayEquals(new byte[] {1, 2, 3}, Files.readAllBytes(Paths.get(saved)));
        assertTrue(Files.exists(original), "the original picture is left where it was");
    }

    @Test
    void missingPictureThrows() {
        final UserImages images = new UserImages(temp.toString());
        assertThrows(IOException.class,
                () -> images.saveImage(temp.resolve("nope.png").toString(), "ginni", "c001"));
    }
}
