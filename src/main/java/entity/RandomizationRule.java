package entity;

import java.util.Locale;
import java.util.Random;

/** Immutable story configuration. */
public final class RandomizationRule {
    public final int minInclusive;
    public final int maxExclusive;
    public final int width;

    public RandomizationRule(int minInclusive,
                             int maxExclusive,
                             int width) {
        this.minInclusive = minInclusive;
        this.maxExclusive = maxExclusive;
        this.width = width;
        if (minInclusive >= maxExclusive || width < 0) throw new IllegalArgumentException("Invalid randomization range or width");
    }

    public String generate(Random random) {
        int value = random.ints(1, minInclusive, maxExclusive).findFirst().orElseThrow();
        return width == 0 ? Integer.toString(value) : String.format(Locale.ROOT, "%0" + width + "d", value);
    }
}
