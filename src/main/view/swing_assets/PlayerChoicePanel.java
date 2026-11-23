package main.view.swing_assets;

import main.interface_adapters.game.GameController;
import main.view.ViewUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PlayerChoicePanel extends JPanel implements ActionListener {

    private GameController gameController;

    private final JButton checkButton;
    private final JButton raiseButton;
    private final JButton foldButton;
    /*
    private final String state;
    private final JButton callButton;
    private final JButton reraiseButton;
    */
    public PlayerChoicePanel() {
        this.setLayout(null);
        this.setBackground(new Color(10,10,15));
        this.setBorder(BorderFactory.createLineBorder(new Color(200,200, 200), 6));
        ViewUtils.setRelativeBounds(this, 0.3, 0.77, 0.68, 0.21);

        checkButton = new JButton("Check");
        ViewUtils.setFont(checkButton, "Figtree Black", Font.PLAIN, 28, 0.05);
        ViewUtils.setRelativeBounds(checkButton, 0.015, 0.07, 0.2,0.12);
        checkButton.setForeground(Color.white);
        checkButton.setBackground(new Color(235, 140, 52));
        checkButton.setBorder(BorderFactory.createLineBorder(Color.white, 2));
        checkButton.setFocusPainted(false);
        this.add(checkButton);
        checkButton.addActionListener(this);

        raiseButton = new JButton("Raise");
        ViewUtils.setFont(raiseButton, "Figtree Black", Font.PLAIN, 28, 0.05);
        ViewUtils.setRelativeBounds(raiseButton, 0.235, 0.07, 0.2,0.12);
        raiseButton.setForeground(Color.white);
        raiseButton.setBackground(new Color(38, 201, 60));
        raiseButton.setBorder(BorderFactory.createLineBorder(Color.white, 2));
        raiseButton.setFocusPainted(false);
        this.add(raiseButton);

        foldButton = new JButton("Fold");
        ViewUtils.setFont(foldButton, "Figtree Black", Font.PLAIN, 28, 0.05);
        ViewUtils.setRelativeBounds(foldButton, 0.455, 0.07, 0.2,0.12);
        foldButton.setForeground(Color.white);
        foldButton.setBackground(new Color(201, 38, 76));
        foldButton.setBorder(BorderFactory.createLineBorder(Color.white, 2));
        foldButton.setFocusPainted(false);
        this.add(foldButton);
    }

    public void setController(GameController gameController) {
        this.gameController = gameController;
    }
    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource().equals(checkButton)) {
            gameController.playerCheck();
        }
    }
}
