package plan4life.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import plan4life.entities.BlockedTime;
import plan4life.entities.Schedule;
import plan4life.use_case.block_off_time.BlockOffTimeController;

import java.time.LocalDateTime;
import java.util.Random; //Temp till we get langchain/langgraph working

public class CalendarFrame extends JFrame implements CalendarViewInterface, TimeSelectionListener {
    private final CalendarPanel calendarPanel;
    private final ActivityPanel activityPanel;
    private BlockOffTimeController blockOffTimeController;

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
        this.calendarPanel = new CalendarPanel();
        calendarPanel.setTimeSelectionListener(this);

        // <--- Activities Panel --->
        this.activityPanel = new ActivityPanel();

        // --- Add to frame ---
        add(topBar, BorderLayout.NORTH);
        add(calendarPanel, BorderLayout.CENTER);
        add(activityPanel, BorderLayout.EAST);

        // --- Button Logic ---
        dayBtn.addActionListener((ActionEvent e) -> calendarPanel.setDayView());
        weekBtn.addActionListener((ActionEvent e) -> calendarPanel.setWeekView());
    }

    public CalendarFrame(BlockOffTimeController blockOffTimeController) {
        this();
        this.blockOffTimeController = blockOffTimeController;
    }

    public void setBlockOffTimeController(BlockOffTimeController controller) {
        this.blockOffTimeController = controller;
    }

    @Override
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    @Override
    public void displaySchedule(Schedule schedule) {
        if (schedule == null) return;

        // This is where you’ll color each entry in the schedule
        // For now, until Activities exist, let’s simulate visually:
        calendarPanel.clear();

        Random random = new Random(); //Temp till we get langchain/langgraph working

        schedule.getActivities().forEach((time, activityName) -> {
            Color color = new Color(random.nextInt(156) + 100,
                    random.nextInt(156) + 100,
                    random.nextInt(156) + 100);
            calendarPanel.colorCell(time, color, activityName);
        });

        if (schedule.getBlockedTimes() != null) {
            for (BlockedTime block : schedule.getBlockedTimes()) {
                calendarPanel.colorBlockedRange(
                        block.getStart(),
                        block.getEnd()
                );
            }
        }

        calendarPanel.repaint();
    }

    @Override
    public void onTimeSelected(LocalDateTime start, LocalDateTime end, int scheduleId) {
        String description = JOptionPane.showInputDialog(this,
                "Optional description for this blocked time:");

        if (description == null) {
            return; // user pressed Cancel
        }

        blockOffTimeController.blockTime(scheduleId, start, end, description);
    }
}