package view.canvas;

import interface_adapter.graph.GraphController;
import interface_adapter.graph.GraphExploreController;
import interface_adapter.graph.GraphState;
import interface_adapter.graph.GraphViewModel;
import view.FormulaCapture;
import view.graph.GraphCard;
import view.graph.GraphSnapshot;

import javax.swing.JComponent;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.function.BiConsumer;

// Graph from the popup: uses the latex already read, live graph card goes beside that formula
final class PopupGraphing extends PopupCardTool {
    private final GraphController controller;
    private final GraphViewModel viewModel;

    PopupGraphing(SharedCanvas canvas, GraphController controller, GraphExploreController explore,
                  GraphViewModel viewModel, Notices notices, JComponent area,
                  BiConsumer<FormulaCapture, String> graphSaved) {
        super(canvas, new GraphCard(viewModel, explore), notices, area, graphSaved, "popup-graph",
                "Can't graph", "Drawing the graph of", "Graph of");
        this.controller = controller;
        this.viewModel = viewModel;
        viewModel.addPropertyChangeListener(e -> {
            if (GraphViewModel.STATE_PROPERTY.equals(e.getPropertyName())) {
                stateChanged();
            } else if (GraphViewModel.VIEW_PROPERTY.equals(e.getPropertyName())) {
                viewChanged();
            }
        });
    }

    JComponent graphCard() {
        return card();
    }

    void graph(String latex, Rectangle formulaWorld, FormulaCapture capture) {
        run(latex, formulaWorld, capture);
    }

    @Override
    void execute(String latex, double[] xRange, double[] yRange) {
        controller.execute(latex, xRange, yRange);
    }

    @Override
    String error() {
        return viewModel.getState().getError();
    }

    @Override
    boolean hasContent() {
        return viewModel.getState().hasGraph();
    }

    @Override
    String latex() {
        return viewModel.getState().getLatex();
    }

    @Override
    double[] window() {
        GraphState s = viewModel.getState();
        return new double[]{s.getXMin(), s.getXMax(), s.getYMin(), s.getYMax()};
    }

    @Override
    boolean followsY() {
        return viewModel.getState().getKind() == GraphState.Kind.CURVE;
    }

    @Override
    BufferedImage firstPicture() {
        return GraphSnapshot.render(viewModel.getState(), SNAPSHOT_WIDTH, SNAPSHOT_HEIGHT);
    }
}
