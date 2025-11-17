package com.studyarc.view;

import javax.swing.*;
import java.awt.*;

public class TrackPlansView extends JPanel {
    final BorderLayout borderLayout = new BorderLayout();

    final JPanel trackPlansPanel;
    final JPanel TitlePanel;
    final JLabel title = new JLabel("MY PLANS");

    public TrackPlansView() {
        trackPlansPanel = new JPanel();
        TitlePanel = new JPanel();
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        TitlePanel.add(title);
        trackPlansPanel.setLayout(new BoxLayout(trackPlansPanel, BoxLayout.Y_AXIS));
        for (int i = 0; i < 4; i++) {
            trackPlansPanel.add(new JLabel("Plan " + (i + 1)));
        }
        this.setLayout(borderLayout);

        this.add(TitlePanel, BorderLayout.NORTH);
        this.add(trackPlansPanel, BorderLayout.CENTER);


    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Track Plans View");
        frame.setMinimumSize(new Dimension(800, 550));
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new TrackPlansView(), BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
