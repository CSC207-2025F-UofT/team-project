// plan4life.view.ColoredActivities.java
package plan4life.view;

import plan4life.entities.Schedule;
import plan4life.entities.Activity;

import java.awt.*;
import java.util.Random;

public class ColoredActivities implements CalendarViewInterface {
    private final CalendarPanel calendarPanel;
    private final Random random = new Random();

    public ColoredActivities(CalendarPanel calendarPanel) {
        this.calendarPanel = calendarPanel;
    }

    @Override
    public void displaySchedule(Schedule schedule) {
        if (schedule == null) return;

        // Example: clear background (uses existing CalendarPanel method)
        calendarPanel.setWeekView(); // reset or keep as is

        // Assign a random color for each activity
        for (Activity activity : schedule.getActivities()) {
            Color color = new Color(random.nextInt(156) + 100,  // avoid dark colors
                    random.nextInt(156) + 100,
                    random.nextInt(156) + 100);

            paintActivity(activity, color);
        }

        calendarPanel.repaint();
    }

    private void paintActivity(Activity activity, Color color) {
        try {
            int day = activity.getDayIndex();
            int start = activity.getStartHour();
            int end = activity.getEndHour() - 1;

            for (int r = start; r <= end; r++) {
                // We’ll use reflection to access CalendarPanel’s cells indirectly
                // without modifying its source.
                java.lang.reflect.Field cellsField = CalendarPanel.class.getDeclaredField("cells");
                cellsField.setAccessible(true);
                JPanel[][] cells = (JPanel[][]) cellsField.get(calendarPanel);
                cells[r][day].setBackground(color);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
