package view.answer;

import interface_adapter.TextRun;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

// answer text drawn like pen ink: kalam (or the typeset tex) centre line traced w/ the same round pen
public final class AnswerInk {
    static final float REF = 240f;  // font size the centre lines get traced at
    private static final int PAD = 2;
    private static final double TWIG = 0.15;  // spurs up to this x glyph size get pointed, x/q arms start ~.175
    private static final int PINHOLE = 50;  // aa gaps are 1-2px, smallest real counter (e) ~1200
    private static final int CACHE_SIZE = 64;
    private static final Map<Key, Path2D> LINES = new LinkedHashMap<>(16, 0.75f, true) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<Key, Path2D> eldest) {
            return size() > CACHE_SIZE;
        }
    };
    private static double digitRatio;  // centre line height of "0" per point
    private static double texDigitRatio;  // same for typeset

    private AnswerInk() {
    }

    // font size whose "0" centre line is charHeight tall, same as a pen stroke's point box
    public static synchronized float sizeFor(double charHeight) {
        if (digitRatio == 0) {
            digitRatio = centerline("0", null).getBounds2D().getHeight() / REF;
        }
        return (float) (charHeight / digitRatio);
    }

    // tex size whose "0" centre line is charHeight tall
    public static synchronized float typesetSizeFor(double charHeight) {
        if (texDigitRatio == 0) {
            texDigitRatio = centerline("", null, "0").getBounds2D().getHeight() / REF;
        }
        return (float) (charHeight / texDigitRatio);
    }

    // ink box at (0,0): centre line box + half a pen all round
    public static Rectangle2D bounds(String text, List<TextRun> runs, float size, double penWidth) {
        return box(scaled(text, runs, null, size), penWidth);
    }

    public static Rectangle2D typesetBounds(String latex, float size, double penWidth) {
        return box(scaled("", null, latex, size), penWidth);
    }

    // ink box top down to the fraction bar line, that's what lines up w/ the formula's middle
    public static double typesetMiddle(String latex, float size, double penWidth) {
        double lineTop = centerline("", null, latex).getBounds2D().getY() * size / REF;
        return TexAnswer.axis(latex, size) - lineTop + penWidth / 2;
    }

    private static Rectangle2D box(Path2D line, double penWidth) {
        Rectangle2D b = line.getBounds2D();
        return new Rectangle2D.Double(0, 0, b.getWidth() + penWidth, b.getHeight() + penWidth);
    }

    // centre line where the answer sits, world coords
    public static Path2D placed(CanvasAnswer a, double penWidth) {
        Path2D line = scaled(a.getText(), a.getRuns(), a.getLatex(), a.getFontSize());
        Rectangle2D b = line.getBounds2D();
        line.transform(AffineTransform.getTranslateInstance(a.getX() + penWidth / 2 - b.getX(),
                a.getY() + penWidth / 2 - b.getY()));
        return line;
    }

    // filled ink outline, world coords
    public static Shape outline(CanvasAnswer a, double penWidth) {
        return pen(penWidth).createStrokedShape(placed(a, penWidth));
    }

    public static void paint(Graphics2D world, CanvasAnswer a, Color color, double penWidth) {
        Graphics2D g = (Graphics2D) world.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g.setStroke(pen(penWidth));
        g.setColor(color);
        g.draw(placed(a, penWidth));
        g.dispose();
    }

    // eraser dragged from (x1,y1) to (x2,y2) w/ this radius hits actual ink, not just the box
    public static boolean touches(CanvasAnswer a, double penWidth, double x1, double y1, double x2, double y2,
                                  double radius) {
        Rectangle2D box = a.getBox();
        box.setRect(box.getX() - radius, box.getY() - radius, box.getWidth() + 2 * radius,
                box.getHeight() + 2 * radius);
        if (!box.intersectsLine(x1, y1, x2, y2)) {
            return false;
        }
        double reach = radius + penWidth / 2;
        double[] c = new double[6];
        for (PathIterator it = placed(a, penWidth).getPathIterator(null); !it.isDone(); it.next()) {
            it.currentSegment(c);
            if (Line2D.ptSegDist(x1, y1, x2, y2, c[0], c[1]) <= reach) {
                return true;
            }
        }
        return false;
    }

    // same answer after a resize, box refit since the pen width doesn't scale
    public static CanvasAnswer refit(CanvasAnswer a, double penWidth) {
        Rectangle2D b = box(scaled(a.getText(), a.getRuns(), a.getLatex(), a.getFontSize()), penWidth);
        return a.sized(b.getWidth(), b.getHeight());
    }

    private static BasicStroke pen(double width) {
        return new BasicStroke((float) width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    }

    // centre line at this size, fresh copy
    private static Path2D scaled(String text, List<TextRun> runs, String latex, float size) {
        Path2D line = new Path2D.Double(centerline(text, runs, latex));
        line.transform(AffineTransform.getScaleInstance(size / REF, size / REF));
        return line;
    }

    static Path2D centerline(String text, List<TextRun> runs) {
        return centerline(text, runs, null);
    }

    // traced once per answer at REF size; text has a baseline origin, tex its box top-left
    static synchronized Path2D centerline(String text, List<TextRun> runs, String latex) {
        Key key = new Key(text, runs, latex);
        Path2D line = LINES.get(key);
        if (line == null) {
            if (latex != null) {
                line = traceTex(latex);
            } else {
                line = runs == null ? trace(AnswerFont.outline(text, REF), REF) : traceEachSize(runs);
            }
            LINES.put(key, line);
        }
        return line;
    }

    // tex painted filled at REF then thinned like kalam is; fraction bars + roots come out as single lines
    private static Path2D traceTex(String latex) {
        Rectangle2D b = TexAnswer.bounds(latex, REF);
        int w = (int) Math.ceil(b.getWidth()) + 2 * PAD;
        int h = (int) Math.ceil(b.getHeight()) + 2 * PAD;
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g = img.createGraphics();
        g.translate(PAD, PAD);
        TexAnswer.paint(g, latex, REF, Color.WHITE);
        g.dispose();
        return thinned(lit(img), w, h, -PAD, -PAD, REF, true);
    }

    // exponents traced apart, their twigs are 0.6x
    private static Path2D traceEachSize(List<TextRun> runs) {
        Path2D line = new Path2D.Double();
        for (Map.Entry<Float, Shape> group : RunLayout.outlinesBySize(runs, REF).entrySet()) {
            line.append(trace(group.getValue(), group.getKey()), false);
        }
        return line;
    }

    // raster the glyphs, thin to 1px, join neighbouring pixels w/ tiny segments
    private static Path2D trace(Shape outline, float glyphSize) {
        Rectangle2D b = outline.getBounds2D();
        int w = (int) Math.ceil(b.getWidth()) + 2 * PAD;
        int h = (int) Math.ceil(b.getHeight()) + 2 * PAD;
        if (b.isEmpty()) {
            return new Path2D.Double();
        }
        return thinned(raster(outline, b, w, h), w, h, b.getX() - PAD, b.getY() - PAD, glyphSize, false);
    }

    // filled px -> 1px centre line; px (0,0) sits at (left, top); tex has dead straight 45deg strokes
    private static Path2D thinned(boolean[] lit, int w, int h, double left, double top, float glyphSize,
                                  boolean straightDiagonals) {
        Path2D line = new Path2D.Double();
        boolean[] filled = Pinholes.fill(lit, w, h, PINHOLE);
        boolean[] thin = straightDiagonals ? Skeleton.thinKeepingDiagonals(filled, w, h) : Skeleton.thin(filled, w, h);
        boolean[] on = Skeleton.sharpen(thin, w, h, (int) Math.round(TWIG * glyphSize));
        double ox = left + 0.5;
        double oy = top + 0.5;
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                if (!on[y * w + x]) {
                    continue;
                }
                boolean east = x + 1 < w && on[y * w + x + 1];
                boolean south = y + 1 < h && on[(y + 1) * w + x];
                boolean joined = false;
                if (east) {
                    joined |= link(line, ox, oy, x, y, x + 1, y);
                }
                if (south) {
                    joined |= link(line, ox, oy, x, y, x, y + 1);
                }
                // diagonals only when no square path covers them
                if (y + 1 < h && x + 1 < w && on[(y + 1) * w + x + 1] && !east && !on[(y + 1) * w + x]) {
                    joined |= link(line, ox, oy, x, y, x + 1, y + 1);
                }
                if (y + 1 < h && x > 0 && on[(y + 1) * w + x - 1] && !south && !on[y * w + x - 1]) {
                    joined |= link(line, ox, oy, x, y, x - 1, y + 1);
                }
                if (!joined && !hasNeighbour(on, w, h, x, y)) {
                    link(line, ox, oy, x, y, x, y);  // lone dot, round cap draws it
                }
            }
        }
        return line;
    }

    private static boolean link(Path2D line, double ox, double oy, int x1, int y1, int x2, int y2) {
        line.moveTo(ox + x1, oy + y1);
        line.lineTo(ox + x2, oy + y2);
        return true;
    }

    private static boolean hasNeighbour(boolean[] on, int w, int h, int x, int y) {
        for (int dy = -1; dy <= 1; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                int nx = x + dx;
                int ny = y + dy;
                if ((dx != 0 || dy != 0) && nx >= 0 && ny >= 0 && nx < w && ny < h && on[ny * w + nx]) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean[] raster(Shape outline, Rectangle2D b, int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.translate(PAD - b.getX(), PAD - b.getY());
        g.setColor(Color.WHITE);
        g.fill(outline);
        g.dispose();
        return lit(img);
    }

    private static boolean[] lit(BufferedImage img) {
        int w = img.getWidth();
        boolean[] on = new boolean[w * img.getHeight()];
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < w; x++) {
                on[y * w + x] = img.getRaster().getSample(x, y, 0) > 127;
            }
        }
        return on;
    }

    private static final class Key {
        private final String text;
        private final List<TextRun> runs;
        private final String latex;

        Key(String text, List<TextRun> runs, String latex) {
            this.text = text;
            this.runs = runs;
            this.latex = latex;
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof Key && text.equals(((Key) o).text) && Objects.equals(runs, ((Key) o).runs)
                    && Objects.equals(latex, ((Key) o).latex);
        }

        @Override
        public int hashCode() {
            return Objects.hash(text, runs, latex);
        }
    }
}
