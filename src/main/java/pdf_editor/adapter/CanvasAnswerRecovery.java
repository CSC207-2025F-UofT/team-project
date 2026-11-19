package pdf_editor.adapter;

import view.answer.CanvasAnswer;
import view.answer.Formula;

import javax.swing.Timer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Read-only compatibility adapter between Canvas answers and PDF/History persistence.
 *
 * The Canvas may publish a formula-only source during Graph and later draw a computed
 * answer without publishing it again (the older exporter deduplicates by revision).
 * This adapter observes only such pending, *already exported* sources. It never runs
 * recognition/calculation, changes Canvas state, or creates history for a preview.
 *
 * Runs on Swing's event thread; its timer runs only while a formula-only source is
 * pending and stops when the associated answer appears or the source is removed.
 */
public final class CanvasAnswerRecovery {
    @FunctionalInterface public interface ResultSink {
        void accept(long sourceId, String latex, String result);
    }
    private record Pending(String latex, long revision) { }
    private final Supplier<List<Formula>> formulaSource;
    private final ResultSink resultSink;
    private final Map<Long, Pending> pending = new HashMap<>();
    private Timer timer;

    public CanvasAnswerRecovery(Supplier<List<Formula>> formulaSource, ResultSink resultSink) {
        this.formulaSource = Objects.requireNonNull(formulaSource);
        this.resultSink = Objects.requireNonNull(resultSink);
    }

    /**
     * The answer worker exports readable text, while Canvas owns a correctly typeset
     * answer LaTeX. Reuse that public representation for PDF and History when it exists.
     */
    public String displayResult(long sourceId, String rawResult) {
        if (rawResult == null || rawResult.isBlank()) return rawResult;
        for (Formula formula : formulaSource.get()) {
            if (formula.getId() != sourceId) continue;
            CanvasAnswer answer = formula.getAnswer();
            if (answer != null && !answer.isError()
                    && answer.getLatex() != null && !answer.getLatex().isBlank()) {
                return answer.getLatex();
            }
            break;
        }
        return rawResult;
    }

    public void published(long id, String latex, String result) {
        if (latex == null || latex.isBlank()) return;
        if (result != null && !result.isBlank()) {
            pending.remove(id);
        } else {
            for (Formula formula : formulaSource.get()) {
                if (formula.getId() == id) {
                    pending.put(id, new Pending(latex, formula.getRevision()));
                    break;
                }
            }
        }
        recover();
        updateTimer();
    }

    public void synchronize(Set<Long> activeSourceIds) {
        Objects.requireNonNull(activeSourceIds);
        recover();
        pending.keySet().removeIf(id -> !activeSourceIds.contains(id));
        updateTimer();
    }

    private void recover() {
        if (pending.isEmpty()) return;
        for (Formula formula : formulaSource.get()) {
            Pending record = pending.get(formula.getId());
            if (record == null) continue;
            if (record.revision() != formula.getRevision()) {
                pending.remove(formula.getId());
                continue;
            }
            CanvasAnswer answer = formula.getAnswer();
            if (answer == null || answer.isError()) continue;
            String latex = answer.getLatex();
            String result = latex == null || latex.isBlank() ? answer.getText() : latex;
            if (result == null || result.isBlank()) continue;
            pending.remove(formula.getId());
            resultSink.accept(formula.getId(), record.latex(), result);
        }
    }

    private void updateTimer() {
        if (pending.isEmpty()) {
            if (timer != null) timer.stop();
            return;
        }
        if (timer == null) {
            timer = new Timer(1000, e -> {
                recover();
                updateTimer();
            });
        }
        if (!timer.isRunning()) timer.start();
    }

    /** Release the UI timer if the Canvas attachment is replaced. */
    public void dispose() {
        pending.clear();
        if (timer != null) timer.stop();
    }
}
