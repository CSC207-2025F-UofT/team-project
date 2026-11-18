package data_access;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

/**
 * Turns a picture of a handwritten formula into the model's input. Scale and canvas follow
 * {@link FormulaLayout} (shared with {@code src/neutral network/formula_layout.py}): the tallest line
 * of ink 112 px high so every line of a multi-line formula keeps single-line size, one uniform scale,
 * centred with at least 8 px of margin on a canvas whose sides are multiples of 16 (padding only).
 * Java-only extra: ink that is nothing but marks (every band under {@value #MARK_PENS} pen widths
 * tall: a dot, a minus, an equals sign) is sized as if it sat in a line {@value #GLYPH_PENS} pens
 * tall instead of being blown up to fill it. Anything with a real character in it is untouched.
 *
 * <p>The output is gray dark ink on white with its antialiasing kept, never thresholded, and resized
 * exactly once. Canvas formulas come in already drawn from the strokes at a high fixed scale, so this
 * normally only shrinks; a small outside picture is the one case that still gets enlarged.
 */
final class FormulaImagePreprocessor {
    // bands thinner than this many pens are just dots / dashes, not a line of chars
    static final double MARK_PENS = 3;
    // made-up line height (in pens) for ink thats only marks, about a normal handwritten char
    static final double GLYPH_PENS = 8;

    private FormulaImagePreprocessor() {
    }

    /**
     * @return a gray-on-white RGB image (r=g=b) at least 128 px high, sides multiples of 16
     * @throws IllegalArgumentException if there is no ink in the image
     */
    static BufferedImage prepare(BufferedImage source) {
        GrayInk gray = GrayInk.of(source);
        Rectangle box = gray.bounds();
        if (box == null) {
            throw new IllegalArgumentException("The image has no handwriting in it.");
        }
        double tallest = lineHeight(gray, box);
        FormulaLayout.Size layout = FormulaLayout.size(box.width, box.height, tallest);
        int resizedWidth = Math.max(1, FormulaLayout.roundHalfUp(box.width * layout.scale()));
        int resizedHeight = Math.max(1, FormulaLayout.roundHalfUp(box.height * layout.scale()));
        BufferedImage resized = resize(gray.crop(box), resizedWidth, resizedHeight);

        BufferedImage canvas = new BufferedImage(layout.width(), layout.height(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = canvas.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, layout.width(), layout.height());
        g.drawImage(resized, (layout.width() - resizedWidth) / 2, (layout.height() - resizedHeight) / 2, null);
        g.dispose();
        return canvas;
    }

    // tallest band, unless its all dots/dashes, then a normal char height so they dont fill the canvas
    private static double lineHeight(GrayInk gray, Rectangle box) {
        double tallest = FormulaLayout.tallestBand(
                FormulaLayout.rowSpans(gray.rowsWithInk(box)), box.height, FormulaLayout.GAP_MIN_PX);
        double pen = gray.penWidth(box);
        return tallest < MARK_PENS * pen ? GLYPH_PENS * pen : tallest;
    }

    // area averaging to shrink (close to training's lanczos), bicubic only for a too-small outside pic
    private static BufferedImage resize(BufferedImage image, int width, int height) {
        if (width == image.getWidth() && height == image.getHeight()) {
            return image;
        }
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
