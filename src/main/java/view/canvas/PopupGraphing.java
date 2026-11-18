package view.canvas;

import interface_adapter.graph.AxisRangeParser;
import interface_adapter.graph.GraphController;
import interface_adapter.graph.GraphExploreController;
import interface_adapter.graph.GraphState;
import interface_adapter.graph.GraphViewModel;
import view.FormulaCapture;
import view.graph.GraphCard;
import view.graph.GraphSnapshot;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;

// Graph from the popup: uses the latex already read, live card goes beside that formula
final class PopupGraphing {
    private static final String CANT_GRAPH = "Can't graph";
    private static final int RANGE_GAP = 6;
    // first picture size, sets the card's shape; later ones follow the card
    static final int SNAPSHOT_WIDTH = 880;
    static final int SNAPSHOT_HEIGHT = 600;
    private static final int SNAPSHOT_DELAY_MS = 250;

    private final SharedCanvas canvas;
    private final GraphController controller;
    private final GraphViewModel viewModel;
    private final Notices notices;
    private final JComponent area;
    private final BiConsumer<FormulaCapture, String> graphSaved;
    private FormulaCapture pendingCapture;
    private final GraphRangeBar ranges;
    private final GraphCard card;
    private final Object tag = new Object();   // marks the canvas card as ours
    private final Timer snapshotLater;         // pan / zoom settle -> refresh the saved picture
    private final ExecutorService executor = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "popup-graph");
        t.setDaemon(true);
        return t;
    });
    private String lastLatex;
    private Rectangle lastFormula;   // world coords
    private Runnable drawing = () -> { };  // takes the Drawing… card down
    private Rectangle pending;       // where the card goes when the graph comes back
    private double[] shownWindow;    // last window written into the range boxes

    PopupGraphing(SharedCanvas canvas, GraphController controller, GraphExploreController explore,
                  GraphViewModel viewModel, Notices notices, JComponent area,
                  BiConsumer<FormulaCapture, String> graphSaved) {
        this.canvas = canvas;
        this.controller = controller;
        this.viewModel = viewModel;
        this.notices = notices;
        this.area = area;
        this.graphSaved = java.util.Objects.requireNonNull(graphSaved);
        this.ranges = new GraphRangeBar(this::regraph);
        this.card = new GraphCard(viewModel, explore);
        this.card.setHost(new CardHost());
        this.snapshotLater = new Timer(SNAPSHOT_DELAY_MS, e -> refreshSnapshot());
        this.snapshotLater.setRepeats(false);
        viewModel.addPropertyChangeListener(this::changed);
        canvas.addViewListener(this::placeRanges);
        canvas.addUserActionListener(() -> SwingUtilities.invokeLater(this::placeRanges));
        canvas.getDrawingCanvas().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                placeRanges();  // undo / clear may have taken the card
            }
        });
    }

    JComponent rangeBar() {
        return ranges;
    }

    // the live graph that sits over the canvas card
    JComponent graphCard() {
        return card;
    }

    void setRangeText(String x, String y) {
        ranges.setRangeText(x, y);
    }

    void graph(String latex, Rectangle formulaWorld, FormulaCapture capture) {
        pendingCapture = capture;
        lastLatex = latex;
        lastFormula = formulaWorld;
        run(latex, formulaWorld);
    }

    // Enter in a range box: same formula, new range
    private void regraph() {
        if (lastLatex != null) {
            run(lastLatex, lastFormula);
        }
    }

    private void run(String latex, Rectangle formulaWorld) {
        double[] x;
        double[] y;
        try {
            x = AxisRangeParser.parse(ranges.xText(), "x");
            y = AxisRangeParser.parse(ranges.yText(), "y");
        } catch (IllegalArgumentException e) {
            notices.error(CANT_GRAPH, e.getMessage());
            return;
        }
        pending = formulaWorld;
        drawing.run();
        drawing = notices.progress("Drawing the graph of", latex);
        executor.execute(() -> {
            try {
                controller.execute(latex, x, y);  // answer comes through the view model
            } catch (RuntimeException e) {
                String reason = e.getMessage() == null ? e.toString() : e.getMessage();
                SwingUtilities.invokeLater(() -> {
                    drawing.run();
                    notices.error(CANT_GRAPH, reason);
                });
            }
        });
    }

    private void changed(PropertyChangeEvent event) {
        if (GraphViewModel.STATE_PROPERTY.equals(event.getPropertyName())) {
            SwingUtilities.invokeLater(() -> show(viewModel.getState()));
        } else if (GraphViewModel.VIEW_PROPERTY.equals(event.getPropertyName())) {
            SwingUtilities.invokeLater(this::syncRangeText);
        }
    }

    private void show(GraphState state) {
        drawing.run();
        if (state.getError() != null) {
            notices.error(CANT_GRAPH, state.getError());
        } else if (state.hasGraph()) {
            BufferedImage first = GraphSnapshot.render(state, SNAPSHOT_WIDTH, SNAPSHOT_HEIGHT);
            canvas.getDrawingCanvas().showGraph(first, pending, tag);
            // Explicit, successful Graph is the publication boundary for formula-only material.
            // Regraphing a range does not create duplicates: the source revision is tracked by the page.
            if (pendingCapture != null && state.getLatex() != null
                    && state.getLatex().equals(lastLatex)) {
                graphSaved.accept(pendingCapture, state.getLatex());
            }
            notices.success("Graph of", state.getLatex());
            placeRanges();
            refreshSnapshot();
        }
    }

    // the boxes follow the window after a pan / zoom, so typing in them starts from what you see
    private void syncRangeText() {
        GraphState s = viewModel.getState();
        if (!s.hasGraph() || !ours()) {
            return;
        }
        double[] window = {s.getXMin(), s.getXMax(), s.getYMin(), s.getYMax()};
        if (java.util.Arrays.equals(window, shownWindow)) {
            return;   // just a hover, window didn't move
        }
        shownWindow = window;
        ranges.showRange(s.getXMin(), s.getXMax(), s.getYMin(), s.getYMax(),
                s.getKind() == GraphState.Kind.CURVE);
    }

    private boolean ours() {
        return canvas.getDrawingCanvas().getGraphTag() == tag;
    }

    // canvas keeps a picture of the card for saving / pdf, redo it at 2x so it's sharp
    private void refreshSnapshot() {
        Rectangle2D on = canvas.getDrawingCanvas().getGraphCardOnScreen();
        if (on == null || !ours()) {
            return;
        }
        int w = (int) Math.max(2, Math.round(on.getWidth() * 2));
        int h = (int) Math.max(2, Math.round(on.getHeight() * 2));
        canvas.getDrawingCanvas().replaceGraphImage(card.snapshot(w, h));
    }

    // card + range boxes ride along with pan and zoom of the canvas
    void placeRanges() {
        Rectangle2D on = canvas.getDrawingCanvas().getGraphCardOnScreen();
        if (on == null || !ours()) {
            hide(card);
            hide(ranges);
            return;
        }
        Rectangle at = SwingUtilities.convertRectangle(canvas.getDrawingCanvas(), new Rectangle(
                (int) Math.round(on.getX()), (int) Math.round(on.getY()),
                (int) Math.round(on.getWidth()), (int) Math.round(on.getHeight())), area);
        card.setBounds(at);
        card.setVisible(true);
        Dimension d = ranges.getPreferredSize();
        ranges.setBounds(new Rectangle(at.x, at.y + at.height + RANGE_GAP, d.width, d.height));
        ranges.setVisible(true);
        ranges.revalidate();
        area.repaint();
    }

    private void hide(JComponent c) {
        if (c.isVisible()) {
            c.setVisible(false);
            area.repaint();
        }
    }

    // what the card asks the canvas to do
    private final class CardHost implements GraphCard.Host {
        @Override
        public void moveBy(double dx, double dy) {
            canvas.getDrawingCanvas().moveGraphBy(dx, dy);
            placeRanges();
        }

        @Override
        public void resizeTo(double width, double height) {
            canvas.getDrawingCanvas().resizeGraphTo(width, height);
            placeRanges();
            snapshotLater.restart();
        }

        @Override
        public void close() {
            canvas.getDrawingCanvas().removeGraph();
            placeRanges();
        }

        @Override
        public void viewChanged() {
            snapshotLater.restart();
        }
    }
}
