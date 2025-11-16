package view;

import javax.swing.*;
import java.awt.*;

// Create List Pop up

public class CreateListDialog extends JDialog { // JDialog is used for Pop ups
    private final JTextField nameField = new JTextField(20);
    private String listName; // result

    public CreateListDialog(Frame parent) {
        super(parent, "Create List", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Layout for the whole dialog
        JPanel content = new JPanel();
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        // Title
        JLabel title = new JLabel("Create List");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 20f));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(title);

        content.add(Box.createVerticalStrut(15));

        // Name label + field
        JPanel namePanel = new JPanel(new BorderLayout(10, 0));
        namePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setPreferredSize(new Dimension(60, 25));

        namePanel.add(nameLabel, BorderLayout.WEST);
        namePanel.add(nameField, BorderLayout.CENTER);

        content.add(namePanel);

        content.add(Box.createVerticalStrut(20));

        // Create button centered
        JButton createButton = new JButton("Create");
        createButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        createButton.addActionListener(e -> {
            String text = nameField.getText().trim();
            if (!text.isEmpty()) {
                listName = text;
                dispose();
            } else {
                // Simple feedback if empty
                JOptionPane.showMessageDialog(
                        this,
                        "Please enter a list name.",
                        "Missing Name",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        });

        content.add(createButton);

        setContentPane(content);
        pack();
        setLocationRelativeTo(parent);
    }

    public String getListName() {
        return listName;
    }
}