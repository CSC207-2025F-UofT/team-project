package view;

import entity.InkPoint;
import entity.Stroke;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.List;

// strokes -> the picture recognition gets, drawn fresh from the points every time, never a screen grab
final class FormulaImageRenderer {
    // out px per world px, fixed so zoom never changes the export, big enough that recognition only shrinks
    static final int SCALE = 4;
    // white border in out px, at least this on every side
    static final int MARGIN = 8;
    // model wants w/h in 16s, we pad up to it, never stretch
    static final int STRIDE = 16;

    private final float penWidth;

    FormulaImageRenderer(float penWidth) {
        this.penWidth = penWidth;
    }

    // black ink on white, gray only, no grid / cursor / selection / answers since only strokes get drawn
    BufferedImage render(List<Stroke> strokes) {
        Rectangle2D ink = inkBox(strokes);
        if (ink == null) {
            throw new IllegalStateException("The canvas is empty.");
        }
        int inkWidth = (int) Math.ceil(ink.getWidth() * SCALE);
        int inkHeight = (int) Math.ceil(ink.getHeight() * SCALE);
        int width = padUp(inkWidth + 2 * MARGIN);
        int height = padUp(inkHeight + 2 * MARGIN);
        // int rgb w/ r=g=b, BYTE_GRAY's getRGB gamma-shifts the antialiased edges
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        try {
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, width, height);
            // extra padding split both sides so the ink stays centred
            g.translate((width - inkWidth) / 2, (height - inkHeight) / 2);
            g.scale(SCALE, SCALE);
            g.translate(-ink.getX(), -ink.getY());
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
            g.setStroke(new BasicStroke(penWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g.setColor(Color.BLACK);
            for (Stroke stroke : strokes) {
                trace(g, stroke);
            }
        } finally {
            g.dispose();
        }
        return image;
    }

    // point box grown by half the pen so round caps + sub/superscript edges dont get clipped
    Rectangle2D inkBox(List<Stroke> strokes) {
        double left = Double.POSITIVE_INFINITY;
        double top = Double.POSITIVE_INFINITY;
        double right = Double.NEGATIVE_INFINITY;
        double bottom = Double.NEGATIVE_INFINITY;
        for (Stroke stroke : strokes) {
            for (InkPoint p : stroke.getPoints()) {
                left = Math.min(left, p.getX());
                top = Math.min(top, p.getY());
                right = Math.max(right, p.getX());
                bottom = Math.max(bottom, p.getY());
            }
        }
        if (left > right) {
            return null;
        }
        double half = penWidth / 2.0;
        return new Rectangle2D.Double(left - half, top - half, right - left + penWidth, bottom - top + penWidth);
    }

    // a tap is one point, the round cap turns it into a dot
    static void trace(Graphics2D g, Stroke stroke) {
        List<InkPoint> points = stroke.getPoints();
        if (points.isEmpty()) {
            return;
        }
        InkPoint previous = points.get(0);
        for (InkPoint point : points) {
            g.drawLine(previous.getX(), previous.getY(), point.getX(), point.getY());
            previous = point;
        }
    }

    static int padUp(int size) {
        return (size + STRIDE - 1) / STRIDE * STRIDE;
    }
}
