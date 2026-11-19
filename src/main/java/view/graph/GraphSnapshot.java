package view.graph;

import interface_adapter.graph.GraphState;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

// still picture of a graph card, for the canvas save file, history + pdf studio
public final class GraphSnapshot {
    private GraphSnapshot() {
    }

    // default camera, no cursor readout, no buttons
    public static BufferedImage render(GraphState state, int width, int height) {
        return render(state, width, height, new OrbitCamera(), null);
    }

    static BufferedImage render(GraphState state, int width, int height, OrbitCamera camera,
                                interface_adapter.graph.GraphTrace pinned) {
        BufferedImage image = new BufferedImage(Math.max(1, width), Math.max(1, height), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        try {
            GraphCardRenderer.paint(g, state, width, height, camera, null, null, pinned, false,
                    GraphCardRenderer.NO_BUTTON);
        } finally {
            g.dispose();
        }
        return image;
    }
}
