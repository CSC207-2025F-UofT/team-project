package view.graph;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * The floating card shell every live plot uses: header drag moves it, corner grip resizes it, ↺ / ×
 * buttons, click vs drag on the plot, wheel, double click home. What the plot does with those is the
 * subclass's call (pan, spin, trace, pin...).
 */
public abstract class PlotCard extends JComponent {
    // where the card sits is the canvas's business, the card just asks
    public interface Host {
        void moveBy(double dx, double dy);

        void resizeTo(double width, double height);

        void close();

        // something the snapshot should show changed (pan, zoom, spin, pin)
        void viewChanged();
    }

    static final double WHEEL_STEP = 1.12;
    static final double MIN_WIDTH = 240;
    static final double MIN_HEIGHT = 180;
    private static final int CLICK_SLOP = 3;   // pixels a click can wobble and still be a click

    private enum Drag { NONE, MOVE, RESIZE, PLOT }

    private static final Host NO_HOST = new Host() {
        @Override
        public void moveBy(double dx, double dy) {
        }

        @Override
        public void resizeTo(double width, double height) {
        }

        @Override
        public void close() {
        }

        @Override
        public void viewChanged() {
        }
    };

    private Host host = NO_HOST;
    private Drag drag = Drag.NONE;
    private Point pressScreen;
    private Point lastScreen;
    private boolean moved;
    private int hotButton = GraphCardRenderer.NO_BUTTON;

    protected PlotCard() {
        setOpaque(false);
        setVisible(false);
        MouseAdapter mouse = new Mouse();
        addMouseListener(mouse);
        addMouseMotionListener(mouse);
        addMouseWheelListener(mouse);
    }

    public void setHost(Host host) {
        this.host = host == null ? NO_HOST : host;
    }

    protected Host host() {
        return host;
    }

    // what the card looks like right now as a picture, for saving
    public abstract BufferedImage snapshot(int width, int height);

    // ---- what the plot does, subclass fills these in

    abstract boolean hasContent();

    abstract void paintCard(Graphics2D g, int hotButton);

    // drag on the plot, screen pixels since last time
    abstract void plotDragged(double dx, double dy);

    // plain click, no drag
    abstract void plotClicked(Point p);

    // cursor over the card (may be off the plot)
    abstract void hover(Point p);

    abstract void hoverEnded();

    abstract void wheel(MouseWheelEvent e, double factor);

    // ↺ / double click
    abstract void resetView();

    // cursor over the plot itself
    int plotCursor() {
        return Cursor.CROSSHAIR_CURSOR;
    }

    Rectangle2D plot() {
        return GraphCardRenderer.plotArea(getWidth(), getHeight());
    }

    float ui() {
        return GraphCardRenderer.ui(getWidth());
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (hasContent()) {
            paintCard((Graphics2D) g, hotButton);
        }
    }

    private int buttonAt(Point p) {
        if (GraphCardRenderer.closeButton(getWidth()).contains(p)) {
            return GraphCardRenderer.CLOSE_BUTTON;
        }
        if (GraphCardRenderer.resetButton(getWidth()).contains(p)) {
            return GraphCardRenderer.RESET_BUTTON;
        }
        return GraphCardRenderer.NO_BUTTON;
    }

    private void cursorFor(Point p) {
        int cursor;
        if (buttonAt(p) != GraphCardRenderer.NO_BUTTON) {
            cursor = Cursor.HAND_CURSOR;
        } else if (GraphCardRenderer.grip(getWidth(), getHeight()).contains(p)) {
            cursor = Cursor.SE_RESIZE_CURSOR;
        } else if (p.y < GraphCardRenderer.headerHeight(getWidth())) {
            cursor = Cursor.MOVE_CURSOR;
        } else {
            cursor = plotCursor();
        }
        setCursor(Cursor.getPredefinedCursor(cursor));
    }

    private final class Mouse extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            if (!SwingUtilities.isLeftMouseButton(e)) {
                return;
            }
            Point p = e.getPoint();
            pressScreen = e.getLocationOnScreen();
            lastScreen = pressScreen;
            moved = false;
            int button = buttonAt(p);
            if (button == GraphCardRenderer.CLOSE_BUTTON) {
                drag = Drag.NONE;
                host.close();
            } else if (button == GraphCardRenderer.RESET_BUTTON) {
                drag = Drag.NONE;
                resetView();
            } else if (GraphCardRenderer.grip(getWidth(), getHeight()).contains(p)) {
                drag = Drag.RESIZE;
            } else if (p.y < GraphCardRenderer.headerHeight(getWidth())) {
                drag = Drag.MOVE;
            } else {
                drag = Drag.PLOT;
                hoverEnded();   // no readout flapping around mid drag
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (drag == Drag.NONE || lastScreen == null) {
                return;
            }
            Point now = e.getLocationOnScreen();
            double dx = now.x - lastScreen.x;
            double dy = now.y - lastScreen.y;
            if (!moved && pressScreen.distance(now) < CLICK_SLOP) {
                return;
            }
            moved = true;
            lastScreen = now;
            switch (drag) {
                case MOVE:
                    host.moveBy(dx, dy);
                    break;
                case RESIZE:
                    host.resizeTo(Math.max(MIN_WIDTH, getWidth() + dx), Math.max(MIN_HEIGHT, getHeight() + dy));
                    break;
                default:
                    plotDragged(dx, dy);
                    break;
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            Drag was = drag;
            drag = Drag.NONE;
            if (was == Drag.NONE) {
                return;
            }
            if (moved) {
                host.viewChanged();
            } else if (was == Drag.PLOT && e.getClickCount() < 2) {
                plotClicked(e.getPoint());
            }
            if (was == Drag.PLOT) {
                hover(e.getPoint());
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e) && plot().contains(e.getPoint())) {
                resetView();
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            Point p = e.getPoint();
            int hot = buttonAt(p);
            if (hot != hotButton) {
                hotButton = hot;
                repaint();
            }
            cursorFor(p);
            hover(p);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (hotButton != GraphCardRenderer.NO_BUTTON) {
                hotButton = GraphCardRenderer.NO_BUTTON;
                repaint();
            }
            if (drag == Drag.NONE) {
                hoverEnded();
            }
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            if (!hasContent() || !plot().contains(e.getPoint())) {
                return;
            }
            wheel(e, Math.pow(WHEEL_STEP, e.getPreciseWheelRotation()));
            hover(e.getPoint());
            host.viewChanged();
        }
    }
}
