package data_access;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Arrays;

/**
 * Turns a picture of a handwritten formula into the input the model was trained on, the same
 * way as {@code src/neutral network/preprocess.py} ({@code load_formula_image}) with
 * {@code IMAGE_INVERT = False} from {@code train.py}:
 * RGB colours and light/dark kept as they are, cropped to the ink, scaled by the rule in
 * {@link FormulaLayout} (the tallest line of ink 112 px high, so every line of a multi-line formula
 * keeps the size a single line has), centred with an 8 px margin on a canvas whose height (at least
 * 128) and width (at least 64) are multiples of 16, the background filled with the original
 * background colour.
 *
 * <p>{@code vital_pixel.forward} only accepts {@code [B,3,H,W]} with H and W multiples of 16, so an
 * image straight from the canvas must go through here first.
 */
final class FormulaImagePreprocessor {
    private static final int INK_THRESHOLD = 32;  // difference in any RGB channel from black/white background

    private FormulaImagePreprocessor() {
    }

    /**
     * @return an RGB image at least 128 px high, height and width multiples of 16,
     *         preserving the source RGB colours like the training images
     * @throws IllegalArgumentException if there is no ink in the image
     */
    static BufferedImage prepare(BufferedImage source) {
        return prepare(source, BandRule.TALLEST_BAND);
    }

    // same, but rule picks what height counts as one line
    static BufferedImage prepare(BufferedImage source, BandRule rule) {
        int width = source.getWidth();
        int height = source.getHeight();
        int[] rgb = toRgb(source);

        // A light border means a light background (as on the canvas); the ink is then the dark part.
        boolean lightBackground = median(border(rgb, width, height)) >= 128;
        int left = width;
        int top = height;
        int right = -1;
        int bottom = -1;
        boolean[] rowHasInk = new boolean[height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = rgb[y * width + x];
                int foreground = 0;
                for (int shift = 16; shift >= 0; shift -= 8) {
                    int value = (pixel >>> shift) & 0xFF;
                    foreground = Math.max(foreground, lightBackground ? 255 - value : value);
                }
                if (foreground > INK_THRESHOLD) {
                    left = Math.min(left, x);
                    top = Math.min(top, y);
                    right = Math.max(right, x);
                    bottom = Math.max(bottom, y);
                    rowHasInk[y] = true;
                }
            }
        }
        if (right < 0) {
            throw new IllegalArgumentException("The image has no handwriting in it.");
        }
        int inkWidth = right - left + 1;
        int inkHeight = bottom - top + 1;
        BufferedImage ink = new BufferedImage(inkWidth, inkHeight, BufferedImage.TYPE_INT_RGB);
        ink.setRGB(0, 0, inkWidth, inkHeight, rgb, top * width + left, width);

        // Same scale and canvas as training (formula_layout.py).
        double tallest = rule.lineHeight(FormulaLayout.tallestBand(
                FormulaLayout.rowSpans(Arrays.copyOfRange(rowHasInk, top, bottom + 1)),
                inkHeight, FormulaLayout.GAP_MIN_PX), inkHeight);
        FormulaLayout.Size layout = FormulaLayout.size(inkWidth, inkHeight, tallest);
        int resizedWidth = Math.max(1, FormulaLayout.roundHalfUp(inkWidth * layout.scale()));
        int resizedHeight = Math.max(1, FormulaLayout.roundHalfUp(inkHeight * layout.scale()));
        BufferedImage resized = resize(ink, resizedWidth, resizedHeight);

        int canvasWidth = layout.width();
        int canvasHeight = layout.height();
        BufferedImage canvas = new BufferedImage(canvasWidth, canvasHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = canvas.createGraphics();
        int background = lightBackground ? 255 : 0;  // not inverted, so the background keeps its colour
        g.setColor(new java.awt.Color(background, background, background));
        g.fillRect(0, 0, canvasWidth, canvasHeight);
        g.drawImage(resized, (canvasWidth - resizedWidth) / 2, (canvasHeight - resizedHeight) / 2, null);
        g.dispose();
        return canvas;
    }

    /** Composite transparency onto white without discarding RGB colour information. */
    private static int[] toRgb(BufferedImage source) {
        int width = source.getWidth();
        int height = source.getHeight();
        int[] rgb = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int argb = source.getRGB(x, y);
                double alpha = ((argb >>> 24) & 0xFF) / 255.0;
                double r = ((argb >> 16) & 0xFF) * alpha + 255 * (1 - alpha);
                double gr = ((argb >> 8) & 0xFF) * alpha + 255 * (1 - alpha);
                double b = (argb & 0xFF) * alpha + 255 * (1 - alpha);
                rgb[y * width + x] = ((int) Math.round(r) << 16)
                        | ((int) Math.round(gr) << 8) | (int) Math.round(b);
            }
        }
        return rgb;
    }

    private static int[] border(int[] rgb, int width, int height) {
        int[] values = new int[3 * (2 * width + 2 * height)];
        int n = 0;
        for (int x = 0; x < width; x++) {
            for (int shift = 16; shift >= 0; shift -= 8) {
                values[n++] = (rgb[x] >>> shift) & 0xFF;
                values[n++] = (rgb[(height - 1) * width + x] >>> shift) & 0xFF;
            }
        }
        for (int y = 0; y < height; y++) {
            for (int shift = 16; shift >= 0; shift -= 8) {
                values[n++] = (rgb[y * width] >>> shift) & 0xFF;
                values[n++] = (rgb[y * width + width - 1] >>> shift) & 0xFF;
            }
        }
        return values;
    }

    private static double median(int[] values) {
        int[] sorted = values.clone();
        Arrays.sort(sorted);
        int middle = sorted.length / 2;
        return sorted.length % 2 == 1 ? sorted[middle] : (sorted[middle - 1] + sorted[middle]) / 2.0;
    }

    /** Smooth resizing: area averaging when shrinking (close to training's Lanczos), bicubic when enlarging. */
    private static BufferedImage resize(BufferedImage image, int width, int height) {
        BufferedImage out = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = out.createGraphics();
        if (width < image.getWidth() || height < image.getHeight()) {
            g.drawImage(image.getScaledInstance(width, height, Image.SCALE_AREA_AVERAGING), 0, 0, null);
        } else {
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g.drawImage(image, 0, 0, width, height, null);
        }
        g.dispose();
        return out;
    }
}
