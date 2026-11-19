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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * A popup button that puts live cards beside formulas (Graph, Phase Portrait). Every graph gets its
 * own {@link Live}: its own use-case session (a "kit" of controller + view model), its own card
 * floating over its canvas card, and its own x / y boxes in that card's header. So several graphs can
 * be on the canvas at once and each one pans, zooms and re-ranges on its own. The canvas keeps a
 * picture of each card for saving.
 *
 * <p>Given one shared kit instead of a factory, there is only ever one live card: a new graph
 * replaces it, as before multiple graphs were supported.
 *
 * @param <K> the per-graph kit (controllers + view model) the subclass runs its use case with
 */
abstract class PopupCardTool<K> {
    private static final int RANGE_GAP = 6;
    // first picture size, sets the card's shape; later ones follow the card
    static final int SNAPSHOT_WIDTH = 880;
    static final int SNAPSHOT_HEIGHT = 600;
    private static final int SNAPSHOT_DELAY_MS = 250;

    /** Where cards and their range boxes go: under the floating controls, over the ink. */
    interface Layers {
        void underlay(JComponent card);

        void overlay(JComponent ranges);
    }

    private final SharedCanvas canvas;
    private final Notices notices;
    private final JComponent area;
    private final BiConsumer<FormulaCapture, String> saved;
    private final Supplier<K> kits;
    private final ExecutorService executor;
    private final String failTitle;
    private final String progressText;
    private final String successText;
    private final List<Live> lives = new ArrayList<>();
    private Live latest;              // the one that ran last
    private Layers layers;
    private String[] presetRange;     // typed before the next graph exists (tests, restore)
    private Runnable onShown = () -> { };

