package view.graph;

import interface_adapter.phase.PhaseReadout;
import interface_adapter.phase.PhaseState;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

// still picture of a phase card, for the canvas save file, history + pdf studio
public final class PhaseSnapshot {
    private PhaseSnapshot() {
    }

    public static BufferedImage render(PhaseState state, int width, int height) {
        BufferedImage image = new BufferedImage(Math.max(1, width), Math.max(1, height), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        try {
            paint(g, state, width, height, null, false, GraphCardRenderer.NO_BUTTON);
        } finally {
            g.dispose();
        }
        return image;
    }

    static void paint(Graphics2D g, PhaseState s, double w, double h, PhaseReadout hover, boolean live, int hot) {
        paint(g, s, w, h, hover, live, hot, null);
    }

    static String badge(PhaseState s) {
        return s.getKind() == PhaseState.Kind.SLOPE_FIELD ? "Slope field" : "Phase plane";
    }

    // controls = size of the live x / y boxes sitting in the header, null for none
    static void paint(Graphics2D g, PhaseState s, double w, double h, PhaseReadout hover, boolean live, int hot,
                      java.awt.Dimension controls) {
        GraphCardRenderer.paintCard(g, w, h, s.getLatex(), badge(s), GraphTheme.PHASE, null, live, hot, controls,
                (gg, plot, ui) -> PhasePainter.paint(gg, s, plot, ui, hover));
    }
}
