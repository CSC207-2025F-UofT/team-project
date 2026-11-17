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

// answer runs laid out in kalam: exponents small + up (twice for a ^ inside one), √ gets a bar
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

    // runs -> placed bits; each √, its radicand and any second-level exponent get cut out
    private static List<Piece> typeset(List<TextRun> runs, float size) {
        String plain = TextRun.plain(runs);
        List<Operands.Span> roots = Operands.after(plain, '√');
        List<Operands.Span> upAgain = secondLevel(runs, plain);
        Set<Integer> cuts = cutsAround(roots, upAgain);
        List<Piece> pieces = new ArrayList<>();
        Map<Integer, Piece> signs = new HashMap<>();
        double pen = 0;
        int at = 0;
        for (TextRun run : runs) {
            String text = run.getText();
            for (int from = 0, to; from < text.length(); from = to) {
                to = from + 1;
                while (to < text.length() && !cuts.contains(at + to)) {
                    to++;
                }
                if (startsAny(upAgain, at + from)) {
                    continue;  // the ^ itself isn't drawn
                }
                float s = sizeOf(run, size);
                double y = -lift(run, size);
                if (holdsAny(upAgain, at + from, at + to)) {
                    y -= AnswerFont.digitHeight(s) * RAISE;
                    s *= RAISED_SCALE;
                }
                Piece piece = new Piece(AnswerFont.layout(text.substring(from, to), s), at + from, at + to, s, pen, y);
                pieces.add(piece);
                pen = roomForRoots(roots, piece, signs, pen + piece.layout.getAdvance());
            }
            at += text.length();
        }
        List<Operands.Span> innerFirst = new ArrayList<>(roots);
        innerFirst.sort(Comparator.comparingInt(r -> r.end - r.mark));
        for (Operands.Span r : innerFirst) {
            barOver(signs.get(r.mark), pieces, r);
        }
        return pieces;
    }

    // each mark alone, and a cut where its operand ends
    private static Set<Integer> cutsAround(List<Operands.Span> roots, List<Operands.Span> upAgain) {
        Set<Integer> cuts = new HashSet<>();
        for (List<Operands.Span> spans : List.of(roots, upAgain)) {
            for (Operands.Span span : spans) {
                cuts.addAll(List.of(span.mark, span.mark + 1, span.end));
            }
        }
        return cuts;
    }

    // pen after this piece: clear of a √'s arm, or past where its bar will end
    private static double roomForRoots(List<Operands.Span> roots, Piece piece, Map<Integer, Piece> signs, double pen) {
        for (Operands.Span r : roots) {
            if (r.mark == piece.start) {
                signs.put(r.mark, piece);
                pen = Math.max(pen, piece.ink().getMaxX() + SIGN_GAP * piece.size);  // kalam's √ arm sticks out
            }
            if (r.end == piece.end) {
                pen += BAR_PAST * signs.get(r.mark).size;
            }
        }
        return pen;
    }

    // ^ the writer leaves inside an exponent bc runs only go up once; ones nested deeper stay as text
    private static List<Operands.Span> secondLevel(List<TextRun> runs, String plain) {
        List<Operands.Span> up = new ArrayList<>();
        for (Operands.Span caret : Operands.after(plain, '^')) {
            if (raisedAt(runs, caret.mark) && !holdsAny(up, caret.mark, caret.end)) {
                up.add(caret);
            }
        }
        return up;
    }

    private static boolean raisedAt(List<TextRun> runs, int index) {
        int at = 0;
        for (TextRun run : runs) {
            at += run.getText().length();
            if (index < at) {
                return run.isRaised();
            }
        }
        return false;
    }

    private static boolean startsAny(List<Operands.Span> spans, int index) {
        for (Operands.Span span : spans) {
            if (span.mark == index) {
                return true;
            }
        }
        return false;
    }

    private static boolean holdsAny(List<Operands.Span> spans, int from, int to) {
        for (Operands.Span span : spans) {
            if (span.holds(from, to)) {
                return true;
            }
        }
        return false;
    }

    // stretch the √ up past the radicand, then run the bar off its tip
    private static void barOver(Piece sign, List<Piece> pieces, Operands.Span r) {
        Rectangle2D under = null;
        for (Piece p : pieces) {
            Rectangle2D ink = p.ink();
            if (r.holds(p.start, p.end) && ink.getWidth() > 0) {
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