    PopupCardTool(SharedCanvas canvas, Supplier<K> kits, Notices notices, JComponent area,
                  BiConsumer<FormulaCapture, String> saved, String thread, String failTitle,
                  String progressText, String successText) {
        this.canvas = canvas;
        this.kits = Objects.requireNonNull(kits);
        this.notices = notices;
        this.area = area;
        this.saved = Objects.requireNonNull(saved);
        this.failTitle = failTitle;
        this.progressText = progressText;
        this.successText = successText;
        this.layers = new Layers() {
            @Override
            public void underlay(JComponent card) {
                area.add(card);
            }

            @Override
            public void overlay(JComponent ranges) {
                area.add(ranges, 0);
            }
        };
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
                placeRanges();  // undo / clear / × may have taken a card
            }
        });
    }

    // a kit -> its live card, per tool
    abstract Live newLive(K kit);

    /** The page decides where cards go (under its docks); set before the first graph. */
    void setLayers(Layers layers) {
        this.layers = Objects.requireNonNull(layers);
        for (Live live : lives) {
            layers.underlay(live.card);
            layers.overlay(live.ranges);
        }
    }

    // the newest card (null before the first graph); tests and the single-card page use it
    JComponent card() {
        return latest == null ? null : latest.card;
    }

    JComponent rangeBar() {
        return latest == null ? null : latest.ranges;
    }

    // how many live cards exist (shown or waiting for reuse)
    int liveCount() {
        return lives.size();
    }

    void setRangeText(String x, String y) {
        if (latest != null && !latest.onCanvas()) {
            latest.ranges.setRangeText(x, y);
        }
        presetRange = new String[]{x, y};
    }

    // someone else's card may need to know when ours shows up
    void setOnShown(Runnable onShown) {
        this.onShown = onShown == null ? () -> { } : onShown;
    }

    void run(String latex, Rectangle formulaWorld, FormulaCapture capture) {
        Live live = liveForNewGraph();
        if (presetRange != null) {
            live.ranges.setRangeText(presetRange[0], presetRange[1]);
            presetRange = null;
        }
        latest = live;
        live.pendingCapture = capture;
        live.lastLatex = latex;
        live.lastFormula = formulaWorld;
        live.start(latex, formulaWorld, false);
    }

    /**
     * A new graph gets a card of its own. An idle card (closed, or never shown because its run
     * failed) is reused before asking for a fresh kit; a kit already in use (one shared kit) means
     * that card is replaced.
     */
    private Live liveForNewGraph() {
        for (Live live : lives) {
            if (!live.onCanvas() && !live.busy) {
                return live;
            }
        }
        K kit = kits.get();
        for (Live live : lives) {
            if (live.kit == kit) {
                return live;
            }
        }
        Live live = newLive(kit);
        lives.add(live);
        layers.underlay(live.card);
        layers.overlay(live.ranges);
        return live;
    }

    // every card + its range boxes ride along with pan and zoom of the canvas
    void placeRanges() {
        for (Live live : lives) {
            live.place();
        }
    }

    /** One graph on the canvas: its own session, live card and x / y boxes. */
    abstract class Live {
        final K kit;
        final PlotCard card;
        final GraphRangeBar ranges;
        private final Timer snapshotLater;   // pan / zoom settle -> refresh the saved picture
        private FormulaCapture pendingCapture;
        private String lastLatex;
        private Rectangle lastFormula;       // world coords
        private Rectangle pending;           // where the card goes when the answer comes back
        private boolean rerun;               // Enter in a range box: keep the card where it is
        private volatile boolean busy;
        private double[] shownWindow;        // last window written into the range boxes
        private Runnable drawing = () -> { };  // takes the Drawing… card down

        Live(K kit, PlotCard card) {
            this.kit = kit;
            this.card = card;
            this.ranges = new GraphRangeBar(this::rerun);
            this.card.setHost(new CardHost());
            this.snapshotLater = new Timer(SNAPSHOT_DELAY_MS, e -> refreshSnapshot());
            this.snapshotLater.setRepeats(false);
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

        String sourceLatex() {
            return latex();
        }

        // ----

        boolean onCanvas() {
            return canvas.getDrawingCanvas().hasGraph(this);
        }

        // Enter in a range box: same formula, new range, same spot
        private void rerun() {
            if (lastLatex != null) {
                start(lastLatex, lastFormula, true);
            }
        }

        private void start(String latex, Rectangle formulaWorld, boolean again) {
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
            rerun = again;
            busy = true;
            drawing.run();
            drawing = notices.progress(progressText, latex);
            executor.execute(() -> {
                try {
                    execute(latex, x, y);
                } catch (RuntimeException e) {
                    String reason = e.getMessage() == null ? e.toString() : e.getMessage();
                    SwingUtilities.invokeLater(() -> {
                        busy = false;
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
            busy = false;
            drawing.run();
            if (error() != null) {
                notices.error(failTitle, error());
            } else if (hasContent()) {
                if (rerun && onCanvas()) {
                    canvas.getDrawingCanvas().replaceGraphImage(this, firstPicture());
                } else {
                    canvas.getDrawingCanvas().showGraph(firstPicture(), pending, this);
                }
                rerun = false;
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

        // the boxes follow the window after a pan / zoom, so typing in them starts from what you see
        private void syncRangeText() {
            if (!hasContent() || !onCanvas()) {
                return;
            }
            double[] window = window();
            if (Arrays.equals(window, shownWindow)) {
                return;   // just a hover, window didn't move
            }
            shownWindow = window;
            ranges.showRange(window[0], window[1], window[2], window[3], followsY());
        }

        // canvas keeps a picture of the card for saving / pdf, redo it at 2x so it's sharp
        private void refreshSnapshot() {
            Rectangle2D on = canvas.getDrawingCanvas().getGraphCardOnScreen(this);
            if (on == null) {
                return;
            }
            int w = (int) Math.max(2, Math.round(on.getWidth() * 2));
            int h = (int) Math.max(2, Math.round(on.getHeight() * 2));
            canvas.getDrawingCanvas().replaceGraphImage(this, card.snapshot(w, h));
        }

        // card + range boxes over this graph's canvas card
        private void place() {
            Rectangle2D on = canvas.getDrawingCanvas().getGraphCardOnScreen(this);
            if (on == null) {
                hide(card);
                hide(ranges);
                return;
            }
            Rectangle at = SwingUtilities.convertRectangle(canvas.getDrawingCanvas(), new Rectangle(
                    (int) Math.round(on.getX()), (int) Math.round(on.getY()),
                    (int) Math.round(on.getWidth()), (int) Math.round(on.getHeight())), area);
            card.setBounds(at);
            card.setVisible(true);
            // boxes go in the header right after the function, at the header's scale
            Rectangle slot = ranges.placeIn(card);
            if (slot != null) {
                ranges.setBounds(new Rectangle(at.x + slot.x, at.y + slot.y, slot.width, slot.height));
            } else {
                // card too small to share the header: hang under it as before
                Dimension d = ranges.getPreferredSize();
                ranges.setBounds(new Rectangle(at.x, at.y + at.height + RANGE_GAP, d.width, d.height));
            }
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

        // what the card asks the canvas to do, for this graph only
        private final class CardHost implements PlotCard.Host {
            @Override
            public void moveBy(double dx, double dy) {
                canvas.getDrawingCanvas().moveGraphBy(Live.this, dx, dy);
                place();
            }

            @Override
            public void resizeTo(double width, double height) {
                canvas.getDrawingCanvas().resizeGraphTo(Live.this, width, height);
                place();
                snapshotLater.restart();
            }

            @Override
            public void close() {
                canvas.getDrawingCanvas().removeGraph(Live.this);
                place();
            }

            @Override
            public void viewChanged() {
                snapshotLater.restart();
            }
        }
    }
}
