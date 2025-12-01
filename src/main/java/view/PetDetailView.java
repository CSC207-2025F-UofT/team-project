package view;
/*
Pet detail that user can see
*/
import entity.Pet;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PetDetailView extends JFrame {
    public PetDetailView(Pet pet) {
        super("Pet Detail View");

        //the main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        setContentPane(mainPanel);
        //title
        JLabel titleLabel = new JLabel("Pet Details");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD));
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(10));
        //area for picture of the pet
        JLabel pictureLabel = new JLabel("Picture", SwingConstants.CENTER);
        pictureLabel.setPreferredSize(new Dimension(220,220));
        pictureLabel.setMinimumSize(new Dimension(220,220));
        pictureLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        //ge tthe image from the URL
        String imageURL = pet.getImageURL();
        if (imageURL != null && !imageURL.equals("")) {
            try {
                ImageIcon icon = new ImageIcon(new java.net.URL(imageURL));
                Image scaled = icon.getImage().getScaledInstance(220, 220, Image.SCALE_SMOOTH);
                pictureLabel.setIcon(new ImageIcon(scaled));
                pictureLabel.setText("");
            } catch (Exception e) {
                //just keep the txt there if the image doesn't work
            }
        }
        JPanel picturePanel = new JPanel();
        picturePanel.add(pictureLabel);
        mainPanel.add(picturePanel);
        mainPanel.add(Box.createVerticalStrut(10));
        //detail columns being created (we need two vertical JPanels.
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.X_AXIS));
        mainPanel.add(detailsPanel);
        //the left column containing some info about the pet. (goes into details panel)
        JPanel leftColumn = new JPanel();
        leftColumn.setLayout(new BoxLayout(leftColumn, BoxLayout.Y_AXIS));
        detailsPanel.add(leftColumn);
        //adding some pet info into the left column!
        leftColumn.add(makeRow("Name", pet.getName()));
        leftColumn.add(makeRow("age", pet.getAge().name()));
        leftColumn.add(makeRow("Size", pet.getSize().name()));
        leftColumn.add(makeRow("Region", "N/A"));
        detailsPanel.add(Box.createHorizontalStrut(20));
        //make the right column of information
        JPanel rightColumn = new JPanel();
        rightColumn.setLayout(new BoxLayout(rightColumn, BoxLayout.Y_AXIS));
        detailsPanel.add(rightColumn);
        //add information rows
        rightColumn.add(makeRow("ID", String.valueOf(pet.getId())));
        rightColumn.add(makeRow("Gender", pet.getGender().name()));
        rightColumn.add(makeRow("Color", pet.getColor()));
        rightColumn.add((makeRow("Weight", "N/A")));
        mainPanel.add(Box.createVerticalStrut(10));
        //make description area
        JTextArea descriptionArea = new JTextArea(pet.getDescription());
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setEditable(false);
        descriptionArea.setBorder(BorderFactory.createTitledBorder("Description"));
        //give scrolling abilities
        JScrollPane descriptionScrollPane = new JScrollPane(descriptionArea);
        descriptionScrollPane.setPreferredSize(new Dimension(450,120));
        mainPanel.add(descriptionScrollPane);
        mainPanel.add(Box.createVerticalStrut(10));
        //adding the bottom buttons in the GUI drawing
        JPanel buttonPanel = new JPanel();
        JButton adoptButton = new JButton("Apply to Adopt");
        //add the action listener
        adoptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(
                        PetDetailView.this,
                        "Apply to Adopt",
                        "Adopt", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        buttonPanel.add(adoptButton);
        mainPanel.add(buttonPanel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    //helper for making the rows of information
    private JPanel makeRow(String labelText, String valueText) {
        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
        JLabel label = new JLabel(labelText + ": ");
        JLabel value = new JLabel(valueText != null && !valueText.equals("") ? valueText : "N/A");
        row.add(label);
        row.add(Box.createHorizontalStrut(5));
        row.add(value);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        return row;
    }
}
