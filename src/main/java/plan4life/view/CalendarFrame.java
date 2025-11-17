// File path: src/main/java/plan4life/view/CalendarFrame.java
package plan4life.view;

// --- 1. Import our new classes and required classes ---
import plan4life.controller.SettingsController;
import java.awt.event.ActionListener; // <-- This fixes the 'ActionListener' error
import java.awt.event.ActionEvent; // <-- This fixes the 'ActionEvent' error
// ----------------------------------------------------

import javax.swing.*;
import java.awt.*;


public class CalendarFrame extends JFrame {
    private final CalendarPanel calendarPanel;
    private final ActivityPanel activityPanel;

    // --- 2. Add member variables for SettingsView and Controller ---
    private SettingsView settingsView; // <-- This fixes the 'settingsView' error
    private SettingsController settingsController;
    // -----------------------------------------------------------

    public CalendarFrame() {
        super("Plan4Life - Scheduler");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 750);
        setLayout(new BorderLayout(10, 10));

        // --- 3. Initialize SettingsView and Controller ---
        // 'this' refers to the current CalendarFrame, which is the parent window for SettingsView
        this.settingsView = new SettingsView(this);
        this.settingsController = new SettingsController(this.settingsView);
        // --------------------------------------------------

        // --- top part with Day/Week and Generate Schedule buttons --->
        JPanel topBar = new JPanel(new BorderLayout());

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton dayBtn = new JButton("Day");
        JButton weekBtn = new JButton("Week");
        leftPanel.add(dayBtn);
        leftPanel.add(weekBtn);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton generateBtn = new JButton("Generate Schedule");

        // --- 4. Create "Settings" button and add to rightPanel ---
        JButton settingsBtn = new JButton("Settings"); // <-- This fixes the 'settingsBtn' error
        rightPanel.add(generateBtn);
        rightPanel.add(settingsBtn);
        // --------------------------------------------------------

        topBar.add(leftPanel, BorderLayout.WEST);
        topBar.add(rightPanel, BorderLayout.EAST);

        // --- Calendar Panel ---
        calendarPanel = new CalendarPanel();

        // --- Activities Panel ---
        activityPanel = new ActivityPanel();

        // --- Add to frame ---
        add(topBar, BorderLayout.NORTH);
        add(calendarPanel, BorderLayout.CENTER);
        add(activityPanel, BorderLayout.EAST);

        // --- Button Logic ---
        dayBtn.addActionListener((ActionEvent e) -> calendarPanel.setDayView());
        weekBtn.addActionListener((ActionEvent e) -> calendarPanel.setWeekView());

        // --- 5. Add logic for the "Settings" button ---
        settingsBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // When the button is clicked, show the SettingsView dialog
                settingsView.setVisible(true);
            }
        });
        // ----------------------------------------------

        setVisible(true);
    }
}