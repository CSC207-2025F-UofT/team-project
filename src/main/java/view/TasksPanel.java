package view;

import javax.swing.*;
import java.awt.*;


public class TasksPanel extends JPanel {

    public TasksPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.decode("#000000"));

        Color panelDark = Color.decode("#020F28");
        Color textLight = Color.decode("#E6E6E6");

        // Title/top of page
        JLabel title = new JLabel("Task Manager", SwingConstants.CENTER);
        title.setFont(new Font("Georgia", Font.BOLD, 28));
        title.setForeground(textLight);
        title.setPreferredSize(new Dimension(0, 50));

        JPanel titleWrapper = new JPanel(new BorderLayout());
        titleWrapper.setBackground(panelDark);
        titleWrapper.add(title, BorderLayout.CENTER);
        add(titleWrapper, BorderLayout.NORTH);

        // content
        JPanel rowsContainer = new JPanel();
        rowsContainer.setLayout(new BoxLayout(rowsContainer, BoxLayout.Y_AXIS));
        rowsContainer.setOpaque(false);
        rowsContainer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Top row: 3 boxes
        JPanel topRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        topRow.setOpaque(false);
        topRow.add(createCourseBox(panelDark, textLight));
        topRow.add(createCourseBox(panelDark, textLight));
        topRow.add(createCourseBox(panelDark, textLight));
        rowsContainer.add(topRow);

        rowsContainer.add(Box.createRigidArea(new Dimension(0, 30))); // spacing

        // Bottom row: 2 boxes centered
        JPanel bottomRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        bottomRow.setOpaque(false);
        bottomRow.add(createCourseBox(panelDark, textLight));
        bottomRow.add(createCourseBox(panelDark, textLight));
        rowsContainer.add(bottomRow);

        // Scroll wrapper so content is always viewable
        JScrollPane scroll = new JScrollPane(rowsContainer);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        add(scroll, BorderLayout.CENTER);
    }

    // Course boxes
    private JPanel createCourseBox(Color panelDark, Color textLight) {
        JPanel coursePanel = new JPanel(new BorderLayout());
        coursePanel.setPreferredSize(new Dimension(320, 270)); // larger box
        coursePanel.setBackground(panelDark);
        coursePanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // Course: label and text box for user to input course name
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        top.setOpaque(false);

        JLabel nameLabel = new JLabel("Course:");
        nameLabel.setForeground(textLight);
        nameLabel.setFont(new Font("Georgia", Font.PLAIN, 16));

        JTextField courseField = new JTextField();
        courseField.setColumns(20); // longer field so text doesn't cut off
        courseField.setPreferredSize(new Dimension(230, 45)); // fixed wider field
        courseField.setBackground(Color.decode("#001F3F"));
        courseField.setForeground(textLight);
        courseField.setFont(new Font("Georgia", Font.PLAIN, 16));
        courseField.setBorder(BorderFactory.createEmptyBorder(0, 8, 12, 8));

        top.add(nameLabel);
        top.add(courseField);

        coursePanel.add(top, BorderLayout.NORTH);


        JLabel tasksPlaceholder = new JLabel(
                "<html><div style='text-align:center;'>Tasks go here...<br>(Add tasks later)</div></html>",
                SwingConstants.CENTER
        );
        tasksPlaceholder.setForeground(textLight);
        tasksPlaceholder.setFont(new Font("Georgia", Font.ITALIC, 14));

        coursePanel.add(tasksPlaceholder, BorderLayout.CENTER);

        // Add Task button (at the bottom of each course box)
        JButton addTaskBtn = new JButton("Add Task");
        addTaskBtn.setFocusPainted(false);
        addTaskBtn.setBackground(panelDark);
        addTaskBtn.setForeground(textLight);
        addTaskBtn.setFont(new Font("Georgia", Font.PLAIN, 16));
        addTaskBtn.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        addTaskBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Hover effect
        addTaskBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                addTaskBtn.setBackground(Color.decode("#0047A3")); // accentHover
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                addTaskBtn.setBackground(panelDark);
            }
        });

        coursePanel.add(addTaskBtn, BorderLayout.SOUTH);

        return coursePanel;
    }
}