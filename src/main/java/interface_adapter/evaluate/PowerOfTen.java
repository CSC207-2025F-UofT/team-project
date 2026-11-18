package interface_adapter.evaluate;

import java.math.BigDecimal;
import java.math.MathContext;

// huge numbers as m×10^n
final class PowerOfTen {
    private static final String TIMES_TEN = "×10";

    private PowerOfTen() {
    }

    // 2432902008176640000 -> 2.432902008×10^18
    static String write(BigDecimal value, int digits) {
        BigDecimal rounded = value.round(new MathContext(digits));
        int exponent = rounded.precision() - rounded.scale() - 1;
        return rounded.movePointLeft(exponent).stripTrailingZeros().toPlainString() + TIMES_TEN + "^" + exponent;
    }
}
