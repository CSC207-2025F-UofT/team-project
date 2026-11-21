package view;

import interface_adapter.game.GameState;
import interface_adapter.game.GameViewModel;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.beans.PropertyChangeListener;

/** Draws the map background, player and any map error. Only reads display state. */
public class GamePanel extends JPanel {
    private final GameViewModel viewModel;
    private final MapLayer mapLayer;
    private final PropertyChangeListener mapListener = event -> repaint();

    /** Creates a panel without a map layer, for callers using only the core loop. */
    public GamePanel(GameViewModel viewModel, int width, int height) {
        this(viewModel, width, height, null);
    }

    /** Creates a panel with an injected map layer, drawn behind the player. */
    public GamePanel(GameViewModel viewModel, int width, int height, MapLayer mapLayer) {
        this.viewModel = viewModel;
        this.mapLayer = mapLayer;
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
        setFocusable(true);
    }

    @Override
    public void addNotify() {
        super.addNotify();
        if (mapLayer != null) {
            mapLayer.addPropertyChangeListener(mapListener);
        }
    }

    @Override
    public void removeNotify() {
        if (mapLayer != null) {
            mapLayer.removePropertyChangeListener(mapListener);
        }
        super.removeNotify();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);   // clears the screen with the background colour
        Graphics2D g2 = (Graphics2D) g;
        if (mapLayer != null) {
            mapLayer.draw(g2);
        }
        GameState state = viewModel.getState();

        g2.setColor(Color.WHITE);
        g2.fillRect(state.getPlayerX(), state.getPlayerY(), state.getPlayerSize(), state.getPlayerSize());
        if (mapLayer != null) {
            mapLayer.drawError(g2, getWidth());
        }
    }
}
