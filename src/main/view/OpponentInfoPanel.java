package main.view;

import main.interface_adapters.game.GameViewModel;

import javax.swing.*;
import javax.swing.text.View;
import java.awt.*;

public class OpponentInfoPanel extends JPanel {

    private final int playerID;
    private final String playerIdentifer;
    private final JLabel nameLabel;
    private JLabel chipsLabel;
    private JLabel card1Label;
    private JLabel card2Label;
    private final double x;

    private final GameViewModel gameViewModel;

    public OpponentInfoPanel(GameViewModel gameViewModel, int playerID, double xPct) {
        this.gameViewModel = gameViewModel;
        this.playerID = playerID - 1;
        this.playerIdentifer = "player" + Integer.toString(this.playerID);
        this.x = xPct;

        this.setLayout(null);
        this.setBackground(new Color(10, 10, 10));
        this.setBorder(BorderFactory.createLineBorder(gameViewModel.getPlayerColors(playerID - 1), 6));
        Color nameColor = gameViewModel.getPlayerColors(playerID - 1);
        ViewUtils.setRelativeBounds(this, x, 0.1, 0.15, 0.13);

        nameLabel = new JLabel(gameViewModel.players.get(playerIdentifer).get("Name"));
        nameLabel.setForeground(new Color(nameColor.getRed(), nameColor.getGreen(), nameColor.getBlue(), 200));
        ViewUtils.setFont(nameLabel, "Figtree ExtraBold", Font.PLAIN, 28, 0.03);
        ViewUtils.setRelativeBounds(nameLabel, 0.01, 0.01, 0.15, 0.05);

        chipsLabel = new JLabel("$" + gameViewModel.players.get(playerIdentifer).get("Chips"));
        chipsLabel.setForeground(new Color(nameColor.getRed(), nameColor.getGreen(), nameColor.getBlue(), 150));
        ViewUtils.setFont(chipsLabel, "Figtree SemiBold", Font.PLAIN, 16, 0.02);
        ViewUtils.setRelativeBounds(chipsLabel, 0.01, 0.037, 0.15, 0.04);

        updateCards("/cardHolders/blank_card.png", "/cardHolders/blank_card.png");

        this.add(nameLabel);
        this.add(chipsLabel);
    }

    public void updateChips(String newAmount) {
        chipsLabel.setText(newAmount);
    }

    public void updateCards(String card1Path, String card2Path) {
        card1Label = ViewUtils.createImage(card1Path, 0.1, 0.07, 0.025);
        card2Label = ViewUtils.createImage(card2Path, 0.12, 0.07, 0.025);

        this.add(card1Label);
        this.add(card2Label);
        this.revalidate();
        this.repaint();
    }
}
