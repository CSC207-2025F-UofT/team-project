package com.studyarc.use_case.track_plan.TestOnUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoggedInView extends JPanel  {

    String viewName = "logged in";
    public final JPanel sidePanel = new JPanel();
    public final JPanel contentPanel = new JPanel();
    JPanel parent;
    CardLayout parentcardlayout;


    private final JPanel mainButtonPanel = new JPanel();
    private final JLabel logo = new JLabel("Study Arc");
    private final JLabel userLoggedIn = new JLabel("Logged In User");


    private final JButton seePlans = new JButton("Plans");
    private final JButton seePapers = new JButton("Papers");
    private final JButton seeJobs = new JButton("Jobs");
    private final JButton logOut = new JButton("Logout");


    public LoggedInView(JPanel parent, CardLayout cardLayout) {
        this.parentcardlayout = cardLayout;
        this.parent = parent;

        buildSidePanel();
        addListenerToButtons();

        this.setLayout(new BorderLayout());
        contentPanel.setLayout(cardLayout);
        this.add(sidePanel, BorderLayout.WEST);
        this.add(contentPanel, BorderLayout.CENTER);

    }

    private void addListenerToButtons() {
        seePlans.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                parentcardlayout.show(contentPanel, "track");


            }
        });
        seePapers.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parentcardlayout.show(contentPanel, "paper");
            }
        });
        seeJobs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parentcardlayout.show(contentPanel, "miles");
            }
        });

        logOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parentcardlayout.show(parent, "login");
            }
        });
    }


    private void buildSidePanel() {
        this.sidePanel.setLayout(new BorderLayout());
        mainButtonPanel.setLayout(new BoxLayout(mainButtonPanel, BoxLayout.Y_AXIS));

        this.sidePanel.add(logo, BorderLayout.NORTH);
        mainButtonPanel.add(seePlans);
        mainButtonPanel.add(seePapers);
        mainButtonPanel.add(seeJobs);
        mainButtonPanel.add(logOut);

        this.sidePanel.add(mainButtonPanel, BorderLayout.CENTER);
        this.sidePanel.add(userLoggedIn, BorderLayout.SOUTH);
    }


}
