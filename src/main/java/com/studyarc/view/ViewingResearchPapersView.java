package com.studyarc.view;

import javax.swing.*;
import java.awt.*;

public class ViewingResearchPapersView extends JPanel{
    private JLabel titleLabel;
    private JTable papersTable;

    public ViewingResearchPapersView() {
        this.setLayout(new BorderLayout());

        // Title Section at the top
        JPanel topPanel = new JPanel();
        titleLabel = new JLabel("Research Papers");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        topPanel.add(titleLabel);

        this.add(topPanel, BorderLayout.NORTH);

        // Research Paper list section
        String[] columns = {"Data1", "Data2", "......", "Data n"};
        Object[][] data = {};
        papersTable = new JTable(data, columns);
        JScrollPane scrollPane = new JScrollPane(papersTable);
        this.add(scrollPane, BorderLayout.CENTER);
    }
}
