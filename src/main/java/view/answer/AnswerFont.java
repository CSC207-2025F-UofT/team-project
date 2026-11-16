package view.answer;

import interface_adapter.TextRun;
import view.stack.Fonts;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;
import java.io.InputStream;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

// Kalam handwriting for answers on the canvas
public final class AnswerFont {
    private static final FontRenderContext FRC = new FontRenderContext(null, true, true);
    private static final float RAISED_SCALE = 0.6f;  // exponent size vs body
    private static final double RAISE = 0.45;        // exponent baseline lift, x the digit height
    private static final double SIGN_GAP = 0.03;     // radicand starts this far past the √ ink, x size
    private static final double BAR = 0.075;         // √ bar thickness, about one kalam stroke
    private static final double CLEAR = 0.08;        // room between radicand top and the bar
    private static final double BAR_PAST = 0.05;     // bar runs this far past the radicand
    private static Font base;
    private static double digitRatio;  // ink height of "0" per point of font size

    private AnswerFont() {
    }

    // Kalam at size 1, or ui font if it won't load
    public static synchronized Font base() {
        if (base == null) {
            try (InputStream in = AnswerFont.class.getResourceAsStream("/fonts/Kalam-Regular.ttf")) {
                base = in == null ? Fonts.ui(Font.PLAIN, 1f) : Font.createFont(Font.TRUETYPE_FONT, in).deriveFont(1f);
            } catch (IOException | FontFormatException e) {
                base = Fonts.ui(Font.PLAIN, 1f);
            }
        }
        return base;
    }

    // font size whose "0" is charHeight tall
    public static synchronized float sizeFor(double charHeight) {
        if (digitRatio == 0) {
            digitRatio = visualBounds("0", 100f).getHeight() / 100;
        }
        return (float) (charHeight / digitRatio);
    }

    // ink box, origin at baseline start
    public static Rectangle2D visualBounds(String text, float size) {
        if (text.isEmpty()) {
            return new Rectangle2D.Double();
        }
        return layout(text, size).getBounds();
    }

    // ink top-left lands on (left, top); caller sets color
    public static void draw(Graphics2D g, String text, float size, double left, double top) {
        if (text.isEmpty()) {
            return;
        }
        TextLayout layout = layout(text, size);
        Rectangle2D ink = layout.getBounds();
        layout.draw(g, (float) (left - ink.getX()), (float) (top - ink.getY()));
    }

    // ink box of runs, raised ones small and lifted; origin at baseline start
    public static Rectangle2D visualBounds(List<TextRun> runs, float size) {
        return inkOf(typeset(runs, size));
    }

    // same as draw(String) but w/ exponents raised
    public static void draw(Graphics2D g, List<TextRun> runs, float size, double left, double top) {
        List<Piece> pieces = typeset(runs, size);
        Rectangle2D ink = inkOf(pieces);
        for (Piece piece : pieces) {
            piece.draw(g, left - ink.getX(), top - ink.getY());
        }
    }

    // glyph outline, origin at baseline start
    public static Shape outline(String text, float size) {
        return text.isEmpty() ? new Path2D.Double() : layout(text, size).getOutline(null);
    }

    // outline w/ exponents raised, same spots as draw(runs)
    public static Shape outline(List<TextRun> runs, float size) {
        Path2D all = new Path2D.Double();
        for (Piece piece : typeset(runs, size)) {
            piece.appendTo(all);
        }
        return all;
    }

    // runs -> placed bits; each √ and its radicand get cut out so the bar can go over
    private static List<Piece> typeset(List<TextRun> runs, float size) {
        List<Radicands.Root> roots = Radicands.find(TextRun.plain(runs));
        Set<Integer> cuts = new HashSet<>();
        for (Radicands.Root r : roots) {
            cuts.addAll(List.of(r.sign, r.sign + 1, r.end));
        }
        List<Piece> pieces = new ArrayList<>();
        Map<Integer, Piece> signs = new HashMap<>();
        double pen = 0;
        int at = 0;
        for (TextRun run : runs) {
            String text = run.getText();
            float s = sizeOf(run, size);
            for (int from = 0, to; from < text.length(); from = to) {
                to = from + 1;
                while (to < text.length() && !cuts.contains(at + to)) {
                    to++;
                }
                Piece piece = new Piece(layout(text.substring(from, to), s), at + from, at + to, s, pen,
                        -lift(run, size));
                pieces.add(piece);
                pen += piece.layout.getAdvance();
                for (Radicands.Root r : roots) {
                    if (r.sign == piece.start) {
                        signs.put(r.sign, piece);
                        pen = Math.max(pen, piece.ink().getMaxX() + SIGN_GAP * s);  // kalam's √ arm sticks out
                    }
                    if (r.end == piece.end) {
                        pen += BAR_PAST * signs.get(r.sign).size;
                    }
                }
            }
            at += text.length();
        }
        List<Radicands.Root> innerFirst = new ArrayList<>(roots);
        innerFirst.sort(Comparator.comparingInt(r -> r.end - r.sign));
        for (Radicands.Root r : innerFirst) {
            barOver(signs.get(r.sign), pieces, r);
        }
        return pieces;
    }

