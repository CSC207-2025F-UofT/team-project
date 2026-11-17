package view.answer;

import entity.InkPoint;
import entity.Stroke;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ScriptsTest {

    private static Stroke stroke(int... xy) {
        Stroke s = new Stroke();
        for (int i = 0; i < xy.length; i += 2) {
            s.addPoint(new InkPoint(xy[i], xy[i + 1], i));
        }
        return s;
    }

    // big Σ at 100..220 x, 100..220 y, then "n+1" 60 tall
    private static List<Stroke> sum() {
        return List.of(
                stroke(220, 100, 100, 100, 160, 160, 100, 220, 220, 220),  // Σ
                stroke(240, 130, 240, 190, 280, 130, 280, 190),            // n-ish
                stroke(300, 160, 340, 160), stroke(320, 140, 320, 180),    // +
                stroke(360, 130, 360, 190));                               // 1
    }

    // "n=0" 30 tall, top a gap below Σ
    private static List<Stroke> lowerLimit(int gap) {
        int top = 220 + gap;
        return List.of(
                stroke(120, top, 120, top + 30, 140, top),                 // n
                stroke(150, top + 10, 170, top + 10), stroke(150, top + 20, 170, top + 20),  // =
                stroke(180, top, 200, top + 30, 180, top + 30));           // 0
    }

    @Test
    void limitUnderTheSumIsStacked() {
        assertTrue(Scripts.stackedOn(sum(), lowerLimit(30)));  // half a char
    }

    @Test
    void limitOverTheSumIsStacked() {
        List<Stroke> ten = List.of(stroke(140, 55, 140, 85), stroke(160, 55, 180, 85, 160, 85));
        assertTrue(Scripts.stackedOn(sum(), ten));
    }

    @Test
    void bodyIsNotStackedOnItsLimit() {
        assertFalse(Scripts.stackedOn(lowerLimit(30), sum()));
    }

    @Test
    void limitTooFarDownIsNot() {
        assertFalse(Scripts.stackedOn(sum(), lowerLimit(90)));  // 1.5 chars
    }

    @Test
    void fullSizeNextLineIsNot() {
        List<Stroke> next = List.of(stroke(120, 260, 120, 320), stroke(160, 260, 160, 320));
        assertFalse(Scripts.stackedOn(sum(), next));
    }

    @Test
    void lonelyDashUnderIsNot() {
        assertFalse(Scripts.stackedOn(sum(), List.of(stroke(120, 250, 200, 250))));  // next line's "-"
    }

    @Test
    void smallInkOffToTheSideIsNot() {
        List<Stroke> side = List.of(stroke(500, 240, 500, 270));
        assertFalse(Scripts.stackedOn(sum(), side));
    }

    // Σ 64 tall, then lowercase "n+2" 40 line: n / + drag the median down
    private static List<Stroke> sumLowercase() {
        return List.of(
                stroke(251, 200, 200, 200, 229, 232, 200, 264, 251, 264),  // Σ
                stroke(261, 222, 261, 246), stroke(261, 230, 271, 222, 281, 246),  // n, x-height
                stroke(289, 236, 309, 236), stroke(299, 226, 299, 246),    // +
                stroke(317, 214, 323, 212, 331, 220, 317, 254, 339, 254)); // 2, full height
    }

    @Test
    void digitSizedLimitOverALowercaseBodyIsStacked() {
        List<Stroke> five = List.of(stroke(237, 164, 223, 164, 222, 177, 236, 182, 230, 194, 221, 192));  // 30 tall
        assertTrue(Scripts.stackedOn(sumLowercase(), five));
    }

    // (x+1)/2 = , numerator over a bar over the 2
    private static List<Stroke> fraction() {
        return List.of(
                stroke(200, 176, 220, 200), stroke(220, 176, 200, 200),    // x
                stroke(225, 184, 245, 184), stroke(235, 174, 235, 194),    // +
                stroke(257, 168, 265, 160, 265, 200),                      // 1
                stroke(195, 205, 285, 205),                                // bar
                stroke(230, 220, 236, 212, 250, 218, 230, 252, 252, 252),  // 2
                stroke(300, 207, 322, 207), stroke(300, 216, 322, 216));   // =
    }

    @Test
    void lineWellUnderAFractionIsNot() {
        List<Stroke> next = List.of(stroke(205, 318, 212, 312, 212, 340),  // 1, 28 tall, 1.5 lines down
                stroke(220, 326, 234, 326), stroke(227, 319, 227, 333),    // +
                stroke(240, 318, 247, 312, 247, 340));                     // 1
        assertFalse(Scripts.stackedOn(fraction(), next));
    }

    @Test
    void limitStillCountsOnceTheOtherLimitIsIn() {
        List<Stroke> body = new java.util.ArrayList<>(sumLowercase());
        body.add(stroke(205, 270, 205, 282, 210, 270, 215, 282));   // lower "n=0" already joined, small
        body.add(stroke(219, 274, 229, 274));
        body.add(stroke(219, 279, 229, 279));
        body.add(stroke(233, 270, 233, 290, 241, 290, 241, 270, 233, 270));
        List<Stroke> five = List.of(stroke(237, 164, 223, 164, 222, 177, 236, 182, 230, 194, 221, 192));
        assertTrue(Scripts.stackedOn(body, five));
    }
}
