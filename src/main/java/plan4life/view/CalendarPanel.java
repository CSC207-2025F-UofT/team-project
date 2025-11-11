package plan4life.view;

import plan4life.entities.Schedule;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CalendarPanel extends JPanel {
    private JPanel[][] cells;
    private int currentColumns = 7;

    private int startRow = -1;
    private int endRow = -1;
    private int column = -1;
    private boolean dragging = false;

    public CalendarPanel() {
        setBorder(BorderFactory.createTitledBorder("Weekly Calendar"));
        buildGrid(7);
    }

    private void buildGrid(int columns) {
        removeAll();
        currentColumns = columns;
        int rows = 24;

        // Each component added to a GridLayout fills the next cell in the grid (left to right, then top to bottom)
        setLayout(new GridLayout(rows, columns, 2, 2));
        cells = new JPanel[rows][columns];

        for (int hour = 0; hour < rows; hour++) {
            for (int day = 0; day < columns; day++) {
                JPanel cell = new JPanel();
                cell.setBackground(Color.WHITE);
                cell.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                cells[hour][day] = cell;
                add(cell);
            }
        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                dragging = true;
                column = getColumnFromX(e.getX());
                startRow = getRowFromY(e.getY());
                endRow = startRow;
                updateSelection();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                dragging = false;

                if (column != -1 && startRow != -1 && endRow != -1) {
                    int min = Math.min(startRow, endRow);
                    int max = Math.max(startRow, endRow);
                    String startTime = min + ":00";
                    String endTime = (max + 1) + ":00";

                    String dayLabel;
                    if (currentColumns == 1) {
                        dayLabel = "Day View";
                    } else {
                        dayLabel = "Day " + (column + 1);
                    }

                    System.out.printf("Selected %s from %s to %s%n", dayLabel, startTime, endTime);
                }
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (dragging) {
                    int newRow = getRowFromY(e.getY());
                    if (newRow != endRow) {
                        endRow = newRow;
                        updateSelection();
                    }
                }
            }
        });

        revalidate();
        repaint();
    }

    public void setDayView() {
        setBorder(BorderFactory.createTitledBorder("Daily Calendar"));
        buildGrid(1);
    }

    public void setWeekView() {
        setBorder(BorderFactory.createTitledBorder("Weekly Calendar"));
        buildGrid(7);
    }

    private int getColumnFromX(int x) {
        int cellWidth = getWidth() / currentColumns;
        return Math.max(0, Math.min(currentColumns - 1, x / cellWidth));
    }

    private int getRowFromY(int y) {
        for (int r = 0; r < 24; r++) {
            Rectangle bounds = cells[r][0].getBounds();
            if (y >= bounds.y && y < bounds.y + bounds.height) {
                return r;
            }
        }

        Rectangle last = cells[23][0].getBounds();
        if (y > last.y + last.height + 3) {
            return 23;
        }

        return Math.max(0, Math.min(23, 23 * y / getHeight()));  // Approximate row if cursor is between cells
    }

    private void updateSelection() {
        for (int r = 0; r < 24; r++) {
            for (int c = 0; c < currentColumns; c++) {
                cells[r][c].setBackground(Color.WHITE);
            }
        }

        if (column != -1 && startRow != -1) {
            int min = Math.min(startRow, endRow);
            int max = Math.max(startRow, endRow);
            for (int r = min; r <= max; r++) {
                cells[r][column].setBackground(new Color(173, 216, 230)); // light blue
            }
        }
    }

    public void updateSchedule(Schedule schedule) {
    }

    public void clear() {
        for (int r = 0; r < 24; r++) {
            for (int c = 0; c < currentColumns; c++) {
                cells[r][c].setBackground(Color.WHITE);
                cells[r][c].removeAll();
            }
        }
    }

    public void colorCell(String time, Color color, String label) {
        try {
            int hour = Integer.parseInt(time.split(":")[0]);
            if (hour >= 0 && hour < 24) {
                cells[hour][0].setBackground(color); // first column for now
                cells[hour][0].add(new JLabel(label));
            }
        } catch (Exception ignored) {}
    }
}