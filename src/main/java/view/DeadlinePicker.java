package view;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/** A date button whose popup opens on the currently selected month. */
final class DeadlinePicker extends JButton {
    private static final DateTimeFormatter MONTH = DateTimeFormatter.ofPattern("MMMM uuuu", Locale.ENGLISH);
    private LocalDate date;

    DeadlinePicker(LocalDate initialDate) {
        super(initialDate.toString());
        date = initialDate;
        getAccessibleContext().setAccessibleName("Choose deadline");
        addActionListener(event -> {
            JPopupMenu popup = new JPopupMenu();
            showMonth(popup, YearMonth.from(date));
            popup.show(this, 0, getHeight());
        });
    }

    LocalDate getDate() {
        return date;
    }

    private void showMonth(JPopupMenu popup, YearMonth month) {
        JPanel calendar = new JPanel(new BorderLayout());
        JPanel header = new JPanel(new BorderLayout());
        JButton previous = new JButton("‹");
        JButton next = new JButton("›");
        previous.getAccessibleContext().setAccessibleName("Previous month");
        next.getAccessibleContext().setAccessibleName("Next month");
        previous.setEnabled(!month.equals(YearMonth.from(LocalDate.MIN)));
        next.setEnabled(!month.equals(YearMonth.from(LocalDate.MAX)));
        previous.addActionListener(event -> showMonth(popup, month.minusMonths(1)));
        next.addActionListener(event -> showMonth(popup, month.plusMonths(1)));
        header.add(previous, BorderLayout.WEST);
        header.add(new JLabel(month.format(MONTH), SwingConstants.CENTER), BorderLayout.CENTER);
        header.add(next, BorderLayout.EAST);
        calendar.add(header, BorderLayout.NORTH);

        JPanel days = new JPanel(new GridLayout(7, 7, 1, 1));
        for (String weekday : new String[]{"Su", "Mo", "Tu", "We", "Th", "Fr", "Sa"}) {
            days.add(new JLabel(weekday, SwingConstants.CENTER));
        }
        int firstColumn = month.atDay(1).getDayOfWeek().getValue() % 7;
        for (int cell = 0; cell < 42; cell++) {
            int day = cell - firstColumn + 1;
            if (day < 1 || day > month.lengthOfMonth()) {
                days.add(new JLabel());
                continue;
            }
            LocalDate candidate = month.atDay(day);
            JButton button = new JButton(Integer.toString(day));
            button.setMargin(new Insets(3, 5, 3, 5));
            button.getAccessibleContext().setAccessibleName(candidate.toString());
            if (candidate.equals(date)) {
                button.setFont(button.getFont().deriveFont(Font.BOLD));
            }
            button.addActionListener(event -> {
                date = candidate;
                setText(date.toString());
                popup.setVisible(false);
            });
            days.add(button);
        }
        calendar.add(days, BorderLayout.CENTER);
        popup.removeAll();
        popup.add(calendar);
        popup.revalidate();
        popup.repaint();
    }
}
