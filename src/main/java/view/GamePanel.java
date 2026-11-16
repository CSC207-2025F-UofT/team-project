package view;

import interface_adapter.game.GameState;
import interface_adapter.game.GameViewModel;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

/** Draws the current GameState. Only reads; never changes the game. */
public class GamePanel extends JPanel {
    private final GameViewModel viewModel;

    public GamePanel(GameViewModel viewModel, int width, int height) {
        this.viewModel = viewModel;
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
        setFocusable(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);   // clears the screen with the background colour
        Graphics2D g2 = (Graphics2D) g;
        GameState state = viewModel.getState();

        g2.setColor(Color.WHITE);
        g2.fillRect(state.getPlayerX(), state.getPlayerY(), state.getPlayerSize(), state.getPlayerSize());
    }
}
