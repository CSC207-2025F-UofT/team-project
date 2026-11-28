package view;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import org.junit.jupiter.api.Test;

class ListingPhotoFillTest {

    private BufferedImage solid(int width, int height, Color color) {
        final BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        final Graphics2D graphics = image.createGraphics();
        graphics.setColor(color);
        graphics.fillRect(0, 0, width, height);
        graphics.dispose();
        return image;
    }

    @Test
    void photosOfAnyShapeFillTheSameBox() {
        final BufferedImage wide = ListingPhoto.fillBox(solid(800, 200, Color.RED), 200, 150);
        final BufferedImage tall = ListingPhoto.fillBox(solid(100, 600, Color.RED), 200, 150);
        final BufferedImage small = ListingPhoto.fillBox(solid(40, 30, Color.RED), 200, 150);

        for (BufferedImage box : new BufferedImage[] {wide, tall, small}) {
            assertEquals(200, box.getWidth());
            assertEquals(150, box.getHeight());
        }
    }

    @Test
    void theBoxIsFilledWithNoEmptyBorder() {
        final BufferedImage box = ListingPhoto.fillBox(solid(800, 200, Color.RED), 200, 150);

        assertEquals(Color.RED.getRGB(), box.getRGB(0, 0));
        assertEquals(Color.RED.getRGB(), box.getRGB(199, 0));
        assertEquals(Color.RED.getRGB(), box.getRGB(0, 149));
        assertEquals(Color.RED.getRGB(), box.getRGB(199, 149));
    }

    @Test
    void theMiddleOfThePhotoIsKept() {
        final BufferedImage image = solid(400, 100, Color.BLUE);
        final Graphics2D graphics = image.createGraphics();
        graphics.setColor(Color.GREEN);
        graphics.fillRect(150, 0, 100, 100);
        graphics.dispose();

        final BufferedImage box = ListingPhoto.fillBox(image, 100, 100);

        assertEquals(Color.GREEN.getRGB(), box.getRGB(50, 50));
    }
}
