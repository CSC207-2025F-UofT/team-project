package com.studyarc.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SidePanelView extends JPanel{
    private final JPanel mainButtonPanel = new JPanel();
    private final JLabel logo = new JLabel("Study Arc");
    private final JLabel userLoggedIn = new JLabel("Logged In User");
    private final JButton seePlans;
    private final JButton seePapers;
    private final JButton seeJobs;

    public SidePanelView() {
        seePlans = new JButton("Plans");
        seePapers = new JButton("Papers");
        seeJobs = new JButton("Jobs");


        this.setLayout(new BorderLayout());
        mainButtonPanel.setLayout(new BoxLayout(mainButtonPanel, BoxLayout.Y_AXIS));

        this.add(logo, BorderLayout.NORTH);
        mainButtonPanel.add(seePlans);
        mainButtonPanel.add(seePapers);
        mainButtonPanel.add(seeJobs);

        this.add(mainButtonPanel, BorderLayout.CENTER);
        this.add(userLoggedIn, BorderLayout.SOUTH);
    }
}
