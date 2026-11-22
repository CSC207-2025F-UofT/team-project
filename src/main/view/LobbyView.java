package main.view;

import main.interface_adapters.lobby.LobbyController;
import main.interface_adapters.lobby.LobbyViewModel;

import javax.swing.*;
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
    private final JButton minusPlayerButton;
    private final JButton plusPlayerButton;
    private final JLabel youLabel;
    private final List<JLabel> playerIcons = new ArrayList<>();

    public LobbyView(LobbyViewModel lobbyViewModel) {
        this.lobbyViewModel = lobbyViewModel;
        this.setLayout(null);
        this.setBackground(new Color(5,5,5));
        this.lobbyViewModel.addPropertyChangeListener(this);

        returnButton = new JButton("<- Return");
        ViewUtils.setFont(returnButton, "Figtree Black", Font.BOLD, 28, 0.04);
        ViewUtils.setRelativeBounds(returnButton, 0.02, 0.88, 0.2, 0.1);
        returnButton.setFocusPainted(false);
        returnButton.setForeground(Color.white);
        returnButton.setBackground(new Color(196, 43, 64));
        returnButton.addActionListener(this);
        returnButton.setBorder(BorderFactory.createLineBorder(Color.white, 2));

        JLabel user = ViewUtils.createImage("/Player.png", 0, 0.3, 0.1);
        playerIcons.add(user);
        this.add(user);

        for (int i = 1; i <= 5; i++) {
            JLabel aiIcon = ViewUtils.createImage("/AI_User.png", 0, 0.3, 0.10);
            playerIcons.add(aiIcon);
            this.add(aiIcon);
        }

        minusPlayerButton = new JButton("-");
        ViewUtils.setFont(minusPlayerButton, "Figtree Black", Font.PLAIN, 28, 0.075);
        ViewUtils.setRelativeSquareBounds(minusPlayerButton, 0.1, 0.75, 0.075);
        minusPlayerButton.setBackground(new Color(222, 64, 96));
        minusPlayerButton.setForeground(Color.white);
        minusPlayerButton.setFocusPainted(false);
        minusPlayerButton.addActionListener(this);

        plusPlayerButton = new JButton("+");
        ViewUtils.setFont(plusPlayerButton, "Figtree Black", Font.PLAIN, 28, 0.03);
        ViewUtils.setRelativeSquareBounds(plusPlayerButton, 0.16, 0.75, 0.075);
        plusPlayerButton.setBackground(new Color(49, 163, 83));
        plusPlayerButton.setForeground(Color.white);
        plusPlayerButton.setFocusPainted(false);
        plusPlayerButton.addActionListener(this);

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
        this.add(returnButton);
        this.add(minusPlayerButton);
        this.add(plusPlayerButton);

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
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        updatePlayerIcons(lobbyViewModel.getPlayers());
    }
}
