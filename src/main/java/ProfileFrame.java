import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ProfileFrame extends JFrame {
    public ProfileFrame(User user, JFrame mainMenu) {
        setTitle("Profile");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(350, 250);
        setLayout(new GridLayout(5, 1));

        JLabel usernameLabel = new JLabel("Username: " + user.getUsername());
        JLabel balanceLabel = new JLabel("Balance: $" + user.getBalance());
        JLabel betLabel = new JLabel("Number of Bets: " + user.getBets());
        JLabel gamesLabel = new JLabel("Games Played: " + user.getGamesPlayed());
        JButton returnBtn = new JButton("Return");

        add(usernameLabel);
        add(balanceLabel);
        add(betLabel);
        add(gamesLabel);
        add(returnBtn);

        returnBtn.addActionListener(e -> {
            mainMenu.setVisible(true);
            dispose();
        });

        setVisible(true);
    }
}
