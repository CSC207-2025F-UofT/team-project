package stockprototype.comparison.ui;

import stockprototype.comparison.ComparisonResult;

import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;
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
    private static final BasicStroke SOLID = new BasicStroke(2.5f);
    private static final BasicStroke DASHED = new BasicStroke(2.5f,
            BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 1, new float[]{7, 4}, 0);
    private ComparisonResult result;

    IndexedPriceChart() {
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
        boolean dark = getBackground().getRed() < 128;
        Color firstColor = dark ? new Color(100, 190, 255) : new Color(17, 94, 154);
        Color secondColor = dark ? new Color(255, 182, 100) : new Color(166, 73, 14);
        drawLegend(g, result.firstSymbol, 12, legendY, firstColor, SOLID);
        int secondX = 12 + 50 + font.stringWidth(result.firstSymbol);
        if (secondX + 30 + font.stringWidth(result.secondSymbol) > right) {
            secondX = 12;
            legendY += line + 2;
        }
        drawLegend(g, result.secondSymbol, secondX, legendY, secondColor, DASHED);
        g.setColor(getForeground());
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
        int left = 0;
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
        g.setStroke(new BasicStroke(1f));
        for (int i = 0; i < ticks.length; i++) {
            int y = bottom - Math.round((bottom - top) * i / 4f);
            Color ink = getForeground();
            g.setColor(new Color(ink.getRed(), ink.getGreen(), ink.getBlue(), 45));
            g.drawLine(left, y, right, y);
            g.setColor(ink);
            g.drawString(ticks[i], left - font.stringWidth(ticks[i]) - 6, y + font.getAscent() / 2);
        }
        LocalDate start = result.days.get(0).date;
        LocalDate end = result.days.get(result.days.size() - 1).date;
        int dateY = bottom + line + 2;
        g.drawString(start.toString(), left, dateY);
        int endLabelX = right - font.stringWidth(end.toString());
        if (endLabelX > left + font.stringWidth(start.toString()) + 4) {
            g.drawString(end.toString(), endLabelX, dateY);
        }

        Path2D first = new Path2D.Double();
        Path2D second = new Path2D.Double();
        for (int i = 0; i < result.days.size(); i++) {
            ComparisonResult.Day day = result.days.get(i);
            double elapsed = (double) (day.date.toEpochDay() - start.toEpochDay())
                    / (end.toEpochDay() - start.toEpochDay());
            double x = left + elapsed * (right - left);
            append(first, i, x, screenY(day.firstIndex, low, range, top, bottom));
            append(second, i, x, screenY(day.secondIndex, low, range, top, bottom));
        }
        g.setColor(firstColor);
        g.setStroke(SOLID);
        g.draw(first);
        g.setColor(secondColor);
        g.setStroke(DASHED);
        g.draw(second);
    }

    private static void append(Path2D path, int i, double x, double y) {
        if (i == 0) {
            path.moveTo(x, y);
        } else {
            path.lineTo(x, y);
        }
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

    private void drawLegend(Graphics2D g, String label, int x, int y, Color color, BasicStroke stroke) {
        g.setColor(color);
        g.setStroke(stroke);
        g.drawLine(x, y - 4, x + 22, y - 4);
        g.setColor(getForeground());
        g.drawString(label, x + 28, y);
    }
}
