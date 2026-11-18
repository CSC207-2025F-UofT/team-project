package view.answer;

import entity.Stroke;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

// whatever most of a formula's ink (by points) was written with: colour, pen width
public final class MostInk {
    private MostInk() {
    }

    // ties go to the first seen; fallback if no ink
    public static <T> T of(Collection<Stroke> formula, Function<Stroke, T> look, T fallback) {
        Map<T, Integer> amount = new LinkedHashMap<>();
        for (Stroke s : formula) {
            amount.merge(look.apply(s), Math.max(1, s.getPoints().size()), Integer::sum);
        }
        T best = fallback;
        int most = 0;
        for (Map.Entry<T, Integer> e : amount.entrySet()) {
            if (e.getValue() > most) {
                best = e.getKey();
                most = e.getValue();
            }
        }
        return best;
    }
}
