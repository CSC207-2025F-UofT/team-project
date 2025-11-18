package view;

import data_access.SportsAPIDataAccess;
import entity.Sportbet;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SportbetFrame extends JFrame {
    public SportbetFrame(User user, JFrame mainMenu){
        setTitle("Place a Sports Bet");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1920, 1080);
        setLayout(new BorderLayout());

        // ----- LIST OF BETS -----
        JList<Sportbet> betsList = new JList<>(
                SportsAPIDataAccess.allbets.toArray(new Sportbet[0])
        );
        betsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(betsList);
        add(scrollPane, BorderLayout.CENTER);

        // ----- INPUT FOR BET AMOUNT -----
        JTextField amountField = new JTextField(10);

        JButton placeBetButton = new JButton("Place Bet");

        placeBetButton.addActionListener(e -> {
            Sportbet selected = betsList.getSelectedValue();

            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Select a bet first.");
                return;
            }

            String text = amountField.getText();

            int amount;
            try {
                amount = Integer.parseInt(text);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Enter a valid integer amount.");
                return;
            }

            JOptionPane.showMessageDialog(this,
                    "Bet placed: " + selected.toString() + " for $" + amount);
        });

        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.add(new JLabel("Amount:"));
        bottomPanel.add(amountField);
        bottomPanel.add(placeBetButton);

        add(bottomPanel, BorderLayout.SOUTH);

        // ***** THIS WAS MISSING *****
        setVisible(true);
    }
}
