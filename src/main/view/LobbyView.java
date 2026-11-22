package main.view;

import main.interface_adapters.lobby.LobbyController;
import main.interface_adapters.lobby.LobbyViewModel;

import javax.swing.*;
import javax.swing.text.View;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class LobbyView extends JPanel implements ActionListener, PropertyChangeListener {

    private final LobbyViewModel lobbyViewModel;
    private LobbyController lobbyController = null;

    private final JButton returnButton;
    private final JButton playButton;
    private final JButton minusPlayerButton;
    private final JButton plusPlayerButton;
    private final JButton minusBetButton;
    private final JButton plusBetButton;
    private final JLabel youLabel;
    private final JLabel minimumBetLabel;
    private final List<JLabel> playerIcons = new ArrayList<>();

    public LobbyView(LobbyViewModel lobbyViewModel) {
        this.lobbyViewModel = lobbyViewModel;
        this.setLayout(null);
        this.setBackground(new Color(5,5,5));
        this.lobbyViewModel.addPropertyChangeListener(this);

        returnButton = new JButton("<- Return");
        ViewUtils.setFont(returnButton, "Figtree Black", Font.BOLD, 28, 0.04);
        ViewUtils.setRelativeBounds(returnButton, 0.02, 0.85, 0.2, 0.1);
        returnButton.setFocusPainted(false);
        returnButton.setForeground(Color.white);
        returnButton.setBackground(new Color(196, 43, 64));
        returnButton.addActionListener(this);
        returnButton.setBorder(BorderFactory.createLineBorder(Color.white, 2));

        playButton = new JButton("Play ->");
        ViewUtils.setFont(playButton, "Figtree Black", Font.BOLD, 28, 0.04);
        ViewUtils.setRelativeBounds(playButton, 0.78, 0.85, 0.2, 0.1);
        playButton.setFocusPainted(false);
        playButton.setForeground(Color.white);
        playButton.setBackground(new Color(9, 150, 14));
        playButton.addActionListener(this);
        playButton.setBorder(BorderFactory.createLineBorder(Color.white, 2));

        JLabel user = ViewUtils.createImage("/Player.png", 0, 0.3, 0.1);
        playerIcons.add(user);
        this.add(user);

        for (int i = 1; i <= 5; i++) {
            JLabel aiIcon = ViewUtils.createImage("/AI_User.png", 0, 0.3, 0.10);
            playerIcons.add(aiIcon);
            this.add(aiIcon);
        }

        JPanel optionsPanel = new JPanel();
        ViewUtils.setRelativeBounds(optionsPanel, 0.1, 0.6, 0.8, 0.2);
        optionsPanel.setBackground(new Color(2, 2, 2));

        JPanel playerPanel = new JPanel();
        ViewUtils.setRelativeBounds(playerPanel, 0.16, 0.28, 0.685, 0.26);
        playerPanel.setBackground(new Color(6, 6, 11));


        JLabel title = new JLabel("Choose Game Settings", SwingConstants.CENTER);
        ViewUtils.setFont(title, "Figtree Black", Font.PLAIN, 28, 0.1);
        ViewUtils.setRelativeBounds(title, 0, 0.07, 1, 0.13);
        title.setForeground(Color.white);

        minimumBetLabel = new JLabel("Minimum Bet: $" + Integer.toString(lobbyViewModel.getMinBet()));
        ViewUtils.setFont(minimumBetLabel, "Figtree ExtraBold", Font.PLAIN, 28, 0.04);
        ViewUtils.setRelativeBounds(minimumBetLabel, 0.425, 0.15, 0.8, 0.1);
        minimumBetLabel.setForeground(new Color(171, 221, 255));

        minusPlayerButton = new JButton("-");
        ViewUtils.setFont(minusPlayerButton, "Figtree Black", Font.PLAIN, 28, 0.075);
        ViewUtils.setRelativeSquareBounds(minusPlayerButton, 0.301, 0.70, 0.075);
        minusPlayerButton.setBackground(new Color(222, 64, 96));
        minusPlayerButton.setForeground(Color.white);
        minusPlayerButton.setFocusPainted(false);
        minusPlayerButton.addActionListener(this);

        plusPlayerButton = new JButton("+");
        ViewUtils.setFont(plusPlayerButton, "Figtree Black", Font.PLAIN, 28, 0.06);
        ViewUtils.setRelativeSquareBounds(plusPlayerButton, 0.356, 0.70, 0.075);
        plusPlayerButton.setBackground(new Color(49, 163, 83));
        plusPlayerButton.setForeground(Color.white);
        plusPlayerButton.setFocusPainted(false);
        plusPlayerButton.addActionListener(this);

        minusBetButton = new JButton("-");
        ViewUtils.setFont(minusBetButton, "Figtree Black", Font.PLAIN, 28, 0.075);
        ViewUtils.setRelativeSquareBounds(minusBetButton, 0.601, 0.70, 0.075);
        minusBetButton.setBackground(new Color(222, 64, 96));
        minusBetButton.setForeground(Color.white);
        minusBetButton.setFocusPainted(false);
        minusBetButton.addActionListener(this);

        plusBetButton = new JButton("+");
        ViewUtils.setFont(plusBetButton, "Figtree Black", Font.PLAIN, 28, 0.06);
        ViewUtils.setRelativeSquareBounds(plusBetButton, 0.656, 0.70, 0.075);
        plusBetButton.setBackground(new Color(49, 163, 83));
        plusBetButton.setForeground(Color.white);
        plusBetButton.setFocusPainted(false);
        plusBetButton.addActionListener(this);

        JLabel editPlayerLabel = new JLabel("Choose # of Players");
        ViewUtils.setFont(editPlayerLabel, "Figtree SemiBold", Font.PLAIN, 28, 0.03);
        ViewUtils.setRelativeBounds(editPlayerLabel, 0.27, 0.62, 0.3, 0.05);
        editPlayerLabel.setForeground(Color.white);

        JLabel editMinBetLabel = new JLabel("Choose Minimum Bet");
        ViewUtils.setFont(editMinBetLabel, "Figtree SemiBold", Font.PLAIN, 28, 0.03);
        ViewUtils.setRelativeBounds(editMinBetLabel, 0.57, 0.62, 0.3, 0.05);
        editMinBetLabel.setForeground(Color.white);

        youLabel = new JLabel("YOU");
        ViewUtils.setFont(youLabel, "Figtree ExtraBold", Font.PLAIN, 28, 0.05);
        youLabel.setBounds(
                (int) (playerIcons.get(0).getX() + 0.015 * ViewUtils.getScreenWidth()),
                (int) (0.48 * ViewUtils.getScreenHeight()),
                (int) (playerIcons.get(0).getWidth() + 0.02 * ViewUtils.getScreenWidth()),
                (int) (0.05 * ViewUtils.getScreenHeight())
                );
        youLabel.setForeground(Color.white);

        this.add(youLabel);
        this.add(title);
        this.add(minimumBetLabel);
        this.add(returnButton);
        this.add(playButton);
        this.add(minusPlayerButton);
        this.add(plusPlayerButton);
        this.add(minusBetButton);
        this.add(plusBetButton);
        this.add(editPlayerLabel);
        this.add(editMinBetLabel);
        this.add(optionsPanel);
        this.add(playerPanel);

        updatePlayerIcons(lobbyViewModel.getPlayers());
    }

    private void updatePlayerIcons(int numPlayers) {
        double iconWidthPct = 0.1;
        double gapPct = 0.01;
        double totalWidthPct = (numPlayers * iconWidthPct) + ((numPlayers - 1) * gapPct);
        double startXPct = (1.0 - totalWidthPct) / 2.0;

        for (int i = 0; i < playerIcons.size(); i++) {
            JLabel icon = playerIcons.get(i);
            if (i < numPlayers) {
                double xPct = startXPct + i * (iconWidthPct + gapPct);
                int x = (int) Math.round(xPct * ViewUtils.getScreenWidth());
                icon.setLocation(x, icon.getY());
                icon.setVisible(true);
            } else {
                icon.setVisible(false);
            }
        }
        if (youLabel != null) {
            youLabel.setLocation((int) (playerIcons.get(0).getX() + 0.02 * ViewUtils.getScreenWidth()), youLabel.getY());
        }
    }

    private void updateMinimumBetLabel(int minBet) {
        minimumBetLabel.setText("Minimum Bet: $" + Integer.toString(minBet));
    }
    public void setController(LobbyController lobbyController) {
        this.lobbyController = lobbyController;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource().equals(returnButton)) {
            lobbyController.switchToMainMenuView();
        }
        if (event.getSource().equals(minusPlayerButton)) {
            lobbyController.removePlayer();
        }
        if (event.getSource().equals((plusPlayerButton))) {
            lobbyController.addPlayer();
        }
        if (event.getSource().equals(minusBetButton)) {
            lobbyController.decreaseMinBet();
        }
        if (event.getSource().equals(plusBetButton)) {
            lobbyController.increaseMinBet();
        }
        if (event.getSource().equals(playButton)) {
            lobbyController.switchToGameView();
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        updatePlayerIcons(lobbyViewModel.getPlayers());
        updateMinimumBetLabel(lobbyViewModel.getMinBet());
    }
}
