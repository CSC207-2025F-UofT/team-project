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
import java.util.function.Supplier;

// Graph from the popup: uses the latex already read, each graph gets its own live card beside its formula
final class PopupGraphing extends PopupCardTool<GraphSession> {


    // one shared session: a single live card, a new graph replaces it
    PopupGraphing(SharedCanvas canvas, GraphController controller, GraphExploreController explore,
                  GraphViewModel viewModel, Notices notices, JComponent area,
                  BiConsumer<FormulaCapture, String> graphSaved) {
        this(canvas, shared(new GraphSession(controller, explore, viewModel)), notices, area, graphSaved);
    }

    // a fresh session per graph: several live graphs on the canvas at once
    PopupGraphing(SharedCanvas canvas, Supplier<GraphSession> kits, Notices notices, JComponent area,
                  BiConsumer<FormulaCapture, String> graphSaved) {
        super(canvas, kits, notices, area, graphSaved, "popup-graph",
                "Can't graph", "Drawing the graph of", "Graph of");
    }

    private static Supplier<GraphSession> shared(GraphSession kit) {
        return () -> kit;
    }

    JComponent graphCard() {
        return card();
    }

    void graph(String latex, Rectangle formulaWorld, FormulaCapture capture) {
        run(latex, formulaWorld, capture);
    }

    @Override
    Live newLive(GraphSession kit) {
        return new GraphLive(kit);
    }

    private final class GraphLive extends Live {
        private final GraphViewModel viewModel;

        GraphLive(GraphSession kit) {
            super(kit, new GraphCard(kit.viewModel(), kit.explore()));
            this.viewModel = kit.viewModel();
            viewModel.addPropertyChangeListener(e -> {
                if (GraphViewModel.STATE_PROPERTY.equals(e.getPropertyName())) {
                    stateChanged();
                } else if (GraphViewModel.VIEW_PROPERTY.equals(e.getPropertyName())) {
                    viewChanged();
                }
            });
        }

        @Override
        void execute(String latex, double[] xRange, double[] yRange) {
            kit.controller().execute(latex, xRange, yRange);
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
}
