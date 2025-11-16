package view;

import javax.swing.*;

public class LoggedInView {
    public LoggedInView() {
        SwingUtilities.invokeLater(() -> {

            //TODO insert the username of who is logged in next to welcome.
            //** WELCOME PANEL **//
            JPanel welcomePanel = new JPanel();
            JLabel welcomeLabelText = new JLabel("Welcome NewTeam09");
            welcomePanel.add(welcomeLabelText);

            //TODO auto complete inputs with a drop down menu.
            //** Search Panel **//
            JPanel searchPanel = new JPanel();
            JTextField searchField = new JTextField(30);
            searchPanel.add(new JLabel("Search:"));
            searchPanel.add(searchField);

            //** Submit Button Panel **//
            JPanel searchButtonPanel = new JPanel();
            JButton search = new JButton("Search Location");
            searchButtonPanel.add(search);

            //** Submit Button Event **//
            search.addActionListener(e -> {
                String location = searchField.getText();

                // Call the controller.
            });

            //TODO Insert a list of saved locations.
            //** Saved location panel  **//
            JPanel locationsPanel = new JPanel();
            JLabel locationLabelText = new JLabel("Saved Locations");
            locationsPanel.add(locationLabelText);

            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.add(welcomePanel);
            mainPanel.add(searchPanel);
            mainPanel.add(searchButtonPanel);
            mainPanel.add(locationsPanel);

            JFrame frame = new JFrame("Sophisticated Weather Application");
            frame.setContentPane(mainPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
        });
    }
}