    // stretch the √ up past the radicand, then run the bar off its tip
    private static void barOver(Piece sign, List<Piece> pieces, Radicands.Root r) {
        Rectangle2D under = null;
        for (Piece p : pieces) {
            Rectangle2D ink = p.ink();
            if (p.start > r.sign && p.end <= r.end && ink.getWidth() > 0) {
                under = under == null ? ink : under.createUnion(ink);
            }
        }
        if (under == null) {
            return;
        }
        Rectangle2D glyph = sign.ink();
        double thick = BAR * sign.size;
        double top = Math.min(glyph.getY(), under.getY() - CLEAR * sign.size - thick);
        if (top < glyph.getY()) {
            double bottom = glyph.getMaxY();
            sign.stretch = new AffineTransform();
            sign.stretch.translate(0, bottom);
            sign.stretch.scale(1, (bottom - top) / (bottom - glyph.getY()));
            sign.stretch.translate(0, -bottom);
        }
        double left = glyph.getMaxX() - thick;
        double right = under.getMaxX() + BAR_PAST * sign.size;
        sign.bar = new RoundRectangle2D.Double(left, top, right - left, thick, thick, thick);
    }

    private static Rectangle2D inkOf(List<Piece> pieces) {
        Rectangle2D box = null;
        for (Piece piece : pieces) {
            Rectangle2D ink = piece.ink();
            if (ink.getWidth() > 0) {
                box = box == null ? ink : box.createUnion(ink);
            }
        }
        return box == null ? new Rectangle2D.Double() : box;
    }

    private static float sizeOf(TextRun run, float size) {
        return run.isRaised() ? size * RAISED_SCALE : size;
    }

    private static double lift(TextRun run, float size) {
        return run.isRaised() ? digitHeight(size) * RAISE : 0;
    }

    private static synchronized double digitHeight(float size) {
        if (digitRatio == 0) {
            digitRatio = visualBounds("0", 100f).getHeight() / 100;
        }
        return digitRatio * size;
    }

    private static TextLayout layout(String text, float size) {
        Font kalam = base().deriveFont(size);
        Font fallback = Fonts.ui(Font.PLAIN, size);
        AttributedString styled = new AttributedString(text);
        styled.addAttribute(TextAttribute.FONT, kalam);
        for (int i = 0; i < text.length(); i++) {
            if (!kalam.canDisplay(text.charAt(i))) {
                styled.addAttribute(TextAttribute.FONT, fallback, i, i + 1);  // e.g. theta
            }
        }
        return new TextLayout(styled.getIterator(), FRC);
    }

    // bit of answer text at (x, y) off the baseline start; a √ may get stretched + a bar
    private static final class Piece {
        private final TextLayout layout;
        private final int start;  // chars of the plain text
        private final int end;
        private final float size;
        private final double x;
        private final double y;
        private AffineTransform stretch;
        private Shape bar;

        Piece(TextLayout layout, int start, int end, float size, double x, double y) {
            this.layout = layout;
            this.start = start;
            this.end = end;
            this.size = size;
            this.x = x;
            this.y = y;
        }

        Rectangle2D ink() {
            Rectangle2D ink = layout.getBounds();
            ink.setRect(ink.getX() + x, ink.getY() + y, ink.getWidth(), ink.getHeight());
            if (stretch != null) {
                ink = stretch.createTransformedShape(ink).getBounds2D();
            }
            return bar == null ? ink : ink.createUnion(bar.getBounds2D());
        }

        void appendTo(Path2D all) {
            if (bar == null) {
                all.append(glyphs(), false);
                return;
            }
            Area joined = new Area(glyphs());  // bar winds the other way, a plain append punches a hole at the tip
            joined.add(new Area(bar));
            all.append(joined, false);
        }

        void draw(Graphics2D g, double dx, double dy) {
            if (stretch == null) {
                layout.draw(g, (float) (dx + x), (float) (dy + y));
            } else {
                g.fill(AffineTransform.getTranslateInstance(dx, dy).createTransformedShape(glyphs()));
            }
            if (bar != null) {
                g.fill(AffineTransform.getTranslateInstance(dx, dy).createTransformedShape(bar));
            }
        }

        private Shape glyphs() {
            AffineTransform at = AffineTransform.getTranslateInstance(x, y);
            if (stretch != null) {
                at.preConcatenate(stretch);
            }
            return layout.getOutline(at);
        }
    }
}
