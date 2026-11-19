package data_access;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.assertEquals;

// little test pictures: solid black blocks on white
final class InkPictures {
    private InkPictures() {
    }

    // each block {x, y, width, height}
    static BufferedImage blocks(int width, int height, int[]... blocks) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        g.setColor(Color.BLACK);
        for (int[] b : blocks) {
            g.fillRect(b[0], b[1], b[2], b[3]);
        }
        g.dispose();
        return image;
    }

    // numerator, bar, denominator w/ white between, so 3 bands
    static BufferedImage fraction() {
        return blocks(160, 200, new int[]{60, 20, 40, 50}, new int[]{30, 95, 100, 6}, new int[]{60, 130, 40, 50});
    }

    // 3 chars side by side, one band
    static BufferedImage oneLine() {
        return blocks(300, 80, new int[]{20, 20, 60, 40}, new int[]{100, 35, 60, 8}, new int[]{180, 20, 60, 40});
    }

    static void assertSamePixels(BufferedImage expected, BufferedImage actual) {
        assertEquals(expected.getWidth(), actual.getWidth(), "width");
        assertEquals(expected.getHeight(), actual.getHeight(), "height");
        for (int y = 0; y < expected.getHeight(); y++) {
            for (int x = 0; x < expected.getWidth(); x++) {
                assertEquals(expected.getRGB(x, y), actual.getRGB(x, y), "pixel " + x + "," + y);
            }
        }
    }
}
