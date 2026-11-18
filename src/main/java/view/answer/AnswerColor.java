package view.answer;

import entity.Stroke;

import java.awt.Color;
import java.util.Collection;
import java.util.function.Function;

// answer goes in the colour most of its formula was written in
public final class AnswerColor {
    private AnswerColor() {
    }

    // most ink (by points) wins, ties go to the first seen; fallback if no ink
    public static Color of(Collection<Stroke> formula, Function<Stroke, Color> colorOf, Color fallback) {
        return MostInk.of(formula, colorOf, fallback);
    }
}
