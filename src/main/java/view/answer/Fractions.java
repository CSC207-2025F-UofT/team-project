package view.answer;

import entity.Stroke;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

// a bar w/ chars right over and under it, d/dx or 1/2 written alone w/ nothing beside to tie the rows
public final class Fractions {
    static final double FLAT = 0.35;     // bar at most this tall (x char height)
    static final double MIN_BAR = 0.5;   // and at least this wide
    static final double REACH = 0.75;    // gap bar <-> top/bottom, up to 3/4 of a char

    private Fractions() {
    }

    // some fraction has bits in both a and b
    public static boolean across(Collection<Stroke> a, Collection<Stroke> b, double h) {
        List<Stroke> all = new ArrayList<>(a);
        all.addAll(b);
        for (Stroke bar : all) {
            List<Stroke> fraction = around(bar, all, h);
            if (!fraction.isEmpty() && !Collections.disjoint(fraction, a) && !Collections.disjoint(fraction, b)) {
                return true;
            }
        }
        return false;
    }

    // bar + the chars right over/under it, empty if it's not a bar w/ both
    private static List<Stroke> around(Stroke bar, List<Stroke> ink, double h) {
        Rectangle r = AnswerLayout.bounds(List.of(bar));
        if (r == null || r.height > FLAT * h || r.width < MIN_BAR * h) {
            return List.of();
        }
        List<Stroke> fraction = new ArrayList<>(List.of(bar));
        boolean over = false;
        boolean under = false;
        for (Stroke s : ink) {
            Rectangle b = AnswerLayout.bounds(List.of(s));
            if (s == bar || b == null || !AnswerLayout.hasChars(List.of(s))
                    || b.getCenterX() < r.getMinX() || b.getCenterX() > r.getMaxX()) {
                continue;  // "-" "=" don't count, has to sit over the bar
            }
            if (b.getMaxY() <= r.getMinY() && r.getMinY() - b.getMaxY() <= REACH * h) {
                over = true;
                fraction.add(s);
            } else if (b.getMinY() >= r.getMaxY() && b.getMinY() - r.getMaxY() <= REACH * h) {
                under = true;
                fraction.add(s);
            }
        }
        return over && under ? fraction : List.of();
    }
}
