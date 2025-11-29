package use_case.spending_report;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.JPanel;

public class ChartVisualizer {
    
    public static JPanel createChart(Map<String, Float> categoryData, String month, String chartType) {
        if ("Pie Chart".equals(chartType)) {
            return new SimplePieChartPanel(categoryData, month);
        } else {
            return new SimpleBarChartPanel(categoryData, month);
        }
    }
    
    public static class SimpleBarChartPanel extends JPanel {
        private final java.util.List<Map.Entry<String, Float>> entries;
        private final String month;

        public SimpleBarChartPanel(Map<String, Float> data, String month) {
            this.entries = new ArrayList<>(data.entrySet());
            this.month = month;
            setPreferredSize(new Dimension(800, 450));
        }

        @Override
        protected void paintComponent(Graphics g) { // just used ai for these cuz idk
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int width = getWidth();
            int height = getHeight();
            g2.setColor(Color.WHITE);
            g2.fillRect(0, 0, width, height);

            if (entries.isEmpty()) {
                g2.setColor(Color.BLACK);
                g2.drawString("No data to display for " + month, 10, 20);
                return;
            }

            float max = 0f;
            for (Map.Entry<String, Float> e : entries) {
                max = Math.max(max, e.getValue());
            }
            int padding = 40;
            int labelHeight = 20;
            int availableHeight = height - padding*2 - labelHeight;
            int availableWidth = width - padding*2;
            int barWidth = Math.max(10, availableWidth / Math.max(1, entries.size()) - 10);
            int x = padding;
            int yBase = height - padding - labelHeight;

            g2.setColor(Color.BLACK);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 16f));
            g2.drawString("Spending for " + month, padding, 20);

            g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 12f));
            for (Map.Entry<String, Float> e : entries) {
                float value = e.getValue();
                int barHeight = max > 0 ? Math.round((value / max) * availableHeight) : 0;
                int y = yBase - barHeight;
                g2.setColor(new Color(100, 150, 240));
                g2.fillRect(x, y, barWidth, barHeight);
                g2.setColor(Color.BLACK);
                g2.drawRect(x, y, barWidth, barHeight);
                String label = e.getKey();
                int strWidth = g2.getFontMetrics().stringWidth(label);
                int labelX = x + Math.max(0, (barWidth - strWidth) / 2);
                g2.drawString(label, labelX, yBase + 15);
                String valStr = String.format("$%.2f", value);
                int valWidth = g2.getFontMetrics().stringWidth(valStr);
                g2.drawString(valStr, x + Math.max(0, (barWidth - valWidth) / 2), y - 5);
                x += barWidth + 10;
            }
        }
    }

    public static class SimplePieChartPanel extends JPanel {
        private final java.util.List<Map.Entry<String, Float>> entries;
        private final String month;

        public SimplePieChartPanel(Map<String, Float> data, String month) {
            this.entries = new ArrayList<>(data.entrySet());
            this.month = month;
            setPreferredSize(new Dimension(800, 450));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();

            g2.setColor(Color.WHITE);
            g2.fillRect(0, 0, width, height);

            if (entries.isEmpty()) {
                g2.setColor(Color.BLACK);
                g2.drawString("No data to display for " + month, 10, 20);
                return;
            }

            g2.setColor(Color.BLACK);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 16f));
            g2.drawString("Spending for " + month, 20, 20);

            int diameter = Math.min(width, height) - 100;
            int x = (width - diameter) / 2;
            int y = (height - diameter) / 2;

            float total = 0;
            for (Map.Entry<String, Float> e : entries) total += e.getValue();

            float startAngle = 0;

            for (Map.Entry<String, Float> e : entries) {
                float value = e.getValue();
                float angle = (value / total) * 360f;

                g2.setColor(Color.getHSBColor((float)Math.random(), 0.5f, 0.9f));
                g2.fillArc(x, y, diameter, diameter, Math.round(startAngle), Math.round(angle));

                startAngle += angle;
            }

            int legendY = 40;
            g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 12f));
            for (Map.Entry<String, Float> e : entries) {
                g2.setColor(Color.BLACK);
                g2.drawString(e.getKey() + " ($" + e.getValue() + ")", 20, legendY);
                legendY += 20;
            }
        }
    }
}