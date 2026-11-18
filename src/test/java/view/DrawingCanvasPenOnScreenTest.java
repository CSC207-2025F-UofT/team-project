package view;

import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DrawingCanvasPenOnScreenTest {
    private final DrawingCanvas canvas = new DrawingCanvas();

    private void send(int id, int x, int y) {
        canvas.dispatchEvent(new MouseEvent(canvas, id, 0, 0, x, y, 1, false, MouseEvent.BUTTON1));
    }

    // dark px down the middle column = line thickness at zoom 1
    private int thickness() {
        BufferedImage img = new BufferedImage(300, 200, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 300, 200);
        canvas.paint(g);
        g.dispose();
        int n = 0;
        for (int y = 0; y < 200; y++) {
            if ((img.getRGB(150, y) & 0xff) < 100) {
                n++;
            }
        }
        return n;
    }

    @Test
    void strokeShowsThePenItWasWrittenWith() {
        canvas.setSize(300, 200);
        canvas.setPenWidth(12);
        send(MouseEvent.MOUSE_PRESSED, 50, 100);
        send(MouseEvent.MOUSE_DRAGGED, 250, 100);
        send(MouseEvent.MOUSE_RELEASED, 250, 100);
        canvas.setPenWidth(2);  // later strokes only
        assertEquals(12, thickness(), 1);
    }
}
