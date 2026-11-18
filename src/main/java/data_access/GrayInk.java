package data_access;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Arrays;

// a picture as gray dark-ink-on-white + where the ink is, nothing gets thresholded in the output
final class GrayInk {
    // how far from the background a pixel has to be to count as ink, only used to find the ink
    static final int INK_THRESHOLD = 32;

    private final int width;
    private final int height;
    private final int[] gray;  // 255 = background, 0 = darkest ink

    private GrayInk(int width, int height, int[] gray) {
        this.width = width;
        this.height = height;
        this.gray = gray;
    }

    // alpha onto white, dark bg flipped to white, coloured ink kept dark via its darkest channel
    static GrayInk of(BufferedImage source) {
        int width = source.getWidth();
        int height = source.getHeight();
        int[] rgb = onWhite(source);
        boolean lightBackground = median(border(rgb, width, height)) >= 128;
        int[] gray = new int[rgb.length];
        for (int i = 0; i < rgb.length; i++) {
            int r = (rgb[i] >> 16) & 0xFF;
            int g = (rgb[i] >> 8) & 0xFF;
            int b = rgb[i] & 0xFF;
            // plain luminance would turn yellow ink near white, min channel keeps it as dark as it stands out
            gray[i] = lightBackground ? Math.min(r, Math.min(g, b)) : 255 - Math.max(r, Math.max(g, b));
        }
        return new GrayInk(width, height, gray);
    }

    boolean isInk(int x, int y) {
        return 255 - gray[y * width + x] > INK_THRESHOLD;
    }

    // tight box round the ink, null if there's none
    Rectangle bounds() {
        int left = width;
        int top = height;
        int right = -1;
        int bottom = -1;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (isInk(x, y)) {
                    left = Math.min(left, x);
                    top = Math.min(top, y);
                    right = Math.max(right, x);
                    bottom = Math.max(bottom, y);
                }
            }
        }
        return right < 0 ? null : new Rectangle(left, top, right - left + 1, bottom - top + 1);
    }

    // per row of the box: any ink in it
    boolean[] rowsWithInk(Rectangle box) {
        boolean[] rows = new boolean[box.height];
        for (int y = 0; y < box.height; y++) {
            for (int x = box.x; x < box.x + box.width && !rows[y]; x++) {
                rows[y] = isInk(x, box.y + y);
            }
        }
        return rows;
    }

    // rough pen thickness: median ink run across rows vs down columns, the thinner one wins
    double penWidth(Rectangle box) {
        return Math.min(medianRun(box, true), medianRun(box, false));
    }

    // the box cut out as a gray rgb image (r=g=b so getRGB doesnt gamma shift anything)
    BufferedImage crop(Rectangle box) {
        BufferedImage out = new BufferedImage(box.width, box.height, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < box.height; y++) {
            for (int x = 0; x < box.width; x++) {
                int v = gray[(box.y + y) * width + box.x + x];
                out.setRGB(x, y, (v << 16) | (v << 8) | v);
            }
        }
        return out;
    }

    private double medianRun(Rectangle box, boolean alongRows) {
        int outer = alongRows ? box.height : box.width;
        int inner = alongRows ? box.width : box.height;
        int[] runs = new int[outer * (inner / 2 + 1)];
        int count = 0;
        for (int i = 0; i < outer; i++) {
            int run = 0;
            for (int j = 0; j <= inner; j++) {
                boolean ink = j < inner && (alongRows ? isInk(box.x + j, box.y + i) : isInk(box.x + i, box.y + j));
                if (ink) {
                    run++;
                } else if (run > 0) {
                    runs[count++] = run;
                    run = 0;
                }
            }
        }
        return count == 0 ? 0 : median(Arrays.copyOf(runs, count));
    }

    private static int[] onWhite(BufferedImage source) {
        int width = source.getWidth();
        int height = source.getHeight();
        int[] rgb = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int argb = source.getRGB(x, y);
                double alpha = ((argb >>> 24) & 0xFF) / 255.0;
                int r = (int) Math.round(((argb >> 16) & 0xFF) * alpha + 255 * (1 - alpha));
                int g = (int) Math.round(((argb >> 8) & 0xFF) * alpha + 255 * (1 - alpha));
                int b = (int) Math.round((argb & 0xFF) * alpha + 255 * (1 - alpha));
                rgb[y * width + x] = (r << 16) | (g << 8) | b;
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
}
