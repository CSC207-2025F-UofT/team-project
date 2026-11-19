package view.canvas;

import interface_adapter.graph.AxisRangeParser;
import view.FormulaCapture;
import view.graph.PlotCard;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;

/**
 * A popup button that puts a live card beside the formula (Graph, Phase Portrait). The card floats
 * over the canvas's card slot, the x / y boxes hang under it, the canvas keeps a picture for saving.
 * Subclasses say how to run their use case and read their state.
 */
abstract class PopupCardTool {
    private static final int RANGE_GAP = 6;
    // first picture size, sets the card's shape; later ones follow the card
    static final int SNAPSHOT_WIDTH = 880;
    static final int SNAPSHOT_HEIGHT = 600;
    private static final int SNAPSHOT_DELAY_MS = 250;

    private final SharedCanvas canvas;
    private final Notices notices;
    private final JComponent area;
    private final BiConsumer<FormulaCapture, String> saved;
    private final GraphRangeBar ranges;
    private final PlotCard card;
    private final Object tag = new Object();   // marks the canvas card as ours
    private final Timer snapshotLater;         // pan / zoom settle -> refresh the saved picture
    private final ExecutorService executor;
    private final String failTitle;
    private final String progressText;
    private final String successText;
    private FormulaCapture pendingCapture;
    private String lastLatex;
    private Rectangle lastFormula;   // world coords
    private Runnable drawing = () -> { };  // takes the Drawing… card down
    private Rectangle pending;       // where the card goes when the answer comes back
    private double[] shownWindow;    // last window written into the range boxes
    private Runnable onShown = () -> { };

    PopupCardTool(SharedCanvas canvas, PlotCard card, Notices notices, JComponent area,
                  BiConsumer<FormulaCapture, String> saved, String thread, String failTitle,
                  String progressText, String successText) {
        this.canvas = canvas;
        this.card = card;
        this.notices = notices;
        this.area = area;
        this.saved = Objects.requireNonNull(saved);
        this.failTitle = failTitle;
        this.progressText = progressText;
        this.successText = successText;
        this.ranges = new GraphRangeBar(this::rerun);
        this.card.setHost(new CardHost());
        this.snapshotLater = new Timer(SNAPSHOT_DELAY_MS, e -> refreshSnapshot());
        this.snapshotLater.setRepeats(false);
        this.executor = Executors.newSingleThreadExecutor(r -> {
            Thread t = new Thread(r, thread);
            t.setDaemon(true);
            return t;
        });
        canvas.addViewListener(this::placeRanges);
        canvas.addUserActionListener(() -> SwingUtilities.invokeLater(this::placeRanges));
        canvas.getDrawingCanvas().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                placeRanges();  // undo / clear may have taken the card
            }
        });
    }

    // ---- the use case + its state, per tool

    // runs off the ui thread; the answer comes back through the view model
    abstract void execute(String latex, double[] xRange, double[] yRange);

    abstract String error();

    abstract boolean hasContent();

    abstract String latex();

    // {xMin, xMax, yMin, yMax}
    abstract double[] window();

    // 3d keeps the typed y box, the rest follow the window
    abstract boolean followsY();

    abstract BufferedImage firstPicture();

    // ----

    JComponent rangeBar() {
        return ranges;
    }

    // the live card that sits over the canvas card
    JComponent card() {
        return card;
    }

    void setRangeText(String x, String y) {
        ranges.setRangeText(x, y);
    }

    // someone else's card may need to step aside when ours shows up
    void setOnShown(Runnable onShown) {
        this.onShown = onShown == null ? () -> { } : onShown;
    }

    void run(String latex, Rectangle formulaWorld, FormulaCapture capture) {
        pendingCapture = capture;
        lastLatex = latex;
        lastFormula = formulaWorld;
        start(latex, formulaWorld);
    }

    // Enter in a range box: same formula, new range
    private void rerun() {
        if (lastLatex != null) {
            start(lastLatex, lastFormula);
        }
    }

    private void start(String latex, Rectangle formulaWorld) {
        double[] x;
        double[] y;
        try {
            x = AxisRangeParser.parse(ranges.xText(), "x");
            y = AxisRangeParser.parse(ranges.yText(), "y");
        } catch (IllegalArgumentException e) {
            notices.error(failTitle, e.getMessage());
            return;
        }
        pending = formulaWorld;
        drawing.run();
        drawing = notices.progress(progressText, latex);
        executor.execute(() -> {
            try {
                execute(latex, x, y);
            } catch (RuntimeException e) {
                String reason = e.getMessage() == null ? e.toString() : e.getMessage();
                SwingUtilities.invokeLater(() -> {
                    drawing.run();
                    notices.error(failTitle, reason);
                });
            }
        });
    }

    // view model said STATE: brand new card or an error
    final void stateChanged() {
        SwingUtilities.invokeLater(this::show);
    }

    // view model said VIEW: pan / zoom / trace
    final void viewChanged() {
        SwingUtilities.invokeLater(this::syncRangeText);
    }

    private void show() {
        drawing.run();
        if (error() != null) {
            notices.error(failTitle, error());
        } else if (hasContent()) {
            canvas.getDrawingCanvas().showGraph(firstPicture(), pending, tag);
            // Explicit, successful run is the publication boundary for formula-only material.
            // Re-running a range does not create duplicates: the source revision is tracked by the page.
            if (pendingCapture != null && Objects.equals(lastLatex, sourceLatex())) {
                saved.accept(pendingCapture, lastLatex);
            }
            notices.success(successText, latex());
            placeRanges();
            refreshSnapshot();
            onShown.run();
        }
    }

    // what the state says it drew from; tools that retitle (systems on one line) say the source here
    String sourceLatex() {
        return latex();
    }

    // the boxes follow the window after a pan / zoom, so typing in them starts from what you see
    private void syncRangeText() {
        if (!hasContent() || !ours()) {
            return;
        }
        double[] window = window();
        if (Arrays.equals(window, shownWindow)) {
            return;   // just a hover, window didn't move
        }
        shownWindow = window;
        ranges.showRange(window[0], window[1], window[2], window[3], followsY());
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
    private final class CardHost implements PlotCard.Host {
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
