package main;

import javax.swing.*;
import java.awt.*;

import panel.StatisticsPanel;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Steam Wrapped");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);

            JSplitPane verticalSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
            verticalSplit.setResizeWeight(2.0 / 3.0);
            verticalSplit.setDividerSize(5);

            JSplitPane topSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
            topSplit.setResizeWeight(0.5);
            topSplit.setDividerSize(5);


            // Displays logged in User's stats
            JPanel leftPanel = new JPanel();
            leftPanel.setBackground(Color.CYAN);

            // Displays comparison stats
            JPanel rightPanel = new JPanel();
            rightPanel.setBackground(Color.PINK);

            // Displays reviews
            JPanel bottomPanel = new JPanel();
            bottomPanel.setBackground(Color.LIGHT_GRAY);

            topSplit.setLeftComponent(leftPanel);
            topSplit.setRightComponent(rightPanel);

            verticalSplit.setTopComponent(topSplit);
            verticalSplit.setBottomComponent(bottomPanel);

            frame.add(verticalSplit);


            frame.setVisible(true);
            SwingUtilities.invokeLater(() -> {
                topSplit.setDividerLocation(0.5);
            });

        });

    }
}
