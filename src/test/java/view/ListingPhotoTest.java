package view;

import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import data_access.InMemoryPetDataAccess;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ListingPhotoTest {
    @Test
    void eachDefaultAvailableCatHasAReadableBundledPhoto() throws Exception {
        for (var cat : InMemoryPetDataAccess.withSampleData().getAvailablePets()) {
            assertNotNull(ListingPhoto.read(cat.getPhotoPath()), cat.getName());
        }
    }

    @Test
    void rendersSavedPhotoAndClearsItForAnEmptyListing(@TempDir Path directory) throws Exception {
        final Path path = directory.resolve("cat.png");
        ImageIO.write(new BufferedImage(200, 100, BufferedImage.TYPE_INT_RGB), "png", path.toFile());
        final JLabel label = new JLabel();
        final CountDownLatch loaded = new CountDownLatch(1);
        SwingUtilities.invokeAndWait(() -> {
            label.addPropertyChangeListener("icon", event -> {
                if (event.getNewValue() != null) {
                    loaded.countDown();
                }
            });
            ListingPhoto.show(label, path.toString(), 120, 100);
        });
        assertTrue(loaded.await(5, TimeUnit.SECONDS));
        SwingUtilities.invokeAndWait(() -> {
            assertEquals(120, label.getIcon().getIconWidth());
            assertEquals(60, label.getIcon().getIconHeight());
            ListingPhoto.show(label, "", 120, 100);
            assertNull(label.getIcon());
            assertEquals("No photo yet", label.getText());
        });
    }
}
