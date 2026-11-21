package view;

import interface_adapter.map.MapViewModel;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/** Draws the map background and error text supplied by the map display state. */
public final class MapLayer {
    private static final int PADDING = 12;

    private final MapViewModel viewModel;
    private final MapRenderer renderer;

    public MapLayer(MapViewModel viewModel, MapRenderer renderer) {
        this.viewModel = viewModel;
        this.renderer = renderer;
    }

    /** Draws the latest map at its original size. Called on Swing's event thread. */
    public void draw(Graphics2D graphics) {
        BufferedImage image = renderer.render(viewModel.getState());
        if (image != null) {
            graphics.drawImage(image, 0, 0, null);
        }
    }

    /** Draws any loading or rendering error above the game content after draw has run. */
    public void drawError(Graphics2D graphics, int panelWidth) {
        String error = renderer.getErrorMessage();
        if (error.isEmpty()) {
            return;
        }
        FontMetrics metrics = graphics.getFontMetrics();
        List<String> lines = wrap(error, metrics, Math.max(1, panelWidth - 2 * PADDING));
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, panelWidth, lines.size() * metrics.getHeight() + 2 * PADDING);
        graphics.setColor(Color.WHITE);
        int y = PADDING + metrics.getAscent();
        for (String line : lines) {
            graphics.drawString(line, PADDING, y);
            y += metrics.getHeight();
        }
    }

    /** Subscribes a view to repaint when the map state changes. */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        viewModel.addPropertyChangeListener(listener);
    }

    /** Removes a view's subscription when that view is detached. */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        viewModel.removePropertyChangeListener(listener);
    }

    private List<String> wrap(String text, FontMetrics metrics, int width) {
        List<String> lines = new ArrayList<>();
        StringBuilder line = new StringBuilder();
        for (int index = 0; index < text.length(); index++) {
            char character = text.charAt(index);
            if (character == '\n') {
                lines.add(line.toString());
                line.setLength(0);
            } else {
                if (line.length() > 0 && metrics.stringWidth(line.toString() + character) > width) {
                    lines.add(line.toString());
                    line.setLength(0);
                }
                line.append(character);
            }
        }
        lines.add(line.toString());
        return lines;
    }
}
