package jswing;

import game.gameModel.Renderable;

import javax.swing.*;

/**
 * responsible for redrawing GameState panel according to a provided World object
 */
public class Renderer {

    private JPanel panel;

    public Renderer (JPanel panel) {
        this.panel = panel;
    }

    public void render(Iterable<? extends Renderable> renderables) {
        panel.removeAll();

        for (Renderable renderable : renderables) {
            JLabel component = renderable.getTexture().getContent();

            int width = renderable.getWidth();
            int height = renderable.getHeight();

            // Originally, setBounds(x, y, width, height) uses x and y to represent the top-left
            // corner of the component.
            // However, Renderable.getX() and getY() represent the bottom-center point of the picture.
            // The following conversion transforms the bottom-center coordinates provided by Renderable
            // into the top-left coordinates required by setBounds().
            component.setBounds(
                    renderable.getX() - width / 2,
                    renderable.getY() - height,
                    width,
                    height
            );

            panel.add(component);
        }

        panel.revalidate();
        panel.repaint();
    }

}
