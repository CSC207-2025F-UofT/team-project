package org.accessgraph.view;

import org.accessgraph.entity.GeoPosition;
import org.accessgraph.entity.Station;
import org.accessgraph.use_case.station_map.MapViewport;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.accessgraph.use_case.station_map.MapViewport.MAX_ZOOM;
import static org.accessgraph.use_case.station_map.MapViewport.TILE_SIZE;

/** Paints map tiles straight from memory so panning and zooming never wait for the network. */
final class MapCanvas extends JPanel {
    interface Gestures {
        void zoom(double steps, double xFraction, double yFraction);
        void zoomStep(int direction);
        void pan(double xFraction, double yFraction);
    }

    private static final Color DEVICE = new Color(0x246BD5);
    private static final Color STATION = new Color(0x06635B);
    private static final double ZOOM_PER_NOTCH = 1.0 / 8;
    private static final double MILLIS_PER_NOTCH = 100;
    private static final int ANCESTOR_LEVELS = 6;
    private static final int DESCENDANT_LEVELS = 2;
    /** Station names fade in between these zoom levels, once the map is close enough to separate them. */
    private static final double LABEL_ZOOM_START = 14.5;
    private static final double LABEL_ZOOM_FULL = 15;
    private static final Font LABEL_FONT = Ui.font(13, true);
    private static final int LABEL_PADDING_X = 9;
    private static final int LABEL_PADDING_Y = 4;
    private static final int LEADER_X = 12;
    private static final int LEADER_Y = 12;
    private static final int[][] LABEL_DIRECTIONS = {{1, -1}, {-1, -1}, {1, 1}, {-1, 1}};
    private final TileLoader tiles;
    private MapViewport viewport;
    private GeoPosition position;
    private List<Station> stations = List.of();
    private Point last;
    private long lastScrollTime;
    private double lastDirection;
    private double availableNotches;

