package view;

import javax.swing.*;
import java.awt.*;

public class CreateListDialog extends JDialog {

    private final JTextField nameField = new JTextField(20);
    private String listName;

    public CreateListDialog(Frame parent) {
        super(parent, "Create List", true);

        // Main panel
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Title
        JLabel titleLabel = new JLabel("Create List");
        panel.add(titleLabel);

        // Name label + field
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        namePanel.add(new JLabel("Name:"));
        namePanel.add(nameField);

        panel.add(namePanel);

        // Create button
        JButton createButton = new JButton("Create");
        createButton.addActionListener(e -> {
            listName = nameField.getText();
            dispose();
        });
        panel.add(createButton);

        // Attach panel to dialog
        setContentPane(panel); // Places the UI inside the window
        pack(); // Automatically resize
    }

    public String getListName() {
        return listName;
    }
}