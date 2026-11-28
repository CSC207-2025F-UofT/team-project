package stockprototype.comparison.ui;

import stockprototype.comparison.ComparisonResult;

import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.util.Locale;

/** Closing prices indexed to 100 on the first shared date. */
final class IndexedPriceChart extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final MathContext PRECISION = MathContext.DECIMAL128;
    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);
    private static final BasicStroke SOLID = new BasicStroke(2.2f);
    private static final float[] DASH = {8, 5};
    private static final float DASH_PERIOD = DASH[0] + DASH[1];
    private ComparisonResult result;

    IndexedPriceChart() {
        setOpaque(true);
        setBackground(ComparisonTheme.BACKGROUND);
        setForeground(ComparisonTheme.TEXT);
        setPreferredSize(new Dimension(580, 270));
        getAccessibleContext().setAccessibleName("Indexed closing prices");
    }

    void setResult(ComparisonResult result) {
        this.result = result;
        getAccessibleContext().setAccessibleDescription(result == null ? "No comparison selected"
                : result.firstSymbol + " and " + result.secondSymbol
                + " indexed to 100 at the first common close. Exact closes are in the price history.");
        repaint();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics.create();
        try {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(getForeground());
            if (result == null || result.days.size() < 2) {
                g.drawString("Compare two stocks to see their price histories.", 12, 26);
                return;
            }
            paintComparison(g);
        } finally {
            g.dispose();
        }
    }

    private void paintComparison(Graphics2D g) {
        FontMetrics font = g.getFontMetrics();
        int line = font.getHeight();
        int right = getWidth() - 14;
        int bottom = getHeight() - line - 12;
        int legendY = 8 + line;
        drawLegend(g, result.firstSymbol, 12, legendY, SOLID);
        int secondX = 12 + 50 + font.stringWidth(result.firstSymbol);
        if (secondX + 30 + font.stringWidth(result.secondSymbol) > right) {
            secondX = 12;
            legendY += line + 2;
        }
        drawLegend(g, result.secondSymbol, secondX, legendY, dashed(0));
        g.setColor(ComparisonTheme.MUTED);
        g.drawString("Index (first close = 100)", 12, legendY + line + 4);

        BigDecimal min = HUNDRED;
        BigDecimal max = HUNDRED;
        for (ComparisonResult.Day day : result.days) {
            min = min.min(day.firstIndex).min(day.secondIndex);
            max = max.max(day.firstIndex).max(day.secondIndex);
        }
        // A one-point margin keeps flat or nearly flat series on a readable scale.
        BigDecimal margin = max.subtract(min, PRECISION).movePointLeft(1).max(BigDecimal.ONE);
        BigDecimal low = min.subtract(margin, PRECISION).max(BigDecimal.ZERO);
        BigDecimal high = max.add(margin, PRECISION);
        BigDecimal range = high.subtract(low, PRECISION);
        int top = legendY + line * 2 + 4;
        int left = font.stringWidth("100");
        String[] ticks = new String[5];
        for (int i = 0; i < ticks.length; i++) {
            BigDecimal fraction = BigDecimal.valueOf(i * 25L, 2);
            ticks[i] = axisLabel(low.add(range.multiply(fraction, PRECISION), PRECISION));
            left = Math.max(left, font.stringWidth(ticks[i]));
        }
        left += 17;
        if (right - left < 50 || bottom - top < 36) {
            return;
        }
        double baseline = screenY(HUNDRED, low, range, top, bottom);
        int baselineTick = (int) Math.round(baseline);
        g.setStroke(new BasicStroke(1f));
        for (int i = 0; i < ticks.length; i++) {
            int y = bottom - Math.round((bottom - top) * i / 4f);
            if (Math.abs(y - baselineTick) >= line) {
                g.setColor(ComparisonTheme.GRID);
                g.drawLine(left, y, right, y);
                g.setColor(ComparisonTheme.MUTED);
                g.drawString(ticks[i], left - font.stringWidth(ticks[i]) - 6, y + font.getAscent() / 2);
            }
        }
        g.setColor(ComparisonTheme.MUTED);
        g.draw(new Line2D.Double(left, baseline, right, baseline));
        g.setColor(ComparisonTheme.TEXT);
        g.drawString("100", left - font.stringWidth("100") - 6, baselineTick + font.getAscent() / 2);
        g.setColor(ComparisonTheme.MUTED);
        LocalDate start = result.days.get(0).date;
        LocalDate end = result.days.get(result.days.size() - 1).date;
        int dateY = bottom + line + 2;
        g.drawString(start.toString(), left, dateY);
        int endLabelX = right - font.stringWidth(end.toString());
        if (endLabelX > left + font.stringWidth(start.toString()) + 4) {
            g.drawString(end.toString(), endLabelX, dateY);
        }

        for (int series = 0; series < 2; series++) {
            double lastX = left;
            double lastY = baseline;
            double length = 0;
            boolean useDashes = series == 1;
            for (int i = 1; i < result.days.size(); i++) {
                ComparisonResult.Day day = result.days.get(i);
                double elapsed = (double) (day.date.toEpochDay() - start.toEpochDay())
                        / (end.toEpochDay() - start.toEpochDay());
                double x = left + elapsed * (right - left);
                double y = screenY(useDashes ? day.secondIndex : day.firstIndex, low, range, top, bottom);
                // A segment that crosses the starting price changes color at the crossing.
                if ((lastY < baseline && y > baseline) || (lastY > baseline && y < baseline)) {
                    double crossX = lastX + (x - lastX) * (baseline - lastY) / (y - lastY);
                    drawSegment(g, lastX, lastY, crossX, baseline, baseline, useDashes, length);
                    length += Math.hypot(crossX - lastX, baseline - lastY);
                    lastX = crossX;
                    lastY = baseline;
                }
                drawSegment(g, lastX, lastY, x, y, baseline, useDashes, length);
                length += Math.hypot(x - lastX, y - lastY);
                lastX = x;
                lastY = y;
            }
        }
    }

    private static void drawSegment(Graphics2D g, double x1, double y1, double x2, double y2,
                                    double baseline, boolean useDashes, double length) {
        double middle = (y1 + y2) / 2;
        if (middle < baseline) {
            g.setColor(ComparisonTheme.GAIN);
        } else if (middle > baseline) {
            g.setColor(ComparisonTheme.LOSS);
        } else {
            g.setColor(ComparisonTheme.TEXT);
        }
        g.setStroke(useDashes ? dashed(length) : SOLID);
        g.draw(new Line2D.Double(x1, y1, x2, y2));
    }

    private static BasicStroke dashed(double length) {
        return new BasicStroke(3.2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND,
                1, DASH, (float) (length % DASH_PERIOD));
    }

    private static double screenY(BigDecimal value, BigDecimal low, BigDecimal range, int top, int bottom) {
        double fraction = value.subtract(low, PRECISION).divide(range, PRECISION).doubleValue();
        return bottom - fraction * (bottom - top);
    }

    private static String axisLabel(BigDecimal value) {
        if (value.compareTo(BigDecimal.valueOf(1_000_000)) >= 0) {
            return new DecimalFormat("0.##E0", DecimalFormatSymbols.getInstance(Locale.ROOT)).format(value);
        }
        return value.setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString();
    }

    private void drawLegend(Graphics2D g, String label, int x, int y, BasicStroke stroke) {
        g.setColor(ComparisonTheme.TEXT);
        g.setStroke(stroke);
        g.drawLine(x, y - 4, x + 22, y - 4);
        g.setColor(getForeground());
        g.drawString(label, x + 28, y);
    }
}