    MapCanvas(TileLoader tiles, Gestures gestures) {
        this.tiles = tiles;
        setOpaque(true);
        setBackground(new Color(0xE3EBE8));
        setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
        setFocusable(true);
        getAccessibleContext().setAccessibleName("TTC station map");
        getAccessibleContext().setAccessibleDescription(
                "Scroll or press plus and minus to zoom. Drag or use the arrow keys to move the map.");
        MouseAdapter mouse = new MouseAdapter() {
            @Override public void mousePressed(MouseEvent event) {
                if (viewport != null && SwingUtilities.isLeftMouseButton(event)) {
                    requestFocusInWindow();
                    last = event.getPoint();
                }
            }

            @Override public void mouseDragged(MouseEvent event) {
                if (last == null) { return; }
                int dx = event.getX() - last.x;
                int dy = event.getY() - last.y;
                last = event.getPoint();
                if (dx != 0 || dy != 0) { gestures.pan(dx / (double) getWidth(), dy / (double) getHeight()); }
            }

            @Override public void mouseReleased(MouseEvent event) { last = null; }

            @Override public void mouseWheelMoved(MouseWheelEvent event) {
                if (viewport == null || last != null) { return; }
                event.consume();
                double steps = scrollZoom(event);
                if (steps != 0) {
                    gestures.zoom(steps, event.getX() / (double) getWidth(), event.getY() / (double) getHeight());
                }
            }
        };
        addMouseListener(mouse);
        addMouseMotionListener(mouse);
        addMouseWheelListener(mouse);
        bind("LEFT", () -> gestures.pan(0.15, 0));
        bind("RIGHT", () -> gestures.pan(-0.15, 0));
        bind("UP", () -> gestures.pan(0, 0.15));
        bind("DOWN", () -> gestures.pan(0, -0.15));
        for (String key : new String[]{"EQUALS", "shift EQUALS", "PLUS", "ADD"}) { bind(key, () -> gestures.zoomStep(1)); }
        for (String key : new String[]{"MINUS", "SUBTRACT"}) { bind(key, () -> gestures.zoomStep(-1)); }
        Border focus = BorderFactory.createLineBorder(Ui.JADE, 2);
        addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent event) { setBorder(focus); }
            @Override public void focusLost(FocusEvent event) { setBorder(null); }
        });
    }

    private double scrollZoom(MouseWheelEvent event) {
        double rotation = event.getPreciseWheelRotation();
        double direction = Math.signum(rotation);
        // Give the first notch an immediate response. Limit accelerated wheel and trackpad bursts.
        long elapsed = Math.max(0, event.getWhen() - lastScrollTime);
        availableNotches = direction == lastDirection
                ? Math.min(1, availableNotches + elapsed / MILLIS_PER_NOTCH) : 1;
        double notches = Math.min(Math.abs(rotation), availableNotches);
        availableNotches -= notches;
        lastDirection = direction;
        lastScrollTime = event.getWhen();
        return -direction * notches * ZOOM_PER_NOTCH;
    }

    private void bind(String key, Runnable action) {
        getInputMap(WHEN_FOCUSED).put(KeyStroke.getKeyStroke(key), key);
        getActionMap().put(key, new AbstractAction() {
            @Override public void actionPerformed(ActionEvent event) { if (viewport != null) { action.run(); } }
        });
    }

    void setStations(List<Station> stations) { this.stations = List.copyOf(stations); }

    void setViewport(MapViewport viewport) {
        this.viewport = viewport;
        if (viewport != null) {
            double centerX = viewport.getWidth() / 2.0;
            double centerY = viewport.getHeight() / 2.0;
            List<Tile> visible = visibleTiles(viewport);
            visible.sort(Comparator.comparingDouble(tile -> tile.distanceSquared(centerX, centerY)));
            List<Long> keys = new ArrayList<>(visible.size());
            for (Tile tile : visible) { keys.add(TileLoader.key(tile.zoom, tile.x, tile.y)); }
            tiles.want(keys);
        }
        repaint();
    }

    void reloadTiles() {
        tiles.retry();
        setViewport(viewport);
    }

    void setPosition(GeoPosition position) {
        this.position = position;
        repaint();
    }

    void clear() {
        viewport = null;
        position = null;
        last = null;
        lastDirection = 0;
        tiles.cancel();
        repaint();
    }

    @Override protected void paintComponent(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics.create();
        try {
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
            if (viewport == null) { return; }
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            for (Tile tile : visibleTiles(viewport)) {
                BufferedImage image = tiles.get(tile.zoom, tile.x, tile.y);
                if (image != null) {
                    g.drawImage(image, tile.left, tile.top, tile.right - tile.left, tile.bottom - tile.top, null);
                } else {
                    // Until the exact tile arrives, show what is already in memory: a coarser parent tile,
                    // then any sharper child tiles on top (these are what the map just showed when zooming out).
                    drawAncestor(g, tile);
                    drawDescendants(g, tile.zoom, tile.x, tile.y, tile.left, tile.top, tile.right, tile.bottom,
                            DESCENDANT_LEVELS);
                }
            }
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            List<Pin> pins = new ArrayList<>();
            for (Station station : stations) {
                MapViewport.Pixel point = viewport.screenPoint(station.getLatitude(), station.getLongitude());
                if (point.x > -10 && point.y > -10 && point.x < getWidth() + 10 && point.y < getHeight() + 10) {
                    Pin pin = new Pin(station.getName(), (int) Math.round(point.x), (int) Math.round(point.y));
                    pins.add(pin);
                    drawDot(g, pin.x, pin.y, 5);
                }
            }
            drawLabels(g, pins);
            if (position == null) { return; }
            MapViewport.Pixel point = viewport.screenPoint(position.getLatitude(), position.getLongitude());
            int x = (int) Math.round(point.x);
            int y = (int) Math.round(point.y);
            g.setColor(new Color(36, 107, 213, 60));
            g.fillOval(x - 20, y - 20, 40, 40);
            g.setColor(Color.WHITE);
            g.fillOval(x - 11, y - 11, 22, 22);
            g.setColor(DEVICE);
            g.fillOval(x - 8, y - 8, 16, 16);
        } finally {
            g.dispose();
        }
    }

    private void drawAncestor(Graphics2D g, Tile tile) {
        for (int levels = 1; levels <= Math.min(tile.zoom, ANCESTOR_LEVELS); levels++) {
            int parentX = tile.x >> levels;
            int parentY = tile.y >> levels;
            BufferedImage image = tiles.get(tile.zoom - levels, parentX, parentY);
            if (image == null) { continue; }
            int span = TILE_SIZE >> levels;
            int sourceX = (tile.x - (parentX << levels)) * span;
            int sourceY = (tile.y - (parentY << levels)) * span;
            g.drawImage(image, tile.left, tile.top, tile.right, tile.bottom,
                    sourceX, sourceY, sourceX + span, sourceY + span, null);
            return;
        }
    }

    private void drawDescendants(Graphics2D g, int zoom, int x, int y, int left, int top, int right, int bottom,
                                 int depth) {
        if (zoom >= MAX_ZOOM) { return; }
        int middleX = (left + right) / 2;
        int middleY = (top + bottom) / 2;
        for (int row = 0; row < 2; row++) {
            for (int column = 0; column < 2; column++) {
                int childLeft = column == 0 ? left : middleX;
                int childRight = column == 0 ? middleX : right;
                int childTop = row == 0 ? top : middleY;
                int childBottom = row == 0 ? middleY : bottom;
                BufferedImage image = tiles.get(zoom + 1, 2 * x + column, 2 * y + row);
                if (image != null) {
                    g.drawImage(image, childLeft, childTop, childRight - childLeft, childBottom - childTop, null);
                } else if (depth > 1) {
                    drawDescendants(g, zoom + 1, 2 * x + column, 2 * y + row,
                            childLeft, childTop, childRight, childBottom, depth - 1);
                }
            }
        }
    }

    /**
     * Draws each station's name in a pill joined to its dot by a short leader line. Stations nearest the
     * center are labelled first, and a label is skipped rather than drawn over another label or station.
     */
    private void drawLabels(Graphics2D graphics, List<Pin> pins) {
        double opacity = Math.max(0, Math.min(1,
                (viewport.getZoom() - LABEL_ZOOM_START) / (LABEL_ZOOM_FULL - LABEL_ZOOM_START)));
        if (opacity == 0 || pins.isEmpty()) { return; }
        Graphics2D g = (Graphics2D) graphics.create();
        try {
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) opacity));
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            g.setFont(LABEL_FONT);
            FontMetrics metrics = g.getFontMetrics();
            int height = metrics.getAscent() + metrics.getDescent() + 2 * LABEL_PADDING_Y;
            Rectangle bounds = new Rectangle(0, 0, getWidth(), getHeight());
            List<Rectangle> taken = new ArrayList<>();
            for (Pin pin : pins) { taken.add(new Rectangle(pin.x - 8, pin.y - 8, 16, 16)); }
            double centerX = getWidth() / 2.0;
            double centerY = getHeight() / 2.0;
            List<Pin> ordered = new ArrayList<>(pins);
            ordered.sort(Comparator.comparingDouble(pin -> Math.hypot(pin.x - centerX, pin.y - centerY)));
            for (Pin pin : ordered) {
                int width = metrics.stringWidth(pin.name) + 2 * LABEL_PADDING_X;
                for (int[] direction : LABEL_DIRECTIONS) {
                    int endX = pin.x + direction[0] * LEADER_X;
                    int endY = pin.y + direction[1] * LEADER_Y;
                    Rectangle label = new Rectangle(direction[0] > 0 ? endX : endX - width, endY - height / 2,
                            width, height);
                    if (!bounds.contains(label) || taken.stream().anyMatch(label::intersects)) { continue; }
                    taken.add(label);
                    g.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g.setColor(STATION);
                    g.drawLine(pin.x + direction[0] * 6, pin.y + direction[1] * 6, endX, endY);
                    g.setColor(Color.WHITE);
                    g.fillRoundRect(label.x, label.y, label.width, label.height, height, height);
                    g.setStroke(new BasicStroke(1.5f));
                    g.setColor(STATION);
                    g.drawRoundRect(label.x, label.y, label.width, label.height, height, height);
                    g.setColor(Ui.INK);
                    g.drawString(pin.name, label.x + LABEL_PADDING_X, label.y + LABEL_PADDING_Y + metrics.getAscent());
                    break;
                }
            }
        } finally {
            g.dispose();
        }
    }

    private static void drawDot(Graphics2D g, int x, int y, int radius) {
        g.setColor(Color.WHITE);
        g.fillOval(x - radius - 2, y - radius - 2, 2 * radius + 4, 2 * radius + 4);
        g.setColor(STATION);
        g.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);
    }

    /** Tiles covering the viewport, at the zoom level nearest the current (possibly fractional) zoom. */
    private static List<Tile> visibleTiles(MapViewport view) {
        int zoom = (int) Math.round(view.getZoom());
        int count = 1 << zoom;
        double size = TILE_SIZE * Math.pow(2, view.getZoom() - zoom);
        double left = view.getLeft();
        double top = view.getTop();
        int firstX = (int) Math.floor(left / size);
        int lastX = (int) Math.ceil((left + view.getWidth()) / size) - 1;
        int firstY = Math.max(0, (int) Math.floor(top / size));
        int lastY = Math.min(count - 1, (int) Math.ceil((top + view.getHeight()) / size) - 1);
        List<Tile> result = new ArrayList<>();
        for (int y = firstY; y <= lastY; y++) {
            for (int x = firstX; x <= lastX; x++) {
                result.add(new Tile(zoom, Math.floorMod(x, count), y,
                        (int) Math.round(x * size - left), (int) Math.round(y * size - top),
                        (int) Math.round((x + 1) * size - left), (int) Math.round((y + 1) * size - top)));
            }
        }
        return result;
    }

    private static final class Pin {
        final String name;
        final int x;
        final int y;

        Pin(String name, int x, int y) {
            this.name = name;
            this.x = x;
            this.y = y;
        }
    }

    private static final class Tile {
        final int zoom;
        final int x;
        final int y;
        final int left;
        final int top;
        final int right;
        final int bottom;

        Tile(int zoom, int x, int y, int left, int top, int right, int bottom) {
            this.zoom = zoom;
            this.x = x;
            this.y = y;
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }

        double distanceSquared(double centerX, double centerY) {
            double dx = (left + right) / 2.0 - centerX;
            double dy = (top + bottom) / 2.0 - centerY;
            return dx * dx + dy * dy;
        }
    }
}
