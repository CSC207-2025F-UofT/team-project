package view.dashboard;

import interface_adapter.dashboard.DashboardItemState;
import interface_adapter.dashboard.DashboardStateCatalog;

import javax.swing.*;
import java.util.List;
import java.awt.*;
import java.util.Map;

/**
 * Render a line plot in Swing.
 */
public class LinePlotRenderer implements DashboardRenderer<DashboardStateCatalog.LinePlotState> {

    @Override
    public JComponent render(DashboardStateCatalog.LinePlotState state) {
        List<Integer> x_vals = state.x_vals();
        List<Integer> y_vals = state.y_vals();

        if (x_vals.isEmpty() || y_vals.isEmpty()) {
            throw new IllegalStateException("Provided x or y coordinate list is empty.");
        } else if (x_vals.size() != y_vals.size()) {
            throw new IllegalStateException("Provided x, y coordinate lists differ in length.");
        }

        return new JPanel() {
            @Override
            protected void paintComponent(Graphics graphics) {
                super.paintComponent(graphics);

                Graphics2D g = (Graphics2D) graphics;
                g.setRenderingHints(Map.of(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
                g.setStroke(new BasicStroke(3));
                int padding = 80;
                int innerPadding = 20;
                int plotWidth = getWidth() - 2 * padding;
                int plotHeight = getHeight() - 2 * padding;

                // Axes
                g.setColor(Color.GRAY);
                g.drawLine(padding, padding, padding, padding + plotHeight);
                g.drawLine(padding, padding + plotHeight, padding + plotWidth, padding + plotHeight);

                // Points and lines
                g.setColor(Color.BLACK);
                double x_max = x_vals.stream().max(Integer::compareTo).orElse(0);
                double y_max = y_vals.stream().max(Integer::compareTo).orElse(0);

                for (int i = 0; i < x_vals.size() - 1; i++) {
                    int x1 =  innerPadding + (int) (padding + x_vals.get(i) / x_max * (plotWidth - 2 * innerPadding));
                    int y1 =  (int) (padding + plotHeight - y_vals.get(i) / y_max * plotHeight);
                    int x2 =  innerPadding + (int) (padding + x_vals.get(i + 1) / x_max * (plotWidth - 2 * innerPadding));
                    int y2 =  (int) (padding + plotHeight - y_vals.get(i + 1) / y_max * plotHeight);
                    g.drawLine(x1, y1, x2, y2);
                    g.drawOval(x1 - 3, y1 - 3, 6, 6);

                    // ticks on the axes
                    g.drawLine(x1, padding + plotHeight - 3, x1, padding + plotHeight + 3);
                    g.drawLine(padding - 3, y1, padding + 3, y1);

                    // draw labels
                    String x_lab = state.dates().get(i);
                    String y_lab = state.counts().get(i).toString();
                    g.drawString(x_lab, x1 - 10, padding + plotHeight + 20);
                    g.drawString(y_lab, padding / 2, y1);
                }
                // draw final
                int x1 =  innerPadding + (int) (padding + x_vals.getLast() / x_max * (plotWidth - 2 * innerPadding));
                int y1 =  (int) (padding + plotHeight - y_vals.getLast() / y_max * plotHeight);
                g.drawOval(x1 - 3, y1 - 3, 6, 6);
                g.drawLine(x1, padding + plotHeight - 3, x1, padding + plotHeight + 3);
                g.drawLine(padding - 3, y1, padding + 3, y1);
                String x_lab = state.dates().getLast();
                String y_lab = state.counts().getLast().toString();
                g.drawString(x_lab, x1 - 10, padding + plotHeight + 20);
                g.drawString(y_lab, padding / 2, y1);
            }
        };
    }
}
