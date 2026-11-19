package view.graph;

import interface_adapter.graph.GraphExploreController;
import interface_adapter.graph.GraphState;
import interface_adapter.graph.GraphTrace;
import interface_adapter.graph.GraphViewModel;

import javax.swing.SwingUtilities;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;

/**
 * The live function graph card. Plot: drag pans (2d) or spins (3d), wheel zooms, hover reads the real
 * value off the graph, click pins that readout. Moving / resizing / buttons come from {@link PlotCard}.
 */
public final class GraphCard extends PlotCard {
    private static final double SNAP_PX = 9;          // this close to a key point in x = snap onto it

    private final GraphViewModel viewModel;
    private final GraphExploreController explore;
    private final OrbitCamera camera = new OrbitCamera();
    private GraphTrace pinned;
    private long revision = -1;
    // scene cache: rebuilt when the data, camera or size changes
    private SurfaceScene scene;
    private double[][] sceneZ;
    private int sceneCamera = -1;
    private double sceneW;
    private double sceneH;

    public GraphCard(GraphViewModel viewModel, GraphExploreController explore) {
        this.viewModel = viewModel;
        this.explore = explore;
        viewModel.addPropertyChangeListener(this::stateChanged);
    }

    @Override
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
    boolean hasContent() {
        return viewModel.getState().hasGraph();
    }

    @Override
    void paintCard(Graphics2D g, int hotButton) {
        GraphState s = viewModel.getState();
        GraphCardRenderer.paint(g, s, getWidth(), getHeight(), camera, sceneFor(s), s.getTrace(), pinned, true,
                hotButton);
    }

    private SurfaceScene sceneFor(GraphState s) {
        if (s.getKind() != GraphState.Kind.SURFACE) {
            return null;
        }
        if (scene == null || sceneZ != s.getZ() || sceneCamera != camera.version()
                || sceneW != getWidth() || sceneH != getHeight()) {
            scene = SurfaceScene.build(s, camera, plot());
            sceneZ = s.getZ();
            sceneCamera = camera.version();
            sceneW = getWidth();
            sceneH = getHeight();
        }
        return scene;
    }

    private boolean surface() {
        return viewModel.getState().getKind() == GraphState.Kind.SURFACE;
    }

    @Override
    int plotCursor() {
        return surface() ? Cursor.HAND_CURSOR : Cursor.CROSSHAIR_CURSOR;
    }

    // back home: window + camera
    @Override
    void resetView() {
        camera.reset();
        pinned = null;
        explore.reset();
        repaint();
        host().viewChanged();
    }

    @Override
    void hover(Point p) {
        traceAt(p);
    }

    @Override
    void hoverEnded() {
        endTrace();
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
            explore.trace(f.worldX(p.x), f.worldY(p.y), SNAP_PX * ui() * f.xPerPixel());
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

    @Override
    void plotDragged(double dx, double dy) {
        if (surface()) {
            camera.rotate(dx, dy);
            repaint();
        } else {
            PlotFrame f = new PlotFrame(viewModel.getState(), plot());
            explore.pan(-dx * f.xPerPixel(), dy * f.yPerPixel());
        }
    }

    // click on the plot: pin what's under the cursor, click the pin again to drop it
    @Override
    void plotClicked(Point p) {
        traceAt(p);
        GraphTrace now = viewModel.getState().getTrace();
        if (now == null || (pinned != null && pinned.getText().equals(now.getText()))) {
            pinned = null;
        } else {
            pinned = now;
        }
        repaint();
        host().viewChanged();
    }

    @Override
    void wheel(MouseWheelEvent e, double factor) {
        GraphState s = viewModel.getState();
        if (s.getKind() == GraphState.Kind.CURVE) {
            PlotFrame f = new PlotFrame(s, plot());
            // shift = x only, alt = y only, like stretching one axis
            double fx = e.isAltDown() ? 1 : factor;
            double fy = e.isShiftDown() ? 1 : factor;
            explore.zoom(f.worldX(e.getX()), f.worldY(e.getY()), fx, fy);
        } else {
            explore.zoom((s.getXMin() + s.getXMax()) / 2, (s.getYMin() + s.getYMax()) / 2, factor, factor);
        }
    }
}
