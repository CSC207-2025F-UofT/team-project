package view.answer;

import interface_adapter.TextRun;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

// answer runs laid out in kalam: exponents small + up, √ gets a bar over its radicand
public final class RunLayout {
    private static final float RAISED_SCALE = 0.6f;  // exponent size vs body
    private static final double RAISE = 0.45;        // exponent baseline lift, x the digit height
    private static final double SIGN_GAP = 0.03;     // radicand starts this far past the √ ink, x size
    private static final double BAR = 0.075;         // √ bar thickness, about one kalam stroke
    private static final double CLEAR = 0.08;        // room between radicand top and the bar
    private static final double BAR_PAST = 0.05;     // bar runs this far past the radicand

    private RunLayout() {
    }

    // ink box of runs, raised ones small and lifted; origin at baseline start
    public static Rectangle2D visualBounds(List<TextRun> runs, float size) {
        return inkOf(typeset(runs, size));
    }

    // ink top-left lands on (left, top), same as AnswerFont.draw(String)
    public static void draw(Graphics2D g, List<TextRun> runs, float size, double left, double top) {
        List<Piece> pieces = typeset(runs, size);
        Rectangle2D ink = inkOf(pieces);
        for (Piece piece : pieces) {
            piece.draw(g, left - ink.getX(), top - ink.getY());
        }
    }

    // outline w/ exponents raised, same spots as draw
    public static Shape outline(List<TextRun> runs, float size) {
        Path2D all = new Path2D.Double();
        for (Piece piece : typeset(runs, size)) {
            piece.appendTo(all);
        }
        return all;
    }

    // same outline split by glyph size, exponents get their own so they can be traced at their own scale
    public static Map<Float, Shape> outlinesBySize(List<TextRun> runs, float size) {
        Map<Float, Path2D> bySize = new LinkedHashMap<>();
        for (Piece piece : typeset(runs, size)) {
            piece.appendTo(bySize.computeIfAbsent(piece.size, s -> new Path2D.Double()));
        }
        return new LinkedHashMap<>(bySize);
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
                Piece piece = new Piece(AnswerFont.layout(text.substring(from, to), s), at + from, at + to, s, pen,
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
            sign.stretchUpTo(top);
        }
        double left = glyph.getMaxX() - thick;
        double right = under.getMaxX() + BAR_PAST * sign.size;
        sign.setBar(new RoundRectangle2D.Double(left, top, right - left, thick, thick, thick));
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
        return run.isRaised() ? AnswerFont.digitHeight(size) * RAISE : 0;
    }
}
