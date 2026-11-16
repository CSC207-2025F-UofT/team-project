package view.answer;

import entity.Stroke;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

// limits stacked over/under ink, Σ's n=0 and 10, lim's x→0
public final class Scripts {
    static final double REACH = 1.0;     // gap to the ink above/below, up to 1 body char
    static final double SMALLER = 0.8;   // script chars at most this x body chars

    private Scripts() {
    }

    // script sits right over or under some body ink and is written smaller
    public static boolean stackedOn(Collection<Stroke> body, Collection<Stroke> script) {
        Rectangle s = AnswerLayout.bounds(script);
        if (s == null || body.isEmpty() || !AnswerLayout.hasChars(script)) {  // lone "-" = next line, not a limit
            return false;
        }
        double h = AnswerLayout.charHeight(body);
        if (AnswerLayout.charHeight(script) > SMALLER * h) {
            return false;
        }
        Rectangle column = AnswerLayout.bounds(inColumn(body, s));
        if (column == null || s.getCenterX() < column.getMinX() || s.getCenterX() > column.getMaxX()) {
            return false;
        }
        double gap = Math.max(s.getMinY() - column.getMaxY(), column.getMinY() - s.getMaxY());
        return gap >= 0 && gap <= REACH * h;
    }

    // body strokes sharing some x with the box
    private static List<Stroke> inColumn(Collection<Stroke> body, Rectangle box) {
        List<Stroke> hit = new ArrayList<>();
        for (Stroke stroke : body) {
            Rectangle b = AnswerLayout.bounds(List.of(stroke));
            if (b != null && b.getMaxX() >= box.getMinX() && b.getMinX() <= box.getMaxX()) {
                hit.add(stroke);
            }
        }
        return hit;
    }
}
