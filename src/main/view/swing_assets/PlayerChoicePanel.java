package main.view.swing_assets;

import main.interface_adapters.game.GameController;
import main.view.ViewUtils;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerChoicePanel extends JPanel {

    private final JButton checkButton;
    private final JButton callButton;
    private final JButton raiseButton;
    private final JButton reraiseButton;
    private final JButton foldButton;

    // Chip selector UI components
    private static final int[] DENOMINATIONS = {1, 5, 25, 50, 100};
    private static final String[] CHIP_NAMES = {"White", "Red", "Green", "Blue", "Black"};
    private static final Color[] CHIP_COLORS = {
            new Color(235, 235, 235),
            new Color(220, 75, 75),
            new Color(75, 190, 105),
            new Color(75, 145, 225),
            new Color(160, 160, 175)
    };

    private final JLabel[] chipHeaderLabels = new JLabel[5];
    private final JButton[] plusButtons = new JButton[5];
    private final JLabel[] countLabels = new JLabel[5];
    private final JButton[] minusButtons = new JButton[5];

    private final JLabel totalBetLabel;
    private final JButton confirmRaiseButton;
    private final JButton cancelRaiseButton;

    // Tracks owned chips, chip dollar values, and currently selected bet counts
    private final int[] ownedChips = new int[5];
    private final int[] selectedChips = new int[5];
    private final int[] chipDollarValues = {1, 5, 25, 50, 100};
    private int minRequiredBet = 0;
    private boolean isReraiseMode = false;

    private GameController gameController;

    public PlayerChoicePanel() {
        this.setLayout(null);
        this.setBackground(new Color(10, 10, 15));
        this.setBorder(BorderFactory.createLineBorder(new Color(5, 5, 7)));
        ViewUtils.setRelativeBounds(this, 0.3, 0.77, 0.68, 0.21);

        checkButton = createActionButton("Check", new Color(235, 140, 52), 0.02);
        callButton = createActionButton("Call", new Color(52, 168, 235), 0.02);
        raiseButton = createActionButton("Raise", new Color(76, 175, 80), 0.24);
        reraiseButton = createActionButton("Re-raise", new Color(76, 175, 80), 0.24);
        foldButton = createActionButton("Fold", new Color(211, 47, 47), 0.46);

        // Build 5 chip columns (White, Red, Green, Blue, Black)
        for (int i = 0; i < 5; i++) {
            final int idx = i;
            double colX = 0.02 + (i * 0.088);

            chipHeaderLabels[i] = new JLabel(CHIP_NAMES[i], SwingConstants.CENTER);
            chipHeaderLabels[i].setForeground(CHIP_COLORS[i]);
            ViewUtils.setFont(chipHeaderLabels[i], "Figtree SemiBold", Font.PLAIN, 14, 0.022);
            ViewUtils.setRelativeBounds(chipHeaderLabels[i], colX, 0.01, 0.08, 0.03);

            plusButtons[i] = createMiniButton("+", new Color(55, 130, 65), colX, 0.045);
            plusButtons[i].addActionListener(e -> {
                if (selectedChips[idx] < ownedChips[idx]) {
                    selectedChips[idx]++;
                    refreshChipSelectorUI();
                }
            });

            countLabels[i] = new JLabel("0", SwingConstants.CENTER);
            countLabels[i].setForeground(Color.WHITE);
            ViewUtils.setFont(countLabels[i], "Figtree Black", Font.PLAIN, 16, 0.028);
            ViewUtils.setRelativeBounds(countLabels[i], colX, 0.09, 0.08, 0.035);

            minusButtons[i] = createMiniButton("-", new Color(165, 55, 55), colX, 0.13);
            minusButtons[i].addActionListener(e -> {
                if (selectedChips[idx] > 0) {
                    selectedChips[idx]--;
                    refreshChipSelectorUI();
                }
            });

            this.add(chipHeaderLabels[i]);
            this.add(plusButtons[i]);
            this.add(countLabels[i]);
            this.add(minusButtons[i]);
        }

        totalBetLabel = new JLabel("Bet: $0 (Min: $0)", SwingConstants.CENTER);
        totalBetLabel.setForeground(new Color(171, 221, 255));
        ViewUtils.setFont(totalBetLabel, "Figtree SemiBold", Font.PLAIN, 16, 0.028);
        ViewUtils.setRelativeBounds(totalBetLabel, 0.47, 0.015, 0.19, 0.04);

        confirmRaiseButton = new JButton("Confirm");
        ViewUtils.setFont(confirmRaiseButton, "Figtree Black", Font.PLAIN, 20, 0.03);
        ViewUtils.setRelativeBounds(confirmRaiseButton, 0.47, 0.06, 0.19, 0.055);
        confirmRaiseButton.setForeground(Color.WHITE);
        confirmRaiseButton.setBackground(new Color(76, 175, 80));
        confirmRaiseButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        confirmRaiseButton.setFocusPainted(false);

        cancelRaiseButton = new JButton("Cancel");
        ViewUtils.setFont(cancelRaiseButton, "Figtree SemiBold", Font.PLAIN, 16, 0.025);
        ViewUtils.setRelativeBounds(cancelRaiseButton, 0.47, 0.125, 0.19, 0.045);
        cancelRaiseButton.setForeground(Color.WHITE);
        cancelRaiseButton.setBackground(new Color(70, 70, 80));
        cancelRaiseButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        cancelRaiseButton.setFocusPainted(false);

        // Action Listeners
        checkButton.addActionListener(e -> {
            if (gameController != null) gameController.playerCheck();
        });
        callButton.addActionListener(e -> {
            if (gameController != null) gameController.playerCall();
        });
        raiseButton.addActionListener(e -> {
            isReraiseMode = false;
            showChipSelector();
        });
        reraiseButton.addActionListener(e -> {
            isReraiseMode = true;
            showChipSelector();
        });
        cancelRaiseButton.addActionListener(e -> {
            setTurnState(isReraiseMode ? "facingBet" : "unopened");
        });
        confirmRaiseButton.addActionListener(e -> {
            Map<Integer, Integer> betChips = new HashMap<>();
            int totalAmount = 0;
            for (int i = 0; i < 5; i++) {
                betChips.put(DENOMINATIONS[i], selectedChips[i]);
                totalAmount += selectedChips[i] * chipDollarValues[i];
            }
            if (gameController != null && totalAmount >= minRequiredBet) {
                if (isReraiseMode) {
                    gameController.playerReraise(betChips, totalAmount);
                } else {
                    gameController.playerRaise(betChips, totalAmount);
                }
            }
        });
        foldButton.addActionListener(e -> {
            if (gameController != null) gameController.playerFold();
        });

        this.add(checkButton);
        this.add(callButton);
        this.add(raiseButton);
        this.add(reraiseButton);
        this.add(foldButton);
        this.add(totalBetLabel);
        this.add(confirmRaiseButton);
        this.add(cancelRaiseButton);

        setTurnState("none");
    }

    private JButton createActionButton(String text, Color bg, double xPct) {
        JButton btn = new JButton(text);
        ViewUtils.setFont(btn, "Figtree Black", Font.PLAIN, 28, 0.04);
        ViewUtils.setRelativeBounds(btn, xPct, 0.055, 0.2, 0.1);
        btn.setForeground(Color.WHITE);
        btn.setBackground(bg);
        btn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        btn.setFocusPainted(false);
        return btn;
    }

    private JButton createMiniButton(String text, Color bg, double xPct, double yPct) {
        JButton btn = new JButton(text);
        ViewUtils.setFont(btn, "Figtree Black", Font.PLAIN, 16, 0.025);
        ViewUtils.setRelativeBounds(btn, xPct, yPct, 0.08, 0.038);
        btn.setForeground(Color.WHITE);
        btn.setBackground(bg);
        btn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        btn.setFocusPainted(false);
        return btn;
    }

    /**
     * Syncs owned chip counts, chip values, and minimum bet before opening the selector.
     */
    public void configureChips(Map<Integer, Integer> currentChips, List<Integer> values, int minBet) {
        this.minRequiredBet = minBet;
        for (int i = 0; i < 5; i++) {
            ownedChips[i] = currentChips.getOrDefault(DENOMINATIONS[i], 0);
            if (values != null && i < values.size()) {
                chipDollarValues[i] = values.get(i);
            }
            selectedChips[i] = 0;
        }
        refreshChipSelectorUI();
    }

    private void showChipSelector() {
        checkButton.setVisible(false);
        callButton.setVisible(false);
        raiseButton.setVisible(false);
        reraiseButton.setVisible(false);
        foldButton.setVisible(false);

        for (int i = 0; i < 5; i++) {
            selectedChips[i] = 0;
            chipHeaderLabels[i].setVisible(true);
            plusButtons[i].setVisible(true);
            countLabels[i].setVisible(true);
            minusButtons[i].setVisible(true);
        }
        totalBetLabel.setVisible(true);
        confirmRaiseButton.setVisible(true);
        cancelRaiseButton.setVisible(true);

        refreshChipSelectorUI();
        this.revalidate();
        this.repaint();
    }

    private void refreshChipSelectorUI() {
        int currentTotal = 0;
        for (int i = 0; i < 5; i++) {
            currentTotal += selectedChips[i] * chipDollarValues[i];
            countLabels[i].setText(selectedChips[i] + "/" + ownedChips[i]);
            plusButtons[i].setEnabled(selectedChips[i] < ownedChips[i]);
            minusButtons[i].setEnabled(selectedChips[i] > 0);
        }
        totalBetLabel.setText("Bet: $" + currentTotal + " (Min: $" + minRequiredBet + ")");
        confirmRaiseButton.setEnabled(currentTotal >= minRequiredBet && currentTotal > 0);
    }

    private void hideChipSelector() {
        for (int i = 0; i < 5; i++) {
            chipHeaderLabels[i].setVisible(false);
            plusButtons[i].setVisible(false);
            countLabels[i].setVisible(false);
            minusButtons[i].setVisible(false);
        }
        totalBetLabel.setVisible(false);
        confirmRaiseButton.setVisible(false);
        cancelRaiseButton.setVisible(false);
    }

    public void setTurnState(String state) {
        hideChipSelector();

        if ("unopened".equals(state)) {
            checkButton.setVisible(true);
            raiseButton.setVisible(true);
            callButton.setVisible(false);
            reraiseButton.setVisible(false);
            foldButton.setVisible(true);
            this.setVisible(true);
        } else if ("facingBet".equals(state)) {
            checkButton.setVisible(false);
            raiseButton.setVisible(false);
            callButton.setVisible(true);
            reraiseButton.setVisible(true);
            foldButton.setVisible(true);
            this.setVisible(true);
        } else {
            this.setVisible(false);
        }
        this.revalidate();
        this.repaint();
    }

    public void setController(GameController controller) {
        this.gameController = controller;
    }
}