package stockprototype.comparison;

import stockprototype.StockDataSource;
import stockprototype.StockSymbol;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.OptionalDouble;
import java.util.TreeSet;

public final class ComparisonInteractor implements CompareStocks {
    private static final MathContext PRECISION = MathContext.DECIMAL128;
    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);
    private final StockDataSource dataSource;

    public ComparisonInteractor(StockDataSource dataSource) {
        this.dataSource = Objects.requireNonNull(dataSource);
    }

    @Override
    public ComparisonResult compare(String first, String second, int maxSessions) throws IOException {
        String firstSymbol = StockSymbol.normalize(first);
        String secondSymbol = StockSymbol.normalize(second);
        if (firstSymbol.equals(secondSymbol)) {
            throw new IllegalArgumentException("Enter two different stock symbols.");
        }
        if (maxSessions < 0 || maxSessions == 1) {
            throw new IllegalArgumentException("Choose a period with at least two trading dates.");
        }

        NavigableMap<LocalDate, BigDecimal> firstPrices = dataSource.getDailyClosingPrices(firstSymbol);
        NavigableMap<LocalDate, BigDecimal> secondPrices = dataSource.getDailyClosingPrices(secondSymbol);
        TreeSet<LocalDate> sharedDates = new TreeSet<>();
        sharedDates.addAll(firstPrices.keySet());
        sharedDates.retainAll(secondPrices.keySet());
        if (sharedDates.size() < 2) {
            throw new IllegalArgumentException("These stocks need at least two closing prices on matching dates.");
        }
        while (maxSessions > 0 && sharedDates.size() > maxSessions) {
            sharedDates.pollFirst();
        }

        BigDecimal firstBase = closeOn(firstPrices, sharedDates.first());
        BigDecimal secondBase = closeOn(secondPrices, sharedDates.first());
        List<ComparisonResult.Day> days = new ArrayList<>();
        for (LocalDate date : sharedDates) {
            BigDecimal a = closeOn(firstPrices, date);
            BigDecimal b = closeOn(secondPrices, date);
            days.add(new ComparisonResult.Day(date, a, b, index(a, firstBase), index(b, secondBase)));
        }
        ComparisonResult.Day last = days.get(days.size() - 1);
        return new ComparisonResult(firstSymbol, secondSymbol, days,
                last.firstIndex.subtract(HUNDRED), last.secondIndex.subtract(HUNDRED),
                project(firstBase, last.firstClose, days.size() - 1),
                project(secondBase, last.secondClose, days.size() - 1), correlation(days));
    }

    private static BigDecimal closeOn(NavigableMap<LocalDate, BigDecimal> prices, LocalDate date) {
        BigDecimal close = prices.get(date);
        if (close == null || close.signum() <= 0) {
            throw new IllegalArgumentException("A closing price on " + date + " is missing or nonpositive.");
        }
        return close;
    }

    private static BigDecimal index(BigDecimal price, BigDecimal base) {
        return price.multiply(HUNDRED).divide(base, PRECISION);
    }

    /** Extend the observed geometric growth rate by one comparable interval. */
    private static BigDecimal project(BigDecimal start, BigDecimal end, int intervals) {
        BigDecimal ratio = end.divide(start, PRECISION);
        if (intervals == 1) {
            return end.multiply(ratio, PRECISION);
        }
        double growthFactor = Math.pow(ratio.doubleValue(), 1.0 / intervals);
        if (!Double.isFinite(growthFactor) || growthFactor <= 0) {
            throw new IllegalArgumentException("The price change is outside the supported projection range.");
        }
        return end.multiply(BigDecimal.valueOf(growthFactor), PRECISION);
    }

    private static BigDecimal relativeChange(BigDecimal current, BigDecimal previous) {
        return current.subtract(previous).divide(previous, PRECISION);
    }

    /** Pearson correlation between paired changes from one shared observation to the next. */
    private static OptionalDouble correlation(List<ComparisonResult.Day> days) {
        if (days.size() < 3) {
            return OptionalDouble.empty();
        }
        BigDecimal sumX = BigDecimal.ZERO;
        BigDecimal sumY = BigDecimal.ZERO;
        BigDecimal sumXX = BigDecimal.ZERO;
        BigDecimal sumYY = BigDecimal.ZERO;
        BigDecimal sumXY = BigDecimal.ZERO;
        for (int i = 1; i < days.size(); i++) {
            ComparisonResult.Day previous = days.get(i - 1);
            ComparisonResult.Day current = days.get(i);
            BigDecimal x = relativeChange(current.firstClose, previous.firstClose);
            BigDecimal y = relativeChange(current.secondClose, previous.secondClose);
            sumX = sumX.add(x);
            sumY = sumY.add(y);
            sumXX = sumXX.add(x.multiply(x));
            sumYY = sumYY.add(y.multiply(y));
            sumXY = sumXY.add(x.multiply(y));
        }
        BigDecimal n = BigDecimal.valueOf(days.size() - 1);
        BigDecimal varianceX = n.multiply(sumXX).subtract(sumX.multiply(sumX));
        BigDecimal varianceY = n.multiply(sumYY).subtract(sumY.multiply(sumY));
        if (varianceX.signum() == 0 || varianceY.signum() == 0) {
            return OptionalDouble.empty();
        }
        BigDecimal covariance = n.multiply(sumXY).subtract(sumX.multiply(sumY));
        BigDecimal denominator = varianceX.multiply(varianceY).sqrt(PRECISION);
        double r = covariance.divide(denominator, PRECISION).doubleValue();
        return OptionalDouble.of(Math.max(-1.0, Math.min(1.0, r)));
    }
}
