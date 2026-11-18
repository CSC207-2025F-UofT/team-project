package view.answer;

import entity.InkPoint;
import entity.Stroke;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

// = at the end of the ink, found w/o the model
class EqualsSpotTest {
    private static Stroke s(int... xy) {
        Stroke s = new Stroke();
        for (int i = 0; i < xy.length; i += 2) {
            s.addPoint(new InkPoint(xy[i], xy[i + 1], i));
        }
        return s;
    }

    @Test
    void onePlusOneEqualsEndsInAPair() {
        Stroke top = s(155, 120, 185, 120);
        Stroke bottom = s(155, 130, 185, 130);
        List<Stroke> ink = List.of(s(100, 100, 100, 150), s(110, 125, 130, 125), s(120, 115, 120, 135),
                s(140, 100, 140, 150), top, bottom);
        assertEquals(List.of(top, bottom), EqualsSpot.trailingPair(ink, 50));
    }

    @Test
    void normalEqualsAfterShrunkInkStillCounts() {
        Stroke top = s(110, 101, 140, 101);
        Stroke bottom = s(110, 115, 140, 115);
        assertEquals(List.of(top, bottom), EqualsSpot.trailingPair(List.of(s(100, 100, 100, 116), top, bottom), 16));
    }

    @Test
    void minusOverAFractionBarIsNot() {
        assertTrue(EqualsSpot.trailingPair(List.of(s(105, 100, 130, 100), s(135, 90, 135, 110),
                s(100, 120, 160, 120), s(125, 130, 135, 150)), 20).isEmpty());
    }

    @Test
    void midFormulaEqualsIsNot() {
        assertTrue(EqualsSpot.trailingPair(List.of(s(100, 100, 100, 150), s(115, 120, 145, 120),
                s(115, 130, 145, 130), s(160, 100, 160, 150)), 50).isEmpty());
    }

    @Test
    void loneEqualsIsNot() {
        assertTrue(EqualsSpot.trailingPair(List.of(s(115, 120, 145, 120), s(115, 130, 145, 130)), 50).isEmpty());
    }

    @Test
    void strayDotAfterTheEqualsBlocksIt() {
        assertTrue(EqualsSpot.trailingPair(List.of(s(100, 100, 100, 150), s(115, 120, 145, 120),
                s(115, 130, 145, 130), s(160, 125, 161, 125)), 50).isEmpty());
    }

    @Test
    void pairOnItsOwnScale() {
        assertTrue(EqualsSpot.isEqualsPair(List.of(s(0, 0, 30, 0), s(0, 14, 30, 14))));
        assertFalse(EqualsSpot.isEqualsPair(List.of(s(0, 0, 30, 0), s(0, 0, 0, 40))));
        assertFalse(EqualsSpot.isEqualsPair(List.of(s(0, 0, 30, 0), s(0, 14, 30, 14), s(0, 28, 30, 28))));
    }

    @Test
    void topLevelEqualsOnly() {
        assertTrue(EqualsSpot.topLevelEquals("1+1="));
        assertFalse(EqualsSpot.topLevelEquals("\\sum_{i=1}^{3}i"));
        assertFalse(EqualsSpot.topLevelEquals(null));
    }
}
