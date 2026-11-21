package stockprototype.comparison;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.OptionalDouble;

/** Paired raw closes and comparison metrics for exactly the same trading dates. */
public final class ComparisonResult {
    public final String firstSymbol;
    public final String secondSymbol;
    public final List<Day> days;
    public final BigDecimal firstChange;
    public final BigDecimal secondChange;
    public final BigDecimal firstProjection;
    public final BigDecimal secondProjection;
    public final OptionalDouble correlation;

    ComparisonResult(String firstSymbol, String secondSymbol, List<Day> days,
                     BigDecimal firstChange, BigDecimal secondChange, BigDecimal firstProjection,
                     BigDecimal secondProjection, OptionalDouble correlation) {
        this.firstSymbol = firstSymbol;
        this.secondSymbol = secondSymbol;
        this.days = List.copyOf(days);
        this.firstChange = firstChange;
        this.secondChange = secondChange;
        this.firstProjection = firstProjection;
        this.secondProjection = secondProjection;
        this.correlation = correlation;
    }

    public static final class Day {
        public final LocalDate date;
        public final BigDecimal firstClose;
        public final BigDecimal secondClose;
        public final BigDecimal firstIndex;
        public final BigDecimal secondIndex;

        Day(LocalDate date, BigDecimal firstClose, BigDecimal secondClose,
            BigDecimal firstIndex, BigDecimal secondIndex) {
            this.date = date;
            this.firstClose = firstClose;
            this.secondClose = secondClose;
            this.firstIndex = firstIndex;
            this.secondIndex = secondIndex;
        }
    }
}
