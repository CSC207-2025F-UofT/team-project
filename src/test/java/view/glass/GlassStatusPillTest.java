package view.glass;

import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GlassStatusPillTest {
    @Test
    void errorPillHasGlassRimAndReadableIndicator() {
        BufferedImage image = new BufferedImage(260, 42, BufferedImage.TYPE_INT_ARGB);
        var g = image.createGraphics();
        GlassStatusPill.paintError(g, image.getWidth(), image.getHeight());
        g.dispose();
        assertTrue((image.getRGB(130, 20) >>> 24) > 0);
        assertNotEquals(image.getRGB(17, 21), image.getRGB(130, 21));
    }

    @Test
    void zeroSizedPillIsSafe() {
        BufferedImage image = new BufferedImage(4, 4, BufferedImage.TYPE_INT_ARGB);
        var g = image.createGraphics();
        GlassStatusPill.paintError(g, 0, 0);
        g.dispose();
        assertTrue(true);
    }
}
