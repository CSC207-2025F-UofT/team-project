package stockprototype.comparison.ui;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Locale;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import stockprototype.comparison.ComparisonResult;
import stockprototype.comparison.ComparisonResult.Day;

final class ComparisonPresenter {
    interface View {
        void startLoading();
        void showError(String message);
        void showComparison(ComparisonResult result, String period, String performance,
                            String correlation, TableModel summary, TableModel history);
    }

    private final View view;

    ComparisonPresenter(View view) {
        this.view = view;
    }

    void start() {
        view.startLoading();
    }

    void fail(String message) {
        view.showError(message);
    }

    void present(ComparisonResult result) {
        Day first = result.days.get(0);
        Day last = result.days.get(result.days.size() - 1);
        String period = first.date + " to " + last.date + "   ·   "
                + result.days.size() + " shared trading dates";
        String correlation = result.correlation.isPresent()
                ? String.format(Locale.ROOT, "Return correlation: %.3f", result.correlation.getAsDouble())
                : "Return correlation unavailable: needs two or more returns that vary for each stock.";

        TableModel summary = table(new String[]{"Symbol", "First close", "Last close", "Price change",
                "Next close estimate"}, new Object[][]{
            {result.firstSymbol, price(first.firstClose), price(last.firstClose),
                    percent(result.firstChange), estimate(result.firstProjection)},
            {result.secondSymbol, price(first.secondClose), price(last.secondClose),
                    percent(result.secondChange), estimate(result.secondProjection)}
        });

        Object[][] history = new Object[result.days.size()][3];
        for (int row = 0; row < history.length; row++) {
            Day day = result.days.get(row);
            history[row] = new Object[]{day.date, price(day.firstClose), price(day.secondClose)};
        }
        view.showComparison(result, period, performance(result), correlation, summary,
                table(new String[]{"Trading date", result.firstSymbol + " close",
                        result.secondSymbol + " close"}, history));
    }

    private static TableModel table(String[] headings, Object[][] rows) {
        return new DefaultTableModel(rows, headings) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private static String performance(ComparisonResult result) {
        BigDecimal difference = result.firstChange.subtract(result.secondChange);
        if (difference.signum() == 0) {
            return "Both stocks had the same percentage price change.";
        }
        String leader = difference.signum() > 0 ? result.firstSymbol : result.secondSymbol;
        BigDecimal gap = difference.abs();
        String distance = gap.compareTo(new BigDecimal("0.01")) < 0 ? "less than 0.01"
                : gap.setScale(2, RoundingMode.HALF_UP).toPlainString();
        return leader + " led by " + distance + " percentage points.";
    }

    private static String percent(BigDecimal value) {
        if (value.signum() != 0 && value.abs().compareTo(new BigDecimal("0.01")) < 0) {
            return value.signum() > 0 ? "< +0.01%" : "> −0.01%";
        }
        String sign = value.signum() > 0 ? "+" : "";
        return sign + value.setScale(2, RoundingMode.HALF_UP).toPlainString() + "%";
    }

    private static String price(BigDecimal value) {
        return value.stripTrailingZeros().toPlainString();
    }

    private static String estimate(BigDecimal value) {
        if (value.abs().compareTo(new BigDecimal("0.0001")) < 0) {
            return value.round(MathContext.DECIMAL32).stripTrailingZeros().toString();
        }
        return price(value.setScale(4, RoundingMode.HALF_UP));
    }
}
