package pdf_editor.view;

import view.DrawingCanvas;
import view.board.Icons;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

/**
 * PDF tool cursors use the exact glyphs used by the floating toolbar.
 * All icons are drawn in the toolbar's dark monochrome style, without a
 * blue tint, halo, shadow, or cursor-specific restyling.
 */
final class PdfToolCursors {
    private static final int SIZE = 32;
    private static final int SUPERSAMPLE = 4;
    private static final Color TOOLBAR_INK = new Color(34, 34, 34);

    private PdfToolCursors() { }

    /** The pencil tip points at (9, 23); precise erasing is centered on the ring. */
    static Point hotspotFor(DrawingCanvas.Tool tool) {
        return tool == DrawingCanvas.Tool.PEN ? new Point(9, 23) : new Point(16, 16);
    }

    static Cursor forTool(DrawingCanvas.Tool tool) {
        if (tool == null || GraphicsEnvironment.isHeadless()) return Cursor.getDefaultCursor();
        BufferedImage image = imageFor(tool);
        if (image == null) return Cursor.getDefaultCursor();
        try {
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Dimension supported = toolkit.getBestCursorSize(SIZE, SIZE);
            if (supported.width <= 0 || supported.height <= 0) {
                return Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
            }
            // Keep the logical 32px artwork; do not stretch it into a pixelated
            // image if the operating system supports a different cursor bitmap size.
            if (supported.width != SIZE || supported.height != SIZE) {
                BufferedImage compatible = new BufferedImage(supported.width, supported.height,
                        BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = compatible.createGraphics();
                try {
                    g.drawImage(image, 0, 0, null);
                } finally {
                    g.dispose();
                }
                image = compatible;
            }
            Point hotspot = hotspotFor(tool);
            hotspot.x = Math.min(hotspot.x, image.getWidth() - 1);
            hotspot.y = Math.min(hotspot.y, image.getHeight() - 1);
            return toolkit.createCustomCursor(image, hotspot, "PDF " + tool);
        } catch (RuntimeException ex) {
            return Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
        }
    }

    static BufferedImage imageFor(DrawingCanvas.Tool tool) {
        if (tool == null) return null;
        if (tool != DrawingCanvas.Tool.PEN && tool != DrawingCanvas.Tool.ERASER
                && tool != DrawingCanvas.Tool.LASSO && tool != DrawingCanvas.Tool.RECTANGLE) return null;

        BufferedImage hiRes = new BufferedImage(SIZE * SUPERSAMPLE, SIZE * SUPERSAMPLE,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = hiRes.createGraphics();
        try {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
            g.scale(SUPERSAMPLE, SUPERSAMPLE);
            g.setColor(TOOLBAR_INK);
            switch (tool) {
                case PEN -> {
                    g.translate(16, 16);
                    Icons.pencil(g);
                }
                case LASSO -> {
                    g.translate(16, 16);
                    Icons.lasso(g);
                }
                case RECTANGLE -> {
                    g.translate(16, 16);
                    Icons.rectangleSelect(g);
                }
                case ERASER -> {
                    // Same clear circular erase target as Canvas, with no decorative
                    // badge or colored center. Its midpoint is the actual erase point.
                    g.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g.drawOval(8, 8, 16, 16);
                }
                default -> { }
            }
        } finally {
            g.dispose();
        }
        BufferedImage result = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D output = result.createGraphics();
        try {
            output.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            output.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            output.drawImage(hiRes, 0, 0, SIZE, SIZE, null);
        } finally {
            output.dispose();
        }
        return result;
    }
}
