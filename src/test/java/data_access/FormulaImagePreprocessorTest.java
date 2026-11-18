package data_access;

import org.junit.jupiter.api.Test;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** The model accepts images whose height (at least 128) and width are multiples of 16. */
class FormulaImagePreprocessorTest {

    /** An image of the given size and background with one thick line drawn in the given colour. */
    private static BufferedImage drawing(int width, int height, Color background, Color ink,
                                         int x1, int y1, int x2, int y2) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setColor(background);
        g.fillRect(0, 0, width, height);
        g.setColor(ink);
        g.setStroke(new BasicStroke(4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.drawLine(x1, y1, x2, y2);
        g.dispose();
        return image;
    }

    private static int gray(BufferedImage image, int x, int y) {
        return image.getRGB(x, y) & 0xFF;
    }

    private static void assertGray(BufferedImage image) {
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int rgb = image.getRGB(x, y);
                assertEquals(rgb & 0xFF, (rgb >> 8) & 0xFF);
                assertEquals(rgb & 0xFF, (rgb >> 16) & 0xFF);
            }
        }
    }

    private static void assertModelShape(BufferedImage image) {
        assertEquals(0, image.getHeight() % 16, "height a multiple of 16");
        assertTrue(image.getHeight() >= 128 && image.getHeight() <= 512, "height " + image.getHeight());
        assertEquals(0, image.getWidth() % 16, "width a multiple of 16");
        assertTrue(image.getWidth() >= 64, "width " + image.getWidth());
        assertTrue((image.getHeight() / 16) * (image.getWidth() / 16) <= 1280, "feature cells within the limit");
    }

    /** White picture with hollow black boxes drawn with a 4 px pen, each {x, y, width, height}. */
    private static BufferedImage blocks(int width, int height, int[]... blocks) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        g.setColor(Color.BLACK);
        for (int[] b : blocks) {
            g.fillRect(b[0], b[1], b[2], 4);
            g.fillRect(b[0], b[1] + b[3] - 4, b[2], 4);
            g.fillRect(b[0], b[1], 4, b[3]);
            g.fillRect(b[0] + b[2] - 4, b[1], 4, b[3]);
        }
        g.dispose();
        return image;
    }

    /** Height of the ink in the output: rows holding anything darker than mid gray. */
    private static int inkRows(BufferedImage image) {
        int rows = 0;
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                if (gray(image, x, y) < 128) {
                    rows++;
                    break;
                }
            }
        }
        return rows;
    }

    /** White picture with solid black bars, each {x, y, width, height}. */
    private static BufferedImage bars(int width, int height, int[]... bars) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        g.setColor(Color.BLACK);
        for (int[] b : bars) {
            g.fillRect(b[0], b[1], b[2], b[3]);
        }
        g.dispose();
        return image;
    }

    @Test
    void loneDotIsNotBlownUpToFillTheLine() {
        BufferedImage out = FormulaImagePreprocessor.prepare(bars(100, 100, new int[]{40, 40, 16, 16}));
        assertModelShape(out);
        assertEquals(128, out.getHeight());
        assertTrue(inkRows(out) <= 40, "dot is " + inkRows(out) + " px tall");
    }

    @Test
    void loneMinusStaysAThinBar() {
        BufferedImage out = FormulaImagePreprocessor.prepare(bars(240, 100, new int[]{40, 42, 160, 16}));
        assertModelShape(out);
        assertEquals(128, out.getHeight());
        assertTrue(inkRows(out) <= 40, "minus is " + inkRows(out) + " px thick");
    }

    @Test
    void equalsSignIsNotStretchedBarByBar() {
        BufferedImage out = FormulaImagePreprocessor.prepare(
                bars(240, 140, new int[]{40, 40, 160, 16}, new int[]{40, 84, 160, 16}));
        assertModelShape(out);
        assertEquals(128, out.getHeight(), "one line, not each bar as its own 112 px line");
    }

    @Test
    void handwrittenSizedCharIsLeftAtFullLineHeight() {
        // 16 px pen, char 10 pens tall like the canvas renders a normal glyph
        BufferedImage out = FormulaImagePreprocessor.prepare(blocks(300, 260, new int[]{40, 40, 120, 160}));
        assertTrue(inkRows(out) >= 108, "char is " + inkRows(out) + " px tall");
    }

    @Test
    void canvasImageOfAnySizeComesOutModelShaped() {
        int[][] sizes = {{74, 37}, {437, 337}, {212, 107}, {1600, 90}, {60, 600}};
        for (int[] size : sizes) {
            BufferedImage image = drawing(size[0], size[1], Color.WHITE, Color.BLACK,
                    18, 18, size[0] - 18, size[1] - 18);
            assertModelShape(FormulaImagePreprocessor.prepare(image));
        }
    }

    @Test
    void whiteBackgroundAndDarkInkAreKeptNotInverted() {
        BufferedImage out = FormulaImagePreprocessor.prepare(
                drawing(300, 120, Color.WHITE, Color.BLACK, 20, 60, 280, 60));
        assertEquals(255, gray(out, 0, 0), "background stays white");
        assertEquals(255, gray(out, out.getWidth() - 1, 127));
        int darkest = 255;
        for (int x = 0; x < out.getWidth(); x++) {
            darkest = Math.min(darkest, gray(out, x, 64));
        }
        assertTrue(darkest < 80, "the ink is still dark: " + darkest);
    }

    @Test
    void inkFitsInsideTheEightPixelMarginAndIsCentred() {
        BufferedImage out = FormulaImagePreprocessor.prepare(
                drawing(200, 400, Color.WHITE, Color.BLACK, 100, 20, 100, 380));  // tall, thin
        int top = 128;
        int bottom = -1;
        for (int y = 0; y < 128; y++) {
            for (int x = 0; x < out.getWidth(); x++) {
                if (gray(out, x, y) < 128) {
                    top = Math.min(top, y);
                    bottom = Math.max(bottom, y);
                }
            }
        }
        assertTrue(top >= 7 && bottom <= 120, "ink rows " + top + ".." + bottom);
        assertTrue(Math.abs((top + bottom) / 2.0 - 63.5) <= 2, "vertically centred");
        assertEquals(64, out.getWidth(), "narrow ink gets the minimum width");
    }

    @Test
    void veryWideSingleLineIsNotShrunkJustForBeingWide() {
        BufferedImage out = FormulaImagePreprocessor.prepare(
                drawing(4000, 60, Color.WHITE, Color.BLACK, 10, 30, 3990, 30));
        assertModelShape(out);
        assertEquals(128, out.getHeight());
        assertTrue(out.getWidth() > 1024 + 16, "wider than the old 1024 limit: " + out.getWidth());
    }

    @Test
    void twoLinesKeepTheSingleLineScaleAndMakeTheCanvasTaller() {
        BufferedImage single = FormulaImagePreprocessor.prepare(blocks(160, 80, new int[]{30, 20, 91, 41}));
        BufferedImage two = FormulaImagePreprocessor.prepare(
                blocks(160, 160, new int[]{30, 20, 91, 41}, new int[]{30, 90, 91, 41}));
        assertEquals(128, single.getHeight());
        assertEquals(272, single.getWidth());
        assertEquals(272, two.getWidth(), "same scale as one line");
        assertEquals(320, two.getHeight(), "two lines of 112 px plus the gap and margins");
        assertModelShape(two);
    }

    @Test
    void darkBackgroundComesOutWhiteWithDarkInk() {
        BufferedImage out = FormulaImagePreprocessor.prepare(
                drawing(300, 120, Color.BLACK, Color.WHITE, 20, 60, 280, 60));
        assertEquals(255, gray(out, 0, 0));
        assertTrue(gray(out, out.getWidth() / 2, 64) < 80, "ink flipped dark");
    }

    @Test
    void colouredInkTurnsDarkGrayEvenYellow() {
        for (Color colour : new Color[]{Color.RED, Color.BLUE, Color.YELLOW}) {
            BufferedImage out = FormulaImagePreprocessor.prepare(
                    drawing(300, 120, Color.WHITE, colour, 20, 60, 280, 60));
            assertModelShape(out);
            int pixel = out.getRGB(out.getWidth() / 2, 64);
            assertEquals(0, pixel & 0xFF, "darkest channel kept as the ink");
            assertGray(out);
            assertEquals(0xFFFFFF, out.getRGB(0, 0) & 0xFFFFFF);
        }
    }

    @Test
    void transparentPixelsAreCompositedOntoWhite() {
        BufferedImage image = new BufferedImage(100, 60, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setColor(new Color(255, 0, 0, 128));
        g.fillRect(20, 20, 60, 20);
        g.dispose();
        BufferedImage out = FormulaImagePreprocessor.prepare(image);
        assertTrue(Math.abs(gray(out, out.getWidth() / 2, 64) - 127) <= 1, "half see-through = mid gray");
        assertEquals(255, gray(out, 0, 0));
        assertGray(out);
    }

    @Test
    void antialiasedEdgesSurviveNoThresholding() {
        BufferedImage out = FormulaImagePreprocessor.prepare(
                drawing(300, 120, Color.WHITE, Color.BLACK, 20, 30, 280, 90));  // slanted -> soft edges
        boolean midTone = false;
        for (int y = 0; y < out.getHeight() && !midTone; y++) {
            for (int x = 0; x < out.getWidth() && !midTone; x++) {
                int v = gray(out, x, y);
                midTone = v > 40 && v < 215;
            }
        }
        assertTrue(midTone, "some in-between grays left");
    }

    @Test
    void blankImageIsRejectedWithAMessage() {
        BufferedImage blank = drawing(100, 50, Color.WHITE, Color.WHITE, 0, 0, 0, 0);
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> FormulaImagePreprocessor.prepare(blank));
        assertTrue(e.getMessage().contains("no handwriting"));
    }
}
