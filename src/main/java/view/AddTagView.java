package view;

import javax.swing.*;
import java.awt.*;


public class AddTagView {
    private static final String FONT_ARIAL = "Arial";
    private static void showSuccessfulTagPage() {
        JFrame popout =  new JFrame("Successfully added tag");
        popout.setSize(300,120);
        popout.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(240, 235, 255));
        panel.setLayout(new BorderLayout());
        JLabel successMessage = new JLabel("Tag added Successfully!", SwingConstants.CENTER);
        successMessage.setFont(new Font(FONT_ARIAL, Font.BOLD, 16));
        successMessage.setForeground(new Color(75, 0, 120));
        panel.add(successMessage, BorderLayout.CENTER);
        popout.setContentPane(panel);
        popout.setVisible(true);
        javax.swing.Timer timer = new javax.swing.Timer(1500, e -> popout.dispose());
        timer.start();
    }
    //Adding tag window opens when user clicks add tag on recipe page
    private static void showAddTagPage() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Add Tag");
            frame.setMinimumSize(new Dimension(480, 360));
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            JPanel panel = new JPanel(new GridBagLayout());
            panel.setBackground(new Color(240, 235, 255));
            GridBagConstraints c = new GridBagConstraints();
            JLabel title = new JLabel("Add Your Tag");
            title.setFont(new Font(FONT_ARIAL, Font.BOLD, 28));
            title.setForeground(new Color(75, 0, 120));
            c.gridx = 0;
            c.gridy = 1;
            c.gridwidth = 2;
            c.insets = new Insets(20, 20, 20, 20);
            panel.add(title, c);

            //Tag name TextField
            JTextField tagNameField = new JTextField(15);
            tagNameField.setFont(new Font(FONT_ARIAL, Font.BOLD, 14));
            tagNameField.setBackground(Color.WHITE);
            tagNameField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.BLACK),
                    BorderFactory.createEmptyBorder(5,5,5,5)
            ));
            c.gridx = 1;
            c.gridy = 2;
            c.anchor = GridBagConstraints.WEST;
            panel.add(tagNameField, c);

            JLabel errorMessage = new JLabel(" ");
            errorMessage.setForeground(Color.RED);
            errorMessage.setFont(new Font(FONT_ARIAL, Font.BOLD, 14));
            c.gridx = 0;
            c.gridy = 4;
            c.gridwidth = 2;
            c.insets = new Insets(10, 0, 0, 0);
            c.anchor = GridBagConstraints.CENTER;
            panel.add(errorMessage, c);

            //"Add Tag" Button
            JButton addTagButton = new JButton("Add Tag");
            addTagButton.setFont(new Font(FONT_ARIAL, Font.BOLD, 14));
            addTagButton.setBackground(new Color(138, 43, 226));
            addTagButton.setForeground(Color.WHITE);
            addTagButton.setFocusPainted(false);
            addTagButton.setBorderPainted(false);
            addTagButton.setOpaque(true);
            addTagButton.setPreferredSize(new Dimension(120, 40));

            addTagButton.addActionListener(e -> {
                String newTag = tagNameField.getText().trim();
                errorMessage.setText("");

                for (char c1 : newTag.toCharArray()) {
                    if (Character.isDigit(c1) || !Character.isLetterOrDigit(c1)) { //Checking for numbers and symbols
                        errorMessage.setText("Tag should only contain letters");
                        tagNameField.setText(""); //Resetting the TextField
                        return;
                    }
                }

                if (newTag.isEmpty()) {
                    errorMessage.setText("Please enter a tag name");
                    return;
                }

                if (newTag.length() > 20) {
                    errorMessage.setText("Tag name too long");
                    tagNameField.setText("");
                    return;
                }

                //TODO: check to see if added tag is a duplicate
                frame.dispose();
                showSuccessfulTagPage();

            });
            //User can press "ENTER" instead of clicking on the button
            tagNameField.addActionListener(e -> addTagButton.doClick());
            c.gridx = 0;
            c.gridy = 3;
            c.gridwidth = 2;
            c.anchor = GridBagConstraints.CENTER;
            c.insets = new Insets(20, 20, 10, 20);
            panel.add(addTagButton, c);

            frame.add(panel);
            frame.pack();
            frame.setVisible(true);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            //Temporary recipe page to be changed when recipe page is completed.
            JFrame frame = new JFrame("Temperary Recipe Page");
            frame.setMinimumSize(new java.awt.Dimension(720, 480));
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null); // Center the window

            // Create main panel
            JPanel mainPanel = new JPanel(new GridBagLayout());
            mainPanel.setBackground(new Color(240, 235, 255)); // Light purple background
            GridBagConstraints gbc = new GridBagConstraints();


            // New tag button
            JButton createTagButton = new JButton("New Tag");
            createTagButton.setFont(new Font(FONT_ARIAL, Font.BOLD, 14));
            createTagButton.setBackground(new Color(138, 43, 226)); // Blue violet purple
            createTagButton.setForeground(Color.WHITE);
            createTagButton.setFocusPainted(false);
            createTagButton.setBorderPainted(false);
            createTagButton.setOpaque(true); // Ensure background color shows
            createTagButton.setPreferredSize(new Dimension(120, 40));

            // opening add tag page
            createTagButton.addActionListener(e -> showAddTagPage());
            mainPanel.add(createTagButton, gbc);
            frame.add(mainPanel);
            frame.pack();
            frame.setVisible(true);
        });
    }
}