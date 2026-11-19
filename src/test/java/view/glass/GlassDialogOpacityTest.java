package view.glass;

import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.assertTrue;

/** The Save As modal must hide the ink behind it instead of leaking strokes through the fields. */
class GlassDialogOpacityTest {
    @Test
    void saveAsDialogHasNearlyOpaqueWhiteBody() {
        var input = new GlassTextField(20, "");
        input.setText("Canvas Notes");
        var pane = new GlassDialogs.DialogPane("Save Canvas", "Name", GlassDialogs.Tone.QUESTION,
                input, new String[]{"Save", "Cancel"}, ignored -> { });
        pane.setSize(450, 190);
        var image = new BufferedImage(450, 190, BufferedImage.TYPE_INT_ARGB);
        var graphics = image.createGraphics();
        pane.paint(graphics);
        graphics.dispose();
        int central = image.getRGB(200, 90);
        assertTrue((central >>> 24) >= 245);
    }

    /** With a backdrop the window can be opaque (no macOS repaint flash) and still look rounded. */
    @Test
    void backdropFillsTheCornersButNotTheBody() {
        var pane = new GlassDialogs.DialogPane("Open", "", GlassDialogs.Tone.QUESTION,
                null, new String[]{"Open", "Cancel"}, ignored -> { });
        pane.setSize(450, 190);
        var backdrop = new BufferedImage(450, 190, BufferedImage.TYPE_INT_RGB);
        var fill = backdrop.createGraphics();
        fill.setColor(new java.awt.Color(200, 30, 30));
        fill.fillRect(0, 0, 450, 190);
        fill.dispose();
        pane.backdrop = backdrop;
        var image = new BufferedImage(450, 190, BufferedImage.TYPE_INT_ARGB);
        var graphics = image.createGraphics();
        pane.paint(graphics);
        graphics.dispose();
        int corner = image.getRGB(1, 1);
        assertTrue((corner >>> 24) == 255 && ((corner >> 16) & 0xff) > 150 && ((corner >> 8) & 0xff) < 90);
        int central = image.getRGB(200, 90);
        assertTrue(((central >> 8) & 0xff) > 240);  // the white body covers the backdrop
    }
}
