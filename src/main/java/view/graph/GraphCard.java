package view.graph;

import interface_adapter.graph.GraphExploreController;
import interface_adapter.graph.GraphState;
import interface_adapter.graph.GraphTrace;
import interface_adapter.graph.GraphViewModel;

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
import java.beans.PropertyChangeEvent;

/**
 * The live graph card that floats over the canvas. Header: drag to move the card (issue #45),
 * reset + close buttons. Plot: drag pans (2d) or spins (3d), wheel zooms, hover reads the real
 * value off the graph, click pins that readout, double click goes back home. Corner grip resizes.
 */
public final class GraphCard extends JComponent {
    // where the card sits is the canvas's business, the card just asks
    public interface Host {
        void moveBy(double dx, double dy);

        void resizeTo(double width, double height);

        void close();

        // something the snapshot should show changed (pan, zoom, spin, pin)
        void viewChanged();
    }

    private static final double SNAP_PX = 9;          // this close to a key point in x = snap onto it
    private static final double WHEEL_STEP = 1.12;
    private static final double MIN_WIDTH = 240;
    private static final double MIN_HEIGHT = 180;
    private static final int CLICK_SLOP = 3;          // pixels a click can wobble and still be a click

    private enum Drag { NONE, MOVE, RESIZE, PAN, ROTATE }

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

    private final GraphViewModel viewModel;
    private final GraphExploreController explore;
    private final OrbitCamera camera = new OrbitCamera();
    private Host host = NO_HOST;
    private GraphTrace pinned;
    private long revision = -1;
    private Drag drag = Drag.NONE;
    private Point pressScreen;
    private Point lastScreen;
    private boolean moved;
    private int hotButton = GraphCardRenderer.NO_BUTTON;
    // scene cache: rebuilt when the data, camera or size changes
    private SurfaceScene scene;
    private double[][] sceneZ;
    private int sceneCamera = -1;
    private double sceneW;
    private double sceneH;

    public GraphCard(GraphViewModel viewModel, GraphExploreController explore) {
        this.viewModel = viewModel;
        this.explore = explore;
        setOpaque(false);
        setVisible(false);
        MouseAdapter mouse = new Mouse();
        addMouseListener(mouse);
        addMouseMotionListener(mouse);
        addMouseWheelListener(mouse);
        viewModel.addPropertyChangeListener(this::stateChanged);
    }

    public void setHost(Host host) {
        this.host = host == null ? NO_HOST : host;
    }

    // what the card looks like right now (camera, pin) as a picture, for saving
    public BufferedImage snapshot(int width, int height) {
        return GraphSnapshot.render(viewModel.getState(), width, height, camera, pinned);
    }

    GraphTrace pinned() {
        return pinned;
    }

    OrbitCamera camera() {
        return camera;
    }

    private void stateChanged(PropertyChangeEvent e) {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(() -> stateChanged(e));
            return;
        }
        GraphState s = viewModel.getState();
        if (GraphViewModel.STATE_PROPERTY.equals(e.getPropertyName()) && s.getRevision() != revision) {
            // brand new graph, start the camera + pin over
            revision = s.getRevision();
            camera.reset();
            pinned = null;
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        GraphState s = viewModel.getState();
        if (!s.hasGraph()) {
            return;
        }
        GraphCardRenderer.paint((Graphics2D) g, s, getWidth(), getHeight(), camera, sceneFor(s),
                s.getTrace(), pinned, true, hotButton);
    }

    private SurfaceScene sceneFor(GraphState s) {
        if (s.getKind() != GraphState.Kind.SURFACE) {
            return null;
        }
        if (scene == null || sceneZ != s.getZ() || sceneCamera != camera.version()
                || sceneW != getWidth() || sceneH != getHeight()) {
            scene = SurfaceScene.build(s, camera, GraphCardRenderer.plotArea(getWidth(), getHeight()));
            sceneZ = s.getZ();
            sceneCamera = camera.version();
            sceneW = getWidth();
            sceneH = getHeight();
        }
        return scene;
    }

    private Rectangle2D plot() {
        return GraphCardRenderer.plotArea(getWidth(), getHeight());
    }

    private boolean surface() {
        return viewModel.getState().getKind() == GraphState.Kind.SURFACE;
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

    // back home: window + camera
    void resetView() {
        camera.reset();
        pinned = null;
        explore.reset();
        repaint();
        host.viewChanged();
    }

    // cursor -> real point on the graph (or nothing)
    void traceAt(Point p) {
        GraphState s = viewModel.getState();
        if (!s.hasGraph() || !plot().contains(p)) {
            endTrace();
            return;
        }
        if (s.getKind() == GraphState.Kind.CURVE) {
            PlotFrame f = new PlotFrame(s, plot());
            explore.trace(f.worldX(p.x), f.worldY(p.y), SNAP_PX * GraphCardRenderer.ui(getWidth()) * f.xPerPixel());
            return;
        }
        double[] xy = sceneFor(s).pick(p.x, p.y);
        if (xy == null) {
            endTrace();
        } else {
            explore.trace(xy[0], xy[1], 0);
        }
    }

    private void endTrace() {
        if (viewModel.getState().getTrace() != null) {
            explore.endTrace();
        }
    }

    // click on the plot: pin what's under the cursor, click the pin again to drop it
    private void togglePin() {
        GraphTrace now = viewModel.getState().getTrace();
        if (now == null || (pinned != null && pinned.getText().equals(now.getText()))) {
            pinned = null;
        } else {
            pinned = now;
        }
        repaint();
        host.viewChanged();
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
            cursor = surface() ? Cursor.HAND_CURSOR : Cursor.CROSSHAIR_CURSOR;
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
                drag = surface() ? Drag.ROTATE : Drag.PAN;
                endTrace();   // no readout flapping around mid drag
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
                case PAN:
                    PlotFrame f = new PlotFrame(viewModel.getState(), plot());
                    explore.pan(-dx * f.xPerPixel(), dy * f.yPerPixel());
                    break;
                default:
                    camera.rotate(dx, dy);
                    repaint();
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
            } else if ((was == Drag.PAN || was == Drag.ROTATE) && e.getClickCount() < 2) {
                traceAt(e.getPoint());
                togglePin();
            }
            if (was == Drag.PAN || was == Drag.ROTATE) {
                traceAt(e.getPoint());
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
            traceAt(p);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (hotButton != GraphCardRenderer.NO_BUTTON) {
                hotButton = GraphCardRenderer.NO_BUTTON;
                repaint();
            }
            if (drag == Drag.NONE) {
                endTrace();
            }
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            GraphState s = viewModel.getState();
            if (!s.hasGraph() || !plot().contains(e.getPoint())) {
                return;
            }
            double factor = Math.pow(WHEEL_STEP, e.getPreciseWheelRotation());
            if (s.getKind() == GraphState.Kind.CURVE) {
                PlotFrame f = new PlotFrame(s, plot());
                // shift = x only, alt = y only, like stretching one axis
                double fx = e.isAltDown() ? 1 : factor;
                double fy = e.isShiftDown() ? 1 : factor;
                explore.zoom(f.worldX(e.getX()), f.worldY(e.getY()), fx, fy);
            } else {
                explore.zoom((s.getXMin() + s.getXMax()) / 2, (s.getYMin() + s.getYMax()) / 2, factor, factor);
            }
            traceAt(e.getPoint());
            host.viewChanged();
        }
    }
}
