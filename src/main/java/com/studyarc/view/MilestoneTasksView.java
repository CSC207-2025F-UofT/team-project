package com.studyarc.view;

import javax.swing.*;
import java.awt.*;

public class MilestoneTasksView extends JPanel {
    private JLabel planTitle;
    private JLabel focuses;
    private JButton addMilestone;
    private JButton save;

    public MilestoneTasksView() {
        final JPanel topDetails = new JPanel();
        planTitle = new JLabel("HCI Study Plan");
        planTitle.setFont(new Font("SansSerif", Font.BOLD, 24));
        topDetails.add(planTitle);

        focuses = new JLabel("Focus: ");
        topDetails.add(focuses);

        save = new JButton("Save Plan");
        topDetails.add(save);

        addMilestone = new JButton("Add Milestone");

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(topDetails, BorderLayout.NORTH);
        this.add(addMilestone, BorderLayout.CENTER);
    }
}
