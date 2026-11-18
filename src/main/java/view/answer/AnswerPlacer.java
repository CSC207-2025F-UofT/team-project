package view.answer;

import entity.Stroke;
import interface_adapter.AnswerKind;
import interface_adapter.TextRun;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.function.Predicate;

// where an answer lands + how big, from the formula's ink
public final class AnswerPlacer {
    private AnswerPlacer() {
    }

    // runs null = plain text; below falls back to the right if it would hit other ink
    public static CanvasAnswer place(List<Stroke> ink, AnswerKind kind, String text, List<TextRun> runs, boolean error,
                                     double penWidth, Predicate<Rectangle2D> hitsOtherInk) {
        double h = AnswerLayout.charHeight(ink);
        float size = AnswerInk.sizeFor(h);
        Rectangle2D text2d = AnswerInk.bounds(text, runs, size, penWidth);
        double w = text2d.getWidth();
        double ht = text2d.getHeight();
        Point2D at = spot(ink, kind, h, w, ht, ht / 2, hitsOtherInk);
        return runs == null ? new CanvasAnswer(text, error, at.getX(), at.getY(), w, ht, size)
                : new CanvasAnswer(runs, error, at.getX(), at.getY(), w, ht, size);
    }

    // typeset answer, same spot rules; "0" as tall as the pen-ink one so old + new match
    public static CanvasAnswer placeTypeset(List<Stroke> ink, AnswerKind kind, String text, String latex,
                                            boolean error, double penWidth, Predicate<Rectangle2D> hitsOtherInk) {
        double h = AnswerLayout.charHeight(ink);
        float size = TexAnswer.sizeFor(h + penWidth);
        Rectangle2D box = TexAnswer.bounds(latex, size);
        Point2D at = spot(ink, kind, h, box.getWidth(), box.getHeight(), TexAnswer.middle(latex, size), hitsOtherInk);
        return CanvasAnswer.typeset(text, latex, error, at.getX(), at.getY(), box.getWidth(), box.getHeight(), size);
    }

    // below if theres room, else right w/ its middle on the last glyph's
    private static Point2D spot(List<Stroke> ink, AnswerKind kind, double h, double w, double ht, double middle,
                                Predicate<Rectangle2D> hitsOtherInk) {
        Point2D at = kind.isBelow() ? AnswerLayout.below(ink, h) : AnswerLayout.rightOfAt(ink, h, middle);
        if (kind.isBelow() && hitsOtherInk.test(new Rectangle2D.Double(at.getX(), at.getY(), w, ht))) {
            at = AnswerLayout.rightOfAt(ink, h, middle);
        }
        return at;
    }
}
