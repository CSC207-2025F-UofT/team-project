package pdf_editor.view;

import view.DrawingCanvas;
import view.board.GlassContextMenu;
import view.glass.GlassDialogs;

import javax.swing.SwingWorker;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/** View-only history formula editor: LaTeX remains structured; the evaluator supplies the result. */
public final class CanvasHistoryEditMenu {
    private CanvasHistoryEditMenu() { }

    /** Compatibility overload for older callers; preserve the saved result if no evaluator is wired. */
    public static void install(DrawingCanvas canvas, BiFunction<String, String, BufferedImage> render) {
        install(canvas, render, latex -> canvas.selectedHistoryResult(), (latex, result) -> { });
    }

    public static void install(DrawingCanvas canvas,
                               BiFunction<String, String, BufferedImage> render,
                               Function<String, String> calculate) {
        install(canvas, render, calculate, (latex, result) -> { });
    }

    /** The optional Library command is injected; this view never manipulates repository state. */
    public static void install(DrawingCanvas canvas,
                               BiFunction<String, String, BufferedImage> render,
                               Function<String, String> calculate,
                               BiConsumer<String, String> addToLibrary) {
        Objects.requireNonNull(canvas);
        Objects.requireNonNull(render);
        Objects.requireNonNull(calculate);
        Objects.requireNonNull(addToLibrary);
        canvas.addMouseListener(new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e) { show(e); }
            @Override public void mouseReleased(MouseEvent e) { show(e); }
            private void show(MouseEvent e) {
                if (!e.isPopupTrigger() || !canvas.hasSelectedHistoryAsset()) return;
                GlassContextMenu menu = new GlassContextMenu();
                menu.addAction("Edit formula (recalculate)", () -> {
                    String before = canvas.selectedHistoryFormula();
                    String formula = GlassDialogs.input(canvas, "Edit formula", "LaTeX formula",
                            before, "Recalculate");
                    if (formula == null || formula.isBlank()) return;
                    if (formula.trim().equals(before)) return;
                    // Keep mathematical work off the Swing event-dispatch thread.
                    new SwingWorker<String, Void>() {
                        @Override protected String doInBackground() { return calculate.apply(formula.trim()); }
                        @Override protected void done() {
                            try {
                                String result = get();
                                if (result == null) result = "";
                                // Ignore a stale async completion after changing selection.
                                if (before.equals(canvas.selectedHistoryFormula())) {
                                    canvas.editHistoryAsset(formula, result, render.apply(formula, result));
                                }
                            } catch (Exception failure) {
                                GlassDialogs.error(canvas, "Could not recalculate: " +
                                        (failure.getCause() != null ? failure.getCause().getMessage() : failure.getMessage()));
                            }
                        }
                    }.execute();
                });
                menu.addAction("Add to Library", () -> addToLibrary.accept(
                        canvas.selectedHistoryFormula(), canvas.selectedHistoryResult()));
                menu.addAction("Delete selected item", canvas::deleteSelection);
                menu.show(canvas, e.getX(), e.getY());
            }
        });
    }
}
