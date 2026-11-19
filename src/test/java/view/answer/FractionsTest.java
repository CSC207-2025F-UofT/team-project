package view.answer;

import entity.InkPoint;
import entity.Stroke;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FractionsTest {
    private static final double H = 40;

    private static Stroke stroke(int... xy) {
        Stroke s = new Stroke();
        for (int i = 0; i < xy.length; i += 2) {
            s.addPoint(new InkPoint(xy[i], xy[i + 1], i));
        }
        return s;
    }

    private final Stroke one = stroke(120, 100, 120, 140);      // 1, 40 tall
    private final Stroke bar = stroke(100, 150, 140, 150);
    private final Stroke two = stroke(105, 160, 135, 160, 105, 200, 135, 200);  // 2-ish under it

    @Test
    void topAndBarJoinTheBottom() {
        assertTrue(Fractions.across(List.of(one, bar), List.of(two), H));
        assertTrue(Fractions.across(List.of(two), List.of(one, bar), H));
    }

    @Test
    void barAloneJoinsTopAndBottom() {
        assertTrue(Fractions.across(List.of(bar), List.of(one, two), H));
    }

    @Test
    void dashWithNothingUnderItIsNoFraction() {
        assertFalse(Fractions.across(List.of(one), List.of(bar), H));
    }

    @Test
    void equalsBarsDontCountAsTopOrBottom() {
        Stroke lower = stroke(100, 165, 140, 165);
        assertFalse(Fractions.across(List.of(one, bar), List.of(lower), H));
    }

    @Test
    void inkOffToTheSideIsNotUnderTheBar() {
        Stroke side = stroke(200, 160, 230, 160, 200, 200, 230, 200);
        assertFalse(Fractions.across(List.of(one, bar), List.of(side), H));
    }

    @Test
    void bottomTooFarDownIsTheNextLine() {
        Stroke low = stroke(105, 190, 135, 190, 105, 230, 135, 230);  // 40 under the bar, past 3/4 char
        assertFalse(Fractions.across(List.of(one, bar), List.of(low), H));
    }
}
