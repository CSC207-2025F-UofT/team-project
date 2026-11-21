package view;

import interface_adapter.map.MapState;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;

/** Builds a complete terrain image and reuses it while the displayed terrain is unchanged. */
public final class MapRenderer {
    private final TileImageSource sprites;
    private MapState lastState;
    private MapState renderedState;
    private BufferedImage cachedImage;
    private String errorMessage = "";

    public MapRenderer(TileImageSource sprites) {
        if (sprites == null) {
            throw new IllegalArgumentException("Tile sprites must not be null.");
        }
        this.sprites = sprites;
    }

    /**
     * Returns the cached image at the map's pixel dimensions, or null when no map is available.
     * Callers must treat the image as read-only and use this renderer from one thread (the
     * Swing event thread once connected to the game). No file access occurs during rendering.
     * A failed render preserves the last complete image and records an explanation.
     *
     * @param state non-null display state containing a validated map or the empty initial state
     */
    public BufferedImage render(MapState state) {
        if (state == lastState) {
            return cachedImage;
        }
        lastState = state;
        errorMessage = state.getErrorMessage();
        if (!state.hasMap()) {
            cachedImage = null;
            renderedState = null;
            return null;
        }
        if (renderedState != null && state.getTileSize() == renderedState.getTileSize()
                && Arrays.deepEquals(state.getTileIds(), renderedState.getTileIds())) {
            return cachedImage;
        }

        try {
            BufferedImage nextImage = drawMap(state);
            cachedImage = nextImage;
            renderedState = state;
        } catch (IllegalArgumentException error) {
            errorMessage = "Unable to render map: " + error.getMessage();
        }
        return cachedImage;
    }

    /** Returns the loading or rendering error for the latest state, or an empty string. */
    public String getErrorMessage() {
        return errorMessage;
    }

    private BufferedImage drawMap(MapState state) {
        BufferedImage image = new BufferedImage(state.getWidthInPixels(), state.getHeightInPixels(),
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        try {
            int tileSize = state.getTileSize();
            for (int row = 0; row < state.getRowCount(); row++) {
                for (int col = 0; col < state.getColumnCount(); col++) {
                    BufferedImage tile = sprites.getImage(state.getTileId(col, row));
                    graphics.drawImage(tile, col * tileSize, row * tileSize, tileSize, tileSize, null);
                }
            }
        } finally {
            graphics.dispose();
        }
        return image;
    }
}
