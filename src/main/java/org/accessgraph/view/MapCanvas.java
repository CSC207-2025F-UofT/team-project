package org.accessgraph.view;

import org.accessgraph.entity.GeoPosition;
import org.accessgraph.use_case.station_map.MapViewport;

import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.BorderFactory;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;

final class MapCanvas extends JPanel {
    interface Gestures {
        void zoom(int steps, double xFraction, double yFraction);
        void pan(double xFraction, double yFraction);
    }

    private static final Color DEVICE = new Color(0x246BD5);
    private BufferedImage image;
    private MapViewport viewport;
    private GeoPosition position;
    private Point press;
    private final Point drag = new Point();
    private final Point pending = new Point();
    private double wheelRotation;

    MapCanvas(Gestures gestures) {
        setOpaque(true);
        setBackground(new Color(0xE3EBE8));
        setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
        setFocusable(true);
        getAccessibleContext().setAccessibleName("TTC station map");
        getAccessibleContext().setAccessibleDescription("Scroll to zoom. Drag or use the arrow keys to move the map.");
        MouseAdapter mouse = new MouseAdapter() {
            @Override public void mousePressed(MouseEvent event) {
                if (image != null && SwingUtilities.isLeftMouseButton(event)) {
                    requestFocusInWindow();
                    press = event.getPoint();
                }
            }

            @Override public void mouseDragged(MouseEvent event) {
                if (press != null) {
                    drag.setLocation(event.getX() - press.x, event.getY() - press.y);
                    repaint();
                }
            }

            @Override public void mouseReleased(MouseEvent event) {
                if (press == null) { return; }
                press = null;
                if (drag.x != 0 || drag.y != 0) {
                    pending.translate(drag.x, drag.y);
                    gestures.pan(drag.x / (double) getWidth(), drag.y / (double) getHeight());
                    drag.setLocation(0, 0);
                    repaint();
                }
            }

            @Override public void mouseWheelMoved(MouseWheelEvent event) {
                if (image == null || press != null) { return; }
                event.consume();
                wheelRotation -= event.getPreciseWheelRotation();
                int steps = (int) wheelRotation;
                wheelRotation -= steps;
                if (steps != 0) { gestures.zoom(steps, event.getX() / (double) getWidth(),
                        event.getY() / (double) getHeight()); }
            }
        };
        addMouseListener(mouse);
        addMouseMotionListener(mouse);
        addMouseWheelListener(mouse);
        bind("LEFT", () -> gestures.pan(0.15, 0));
        bind("RIGHT", () -> gestures.pan(-0.15, 0));
        bind("UP", () -> gestures.pan(0, 0.15));
        bind("DOWN", () -> gestures.pan(0, -0.15));
        Border focus = BorderFactory.createLineBorder(Ui.JADE, 2);
        addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent event) { setBorder(focus); }
            @Override public void focusLost(FocusEvent event) { setBorder(null); }
        });
    }

    private void bind(String key, Runnable action) {
        getInputMap(WHEN_FOCUSED).put(KeyStroke.getKeyStroke(key), key);
        getActionMap().put(key, new AbstractAction() {
            @Override public void actionPerformed(ActionEvent event) { if (image != null) { action.run(); } }
        });
    }

    void setImage(BufferedImage image, MapViewport viewport) {
        this.image = image;
        this.viewport = viewport;
        pending.setLocation(0, 0);
        repaint();
    }

    void clear() {
        setImage(null, null);
        setPosition(null);
        press = null;
        drag.setLocation(0, 0);
        wheelRotation = 0;
    }

    void setPosition(GeoPosition position) {
        this.position = position;
        repaint();
    }

    @Override protected void paintComponent(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics.create();
        try {
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
            if (image == null) { return; }
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            int left = pending.x + drag.x;
            int top = pending.y + drag.y;
            g.drawImage(image, left, top, getWidth(), getHeight(), null);
            if (position == null) { return; }
            MapViewport.Pixel point = viewport.screenPoint(position.getLatitude(), position.getLongitude());
            int x = left + (int) Math.round(point.x * getWidth() / viewport.getWidth());
            int y = top + (int) Math.round(point.y * getHeight() / viewport.getHeight());
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
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
}
