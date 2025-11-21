package view;

import entity.Sportbet;
import entity.User;

import javax.swing.*;
import java.awt.*;

public class BetHistoryFrame extends JFrame {
    public BetHistoryFrame(User user, JFrame MainMenu){
        setTitle("Place a Sports Bet");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1920, 1080);
        setLayout(new BorderLayout());
        JTextArea area = new JTextArea();
        area.setEditable(false);
        JButton backButton = new JButton("Go Back");
        // ---- JLIST INSTEAD OF TEXT AREA ----
        DefaultListModel<Sportbet> model = new DefaultListModel<>();
        for (Sportbet b : user.getSbs()) {
            model.addElement(b);
        }

        JList<Sportbet> betList = new JList<>(model);
        betList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        betList.setVisibleRowCount(-1);

        add(new JScrollPane(betList), BorderLayout.CENTER);

        backButton.addActionListener(e -> {
            new MainMenuFrame(user);
            dispose();
        });


        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.add(backButton);

        add(bottomPanel, BorderLayout.SOUTH);
        setVisible(true);
    }
}
