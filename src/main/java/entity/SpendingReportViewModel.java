package entity;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import use_case.spending_report.GenerateReportController;

public class SpendingReportViewModel extends JFrame {
    private final JComboBox<String> monthDropdown;
    private final JComboBox<String> chartTypeDropdown;
    private final JPanel chartPanelContainer;
    private GenerateReportController controller;
    private final int userId = 1;

    public SpendingReportViewModel() {
        setTitle("Monthly Spending Report");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        monthDropdown = new JComboBox<>(new String[]{
            "January 2025", "February 2025", "March 2025", "April 2025",
            "May 2025", "June 2025", "July 2025", "August 2025",
            "September 2025", "October 2025", "November 2025"
        });

        chartTypeDropdown = new JComboBox<>(new String[]{
            "Bar Chart", "Pie Chart"
        });

        topPanel.add(new JLabel("Select Month:"));
        topPanel.add(new JLabel(" | Chart Type:"));
        topPanel.add(monthDropdown);
        topPanel.add(chartTypeDropdown);
        add(topPanel, BorderLayout.NORTH);

        chartPanelContainer = new JPanel(new BorderLayout());
        add(chartPanelContainer, BorderLayout.CENTER);

        monthDropdown.addActionListener((ActionEvent e) -> {
            if (controller != null) {
                String selectedMonth = (String) monthDropdown.getSelectedItem();
                controller.onGenerateReportClicked(userId, selectedMonth);
            } else {
                System.err.println("Controller not set yet!");
            }
        });

        chartTypeDropdown.addActionListener((ActionEvent e) -> {
            if (controller != null) {
                String selectedMonth = (String) monthDropdown.getSelectedItem();
                controller.onGenerateReportClicked(userId, selectedMonth);
            }
        });
    }

    public void setController(GenerateReportController controller) {
        this.controller = controller;
        SwingUtilities.invokeLater(() -> {
            monthDropdown.setSelectedItem("November 2025");
            if (controller != null) {
                controller.onGenerateReportClicked(userId, "November 2025");
            }
        });
    }

    public void displayChart(Map<String, Float> categoryData, String month) {
        if (categoryData == null || categoryData.isEmpty()) {
            chartPanelContainer.removeAll();
            chartPanelContainer.revalidate();
            chartPanelContainer.repaint();
            JOptionPane.showMessageDialog(this, "No transactions found for this month.");
            return;
        }
        chartPanelContainer.removeAll();
        String type = (String) chartTypeDropdown.getSelectedItem();
        
        if ("Pie Chart".equals(type)) {
            chartPanelContainer.add(new SimplePieChartPanel(categoryData, month), BorderLayout.CENTER);
        } else {
            chartPanelContainer.add(new SimpleBarChartPanel(categoryData, month), BorderLayout.CENTER);
        }

        chartPanelContainer.revalidate();
        chartPanelContainer.repaint();
    }

    public void setReport(entity.SpendingReport report) {
        if (report != null) {
            displayChart(report.getCategoryBreakdown(), report.getMonth());
        }
    }

    public void displayNoDataMessage() {
        displayChart(null, "");
    }

    private static class SimpleBarChartPanel extends JPanel {
        private final java.util.List<Map.Entry<String, Float>> entries;
        private final String month;

        SimpleBarChartPanel(Map<String, Float> data, String month) {
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

    private static class SimplePieChartPanel extends JPanel {
    private final java.util.List<Map.Entry<String, Float>> entries;
    private final String month;

    SimplePieChartPanel(Map<String, Float> data, String month) {
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