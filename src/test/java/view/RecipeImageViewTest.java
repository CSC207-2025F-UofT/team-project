package view;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RecipeImageViewTest {
    private static final int TIMEOUT_SECONDS = 5;

    @BeforeAll
    static void useHeadlessMode() {
        System.setProperty("java.awt.headless", "true");
    }

    @Test
    void missingAndUnsupportedUrisKeepPlaceholder() throws Exception {
        AtomicInteger requests = new AtomicInteger();
        SwingUtilities.invokeAndWait(() -> {
            RecipeImageView view = new RecipeImageView(20, 20, uri -> {
                requests.incrementAndGet();
                return image(Color.RED);
            });
            JLabel label = label(view);
            view.setImage(null);
            assertEquals("No image", label.getText());
            assertNull(label.getIcon());
            view.setImage(URI.create("ftp://example.com/image.png"));
            assertEquals("No image", label.getText());
            assertNull(label.getIcon());
        });
        assertEquals(0, requests.get());
    }

    @Test
    void readsLocalFileAndPreservesAspectRatio(@TempDir Path directory) throws Exception {
        Path file = directory.resolve("recipe image.png");
        ImageIO.write(image(Color.RED), "png", file.toFile());
        AtomicReference<RecipeImageView> view = new AtomicReference<>();
        CountDownLatch displayed = new CountDownLatch(1);

        SwingUtilities.invokeAndWait(() -> {
            view.set(new RecipeImageView(20, 20));
            label(view.get()).addPropertyChangeListener("icon", event -> {
                if (event.getNewValue() != null) {
                    displayed.countDown();
                }
            });
            view.get().setImage(file.toUri());
        });

        assertTrue(displayed.await(TIMEOUT_SECONDS, TimeUnit.SECONDS));
        SwingUtilities.invokeAndWait(() -> {
            ImageIcon icon = (ImageIcon) label(view.get()).getIcon();
            assertNotNull(icon);
            assertEquals(20, icon.getIconWidth());
            assertEquals(10, icon.getIconHeight());
            assertEquals("", label(view.get()).getText());
        });
    }

    @Test
    void readsImageInsideJar(@TempDir Path directory) throws Exception {
        Path archive = directory.resolve("images.jar");
        ByteArrayOutputStream imageBytes = new ByteArrayOutputStream();
        ImageIO.write(image(Color.GREEN), "png", imageBytes);
        try (ZipOutputStream zip = new ZipOutputStream(Files.newOutputStream(archive))) {
            zip.putNextEntry(new ZipEntry("images/recipe.png"));
            zip.write(imageBytes.toByteArray());
            zip.closeEntry();
        }
        URI uri = URI.create("jar:" + archive.toUri() + "!/images/recipe.png");
        CountDownLatch displayed = new CountDownLatch(1);
        AtomicReference<RecipeImageView> view = new AtomicReference<>();

        SwingUtilities.invokeAndWait(() -> {
            view.set(new RecipeImageView(20, 20));
            label(view.get()).addPropertyChangeListener("icon", event -> {
                if (event.getNewValue() != null) {
                    displayed.countDown();
                }
            });
            view.get().setImage(uri);
        });

        assertTrue(displayed.await(TIMEOUT_SECONDS, TimeUnit.SECONDS));
        SwingUtilities.invokeAndWait(() -> assertNotNull(label(view.get()).getIcon()));
    }

    @Test
    void failedImageDoesNotPreventLoadingAnotherImage() throws Exception {
        CountDownLatch attempted = new CountDownLatch(1);
        CountDownLatch displayed = new CountDownLatch(1);
        AtomicReference<RecipeImageView> view = new AtomicReference<>();

        SwingUtilities.invokeAndWait(() -> {
            view.set(new RecipeImageView(20, 20, uri -> {
                if (uri.getPath().equals("/missing")) {
                    attempted.countDown();
                    throw new IOException("Image unavailable");
                }
                return image(Color.BLUE);
            }));
            label(view.get()).addPropertyChangeListener("icon", event -> {
                if (event.getNewValue() != null) {
                    displayed.countDown();
                }
            });
            view.get().setImage(URI.create("https://example.com/missing"));
        });

        assertTrue(attempted.await(TIMEOUT_SECONDS, TimeUnit.SECONDS));
        SwingUtilities.invokeAndWait(() -> {
            assertEquals("No image", label(view.get()).getText());
            view.get().setImage(URI.create("https://example.com/present"));
        });
        assertTrue(displayed.await(TIMEOUT_SECONDS, TimeUnit.SECONDS));
        SwingUtilities.invokeAndWait(() -> assertNotNull(label(view.get()).getIcon()));
    }

    @Test
    void olderRequestCannotReplaceNewerImage() throws Exception {
        CountDownLatch oldStarted = new CountDownLatch(1);
        CountDownLatch allowOldToFinish = new CountDownLatch(1);
        CountDownLatch oldFinished = new CountDownLatch(1);
        CountDownLatch newDisplayed = new CountDownLatch(1);
        AtomicReference<RecipeImageView> view = new AtomicReference<>();

        SwingUtilities.invokeAndWait(() -> {
            view.set(new RecipeImageView(20, 20, uri -> {
                if (uri.getPath().equals("/old")) {
                    oldStarted.countDown();
                    boolean interrupted = false;
                    try {
                        while (true) {
                            try {
                                if (!allowOldToFinish.await(TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                                    throw new IOException("Timed out waiting to finish old image");
                                }
                                break;
                            }
                            catch (InterruptedException exception) {
                                interrupted = true;
                            }
                        }
                        return image(Color.RED);
                    }
                    finally {
                        oldFinished.countDown();
                        if (interrupted) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
                return image(Color.BLUE);
            }));
            label(view.get()).addPropertyChangeListener("icon", event -> {
                if (event.getNewValue() != null) {
                    newDisplayed.countDown();
                }
            });
            view.get().setImage(URI.create("https://example.com/old"));
        });

        try {
            assertTrue(oldStarted.await(TIMEOUT_SECONDS, TimeUnit.SECONDS));
            SwingUtilities.invokeAndWait(() -> view.get().setImage(URI.create("https://example.com/new")));
            assertTrue(newDisplayed.await(TIMEOUT_SECONDS, TimeUnit.SECONDS));
        }
        finally {
            allowOldToFinish.countDown();
        }
        assertTrue(oldFinished.await(TIMEOUT_SECONDS, TimeUnit.SECONDS));
        SwingUtilities.invokeAndWait(() -> {
            ImageIcon icon = (ImageIcon) label(view.get()).getIcon();
            assertNotNull(icon);
            BufferedImage displayed = (BufferedImage) icon.getImage();
            assertEquals(Color.BLUE.getRGB(), displayed.getRGB(0, 0));
            assertFalse(label(view.get()).getText().equals("No image"));
        });
    }

    private static BufferedImage image(Color color) {
        BufferedImage result = new BufferedImage(4, 2, BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < result.getWidth(); x++) {
            for (int y = 0; y < result.getHeight(); y++) {
                result.setRGB(x, y, color.getRGB());
            }
        }
        return result;
    }

    private static JLabel label(RecipeImageView view) {
        return (JLabel) view.getComponent(0);
    }
}
