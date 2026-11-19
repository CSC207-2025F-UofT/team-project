package rendering;

import java.awt.image.BufferedImage;
import java.util.Objects;

/**
 * Presentation-only normalization for imported opaque white-backed charts.
 * Transparent ink and genuinely transparent PNGs are left unchanged; the
 * original image bytes are never modified. Formula artwork is rendered on
 * transparent pixels directly by MaterialRenderer, without this conversion.
 */
public final class TransparentArtwork {
    private static final int NEAR_WHITE = 242;

    private TransparentArtwork() { }

    public static BufferedImage removeWhiteMatte(BufferedImage source) {
        Objects.requireNonNull(source, "source");
        final int w = source.getWidth(), h = source.getHeight();
        // Do not double-unmatte transparent ink or antialiased ARGB artwork.
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                if ((source.getRGB(x, y) >>> 24) != 255) return source;
            }
        }
        // Photos and painted backgrounds are not assumed to be white-paper
        // charts. Require a mostly white outer frame before color-keying.
        int white = 0;
        int samples = 0;
        for (int x = 0; x < w; x++) {
            if (nearWhite(source.getRGB(x, 0))) white++;
            if (nearWhite(source.getRGB(x, h - 1))) white++;
            samples += 2;
        }
        for (int y = 0; y < h; y++) {
            if (nearWhite(source.getRGB(0, y))) white++;
            if (nearWhite(source.getRGB(w - 1, y))) white++;
            samples += 2;
        }
        if (white * 5 < samples * 4) return source;

        BufferedImage transparent = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int rgb = source.getRGB(x, y);
                int r = (rgb >>> 16) & 255;
                int g = (rgb >>> 8) & 255;
                int b = rgb & 255;
                // Undo compositing against white. This also removes the white
                // fringes of antialiased curves when shown over PDF text.
                int opacity = 255 - Math.min(r, Math.min(g, b));
                if (opacity == 0) continue;
                int rr = Math.max(0, Math.min(255, (r - 255 + opacity) * 255 / opacity));
                int gg = Math.max(0, Math.min(255, (g - 255 + opacity) * 255 / opacity));
                int bb = Math.max(0, Math.min(255, (b - 255 + opacity) * 255 / opacity));
                transparent.setRGB(x, y, (opacity << 24) | (rr << 16) | (gg << 8) | bb);
            }
        }
        return transparent;
    }

    private static boolean nearWhite(int rgb) {
        return ((rgb >>> 16) & 255) >= NEAR_WHITE
                && ((rgb >>> 8) & 255) >= NEAR_WHITE && (rgb & 255) >= NEAR_WHITE;
    }
}
