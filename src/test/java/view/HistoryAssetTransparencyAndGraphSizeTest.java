package view;

import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

/** Canvas-specific overlays must be transparent while imported graph PNGs stay native size. */
class HistoryAssetTransparencyAndGraphSizeTest {
    @Test void canvasRetainsOriginalGraphPixelsWithoutDisplayingItAt900PixelsWide() throws Exception {
        DrawingCanvas canvas = new DrawingCanvas();
        canvas.setSize(1200, 800);
        BufferedImage original = new BufferedImage(900, 600, BufferedImage.TYPE_INT_ARGB);
        original.setRGB(800, 500, 0xff2070c0);
        canvas.addHistoryAsset(original, "", "", new Point(200, 180));
        var asset = canvas.exportCanvasState().assets().get(0);
        BufferedImage saved = ImageIO.read(new ByteArrayInputStream(asset.png()));
        assertEquals(900, saved.getWidth());
        assertEquals(600, saved.getHeight());
        assertEquals(0xff2070c0, saved.getRGB(800, 500));
        assertTrue(asset.width() <= 341, "Full resolution is retained without an oversized initial card");
    }

    @Test void pdfWhitePaperIsRemovedOnlyFromTheCanvasOverlay() throws Exception {
        BufferedImage rendered = new BufferedImage(3, 1, BufferedImage.TYPE_INT_ARGB);
        rendered.setRGB(0, 0, 0xffffffff);
        rendered.setRGB(1, 0, 0xff000000);
        rendered.setRGB(2, 0, 0xffaaaaaa);
        Method convert = DrawingCanvas.class.getDeclaredMethod("transparentPaper", BufferedImage.class);
        convert.setAccessible(true);
        BufferedImage transparent = (BufferedImage) convert.invoke(null, rendered);
        assertEquals(0, transparent.getRGB(0, 0) >>> 24);
        assertEquals(255, transparent.getRGB(1, 0) >>> 24);
        assertTrue((transparent.getRGB(2, 0) >>> 24) > 0
                && (transparent.getRGB(2, 0) >>> 24) < 255,
                "Grey antialiased formula edges should remain smooth over Canvas");
        assertEquals(0xffffffff, rendered.getRGB(0, 0), "Do not change the PDF renderer/source image");
    }
}
