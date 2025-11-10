package plan4life.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class CalendarFrame extends JFrame {
    private final CalendarPanel calendarPanel;
    private final ActivityPanel activityPanel;

    public CalendarFrame() {
        super("Plan4Life - Scheduler");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 750);
        setLayout(new BorderLayout(10, 10));

        // <--- Top part with Day/Week and Generate Schedule buttons --->
        JPanel topBar = new JPanel(new BorderLayout());

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton dayBtn = new JButton("Day");
        JButton weekBtn = new JButton("Week");
        leftPanel.add(dayBtn);
        leftPanel.add(weekBtn);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton generateBtn = new JButton("Generate Schedule");
        rightPanel.add(generateBtn);

        topBar.add(leftPanel, BorderLayout.WEST);
        topBar.add(rightPanel, BorderLayout.EAST);

        // <--- Calendar Panel --->
        calendarPanel = new CalendarPanel();

        // <--- Activities Panel --->
        activityPanel = new ActivityPanel();

        // --- Add to frame ---
        add(topBar, BorderLayout.NORTH);
        add(calendarPanel, BorderLayout.CENTER);
        add(activityPanel, BorderLayout.EAST);

        // --- Button Logic ---
        dayBtn.addActionListener((ActionEvent e) -> calendarPanel.setDayView());
        weekBtn.addActionListener((ActionEvent e) -> calendarPanel.setWeekView());

        setVisible(true);
    }
}