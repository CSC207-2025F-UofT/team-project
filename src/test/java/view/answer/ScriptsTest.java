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
    void smallInkOffToTheSideIsNot() {
        List<Stroke> side = List.of(stroke(500, 240, 500, 270));
        assertFalse(Scripts.stackedOn(sum(), side));
    }
}
