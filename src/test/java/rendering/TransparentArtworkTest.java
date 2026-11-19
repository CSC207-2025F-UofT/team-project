package rendering;

import org.junit.jupiter.api.Test;
import java.awt.Color;
import java.awt.image.BufferedImage;
import static org.junit.jupiter.api.Assertions.*;

class TransparentArtworkTest {
    @Test void whiteBackedChartLosesOnlyItsPaperNotItsPlot() {
        BufferedImage chart = new BufferedImage(80, 50, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < chart.getHeight(); y++)
            for (int x = 0; x < chart.getWidth(); x++) chart.setRGB(x, y, Color.WHITE.getRGB());
        chart.setRGB(21, 24, Color.BLUE.getRGB());
        chart.setRGB(22, 24, new Color(200, 200, 200).getRGB());
        BufferedImage rendered = TransparentArtwork.removeWhiteMatte(chart);
        assertEquals(80, rendered.getWidth());
        assertEquals(0, rendered.getRGB(0, 0) >>> 24);
        assertEquals(0, rendered.getRGB(60, 25) >>> 24);
        assertEquals(Color.BLUE.getRGB(), rendered.getRGB(21, 24));
        assertTrue((rendered.getRGB(22, 24) >>> 24) > 0,
                "Antialiased gray axes should remain visible without opaque white paper");
        assertEquals(Color.WHITE.getRGB(), chart.getRGB(0, 0),
                "The original image stays unchanged for persistence and the graph producer");
    }

    @Test void existingTransparentInkIsNotModified() {
        BufferedImage ink = new BufferedImage(40, 20, BufferedImage.TYPE_INT_ARGB);
        ink.setRGB(5, 5, new Color(255, 255, 255, 180).getRGB());
        ink.setRGB(6, 5, new Color(255, 0, 0, 80).getRGB());
        assertSame(ink, TransparentArtwork.removeWhiteMatte(ink));
        assertEquals(180, ink.getRGB(5, 5) >>> 24);
        assertEquals(80, ink.getRGB(6, 5) >>> 24);
    }

    @Test void photographsAndNonWhiteBackdropsStayOpaque() {
        BufferedImage photo = new BufferedImage(30, 30, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < 30; y++) for (int x = 0; x < 30; x++)
            photo.setRGB(x, y, Color.GREEN.getRGB());
        photo.setRGB(15, 15, Color.WHITE.getRGB());
        assertSame(photo, TransparentArtwork.removeWhiteMatte(photo));
        assertEquals(255, photo.getRGB(15, 15) >>> 24);
    }
}
