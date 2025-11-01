package main.view;

import main.utility.FontLoader;

import javax.swing.*;
import java.awt.*;

public class MainView {

    public MainView(){
        FontLoader.registerFonts();
        JFrame frame = new JFrame("Main Screen");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.setResizable(false); // Fixed size window
        frame.setLayout(new BorderLayout(10, 10));

        // ---------- TitleImage ----------
        ImageIcon originalTitleImage = new ImageIcon("images/TitleImage.png");
        int titleImageWidth = originalTitleImage.getIconWidth();
        int titleImageHeight = originalTitleImage.getIconHeight();
        Image titleImage = originalTitleImage.getImage()
                .getScaledInstance((int)(titleImageWidth*0.9), (int)(titleImageHeight*0.9), Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(titleImage);

        JLabel imageLabel = new JLabel(scaledIcon);
        imageLabel.setHorizontalAlignment(JLabel.CENTER);

        JPanel imagePanel = new JPanel(new GridLayout());
        imagePanel.add(imageLabel);

        frame.add(imagePanel, BorderLayout.NORTH);

        // ---------- 2x2 Buttons ----------
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBorder(null);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(30, 30, 30, 30);
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1.0;
        c.weighty = 1.0;
        buttonPanel.add(new JPanel(),c); // Empty panel for spacing

        JButton[][] buttons = { // 2D array of buttons
                {new JButton("Single Player"), new JButton("Multiplayer")},
                {new JButton("Manage Study Set"), new JButton("Leaderboard")}
        };

        for (int row = 0; row < buttons.length; row++) {
            for (int col = 0; col < buttons[row].length; col++) {
                JButton button = buttons[row][col];
                button.setFont(new Font("Helvetica", Font.BOLD, 48));
                button.setBackground(Color.GRAY);
                button.setForeground(Color.WHITE);
                button.addActionListener(e -> {
                    System.out.println(button.getText() + " button clicked");
                });
                c.gridx = col;
                c.gridy = row + 1;
                button.setPreferredSize(new Dimension(400, 150));
                buttonPanel.add(button, c);
            }
        }
        frame.add(buttonPanel, BorderLayout.CENTER);

        // ---------- Finish ----------
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
