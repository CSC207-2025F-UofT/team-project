package view.graph;

import interface_adapter.phase.PhaseExploreController;
import interface_adapter.phase.PhaseState;
import interface_adapter.phase.PhaseViewModel;

import javax.swing.SwingUtilities;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;

/**
 * The live phase portrait card. Plot: drag pans, wheel zooms, hover follows the solution through the
 * cursor and reads the field there (snaps onto equilibria), click pins that solution, click it again
 * to drop it. Moving / resizing / buttons come from {@link PlotCard}.
 */
public final class PhaseCard extends PlotCard {
    private static final double SNAP_PX = 10;   // this close to an equilibrium / pinned curve = on it

    private final PhaseViewModel viewModel;
    private final PhaseExploreController explore;

    public PhaseCard(PhaseViewModel viewModel, PhaseExploreController explore) {
        this.viewModel = viewModel;
        this.explore = explore;
        viewModel.addPropertyChangeListener(e -> {
            if (SwingUtilities.isEventDispatchThread()) {
                repaint();
            } else {
                SwingUtilities.invokeLater(this::repaint);
            }
        });
    }

    @Override
    public BufferedImage snapshot(int width, int height) {
        return PhaseSnapshot.render(viewModel.getState(), width, height);
    }

    @Override
    boolean hasContent() {
        return viewModel.getState().hasPortrait();
    }

    @Override
    String headerLatex() {
        return viewModel.getState().getLatex();
    }

    @Override
    String headerBadge() {
        return PhaseSnapshot.badge(viewModel.getState());
    }

    @Override
    void paintCard(Graphics2D g, int hotButton) {
        PhaseState s = viewModel.getState();
        PhaseSnapshot.paint(g, s, getWidth(), getHeight(), s.getTrace(), true, hotButton, headerControls());
    }

    private PlotFrame frame() {
        PhaseState s = viewModel.getState();
        return new PlotFrame(s.getXMin(), s.getXMax(), s.getYMin(), s.getYMax(), plot());
    }

    @Override
    void plotDragged(double dx, double dy) {
        PlotFrame f = frame();
        explore.pan(-dx * f.xPerPixel(), dy * f.yPerPixel());
    }

    // pin the solution through here, or unpin the one you clicked on
    @Override
    void plotClicked(Point p) {
        if (!plot().contains(p)) {
            return;
        }
        PlotFrame f = frame();
        double snap = SNAP_PX * ui();
        explore.toggleSolution(f.worldX(p.x), f.worldY(p.y), snap * f.xPerPixel(), snap * f.yPerPixel());
        host().viewChanged();
    }

    @Override
    void hover(Point p) {
        traceAt(p);
    }

    // cursor -> the real field there + the solution through it
    void traceAt(Point p) {
        if (!hasContent() || !plot().contains(p)) {
            hoverEnded();
            return;
        }
        PlotFrame f = frame();
        double snap = SNAP_PX * ui();
        explore.trace(f.worldX(p.x), f.worldY(p.y), snap * f.xPerPixel(), snap * f.yPerPixel());
    }

    @Override
    void hoverEnded() {
        if (viewModel.getState().getTrace() != null) {
            explore.endTrace();
        }
    }

    @Override
    void wheel(MouseWheelEvent e, double factor) {
        PlotFrame f = frame();
        double fx = e.isAltDown() ? 1 : factor;    // shift = x only, alt = y only
        double fy = e.isShiftDown() ? 1 : factor;
        explore.zoom(f.worldX(e.getX()), f.worldY(e.getY()), fx, fy);
    }

    // home window, pins cleared
    @Override
    void resetView() {
        explore.reset();
        repaint();
        host().viewChanged();
    }
}
