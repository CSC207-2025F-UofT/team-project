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
}
