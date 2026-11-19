package pdf_editor.adapter;

import history.domain.HistoryEntry;
import history.use_case.HistoryInteractor;
import pdf_editor.MathLibrary;
import pdf_editor.domain.PdfMaterial;
import pdf_editor.domain.PdfDocument;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * Coordinates edits across PDF placements, the in-memory Library and durable History.
 * Uses the existing public History and US2 APIs; does not depend on Canvas internals.
 *
 * UI launches {@link #recalculate(String)} on a worker, and applies the completed edit
 * back on the Swing event thread so Library listeners see a consistent final value.
 */
public final class FormulaRevisionCoordinator {
    private final MathLibrary library;
    private final HistoryInteractor history; // optional for independent PDF-only use
    private final Function<String, String> calculator;

    public FormulaRevisionCoordinator(MathLibrary library, HistoryInteractor history,
                                      Function<String, String> calculator) {
        this.library = Objects.requireNonNull(library, "library");
        this.history = history;
        this.calculator = Objects.requireNonNull(calculator, "calculator");
    }

    public String recalculate(String latex) {
        if (latex == null || latex.isBlank()) throw new IllegalArgumentException("Enter a formula first");
        String result = calculator.apply(latex.trim());
        return result == null ? "" : result.trim();
    }

    /** Update all PDF copies of the selected material as one Undo operation. */
    public void editPdf(PdfEditorController pdf, PdfMaterial previous, String formula, String result)
            throws IOException {
        Objects.requireNonNull(pdf, "pdf");
        Objects.requireNonNull(previous, "previous");
        validate(formula, result);
        if (previous.formula().equals(formula) && previous.result().equals(result)) return;
        if (pdf.selected() == null || !pdf.selected().material().id().equals(previous.id()))
            throw new IllegalStateException("The PDF selection changed while recalculating");
        reviseHistory(previous.formula(), previous.result(), formula, result);
        if (pdf.reviseSelectedFormula(formula, result) == 0)
            throw new IllegalStateException("The selected PDF formula is no longer present");
        updateLibrary(previous, formula, result);
    }

    /** A Library edit also refreshes the document placements linked by material ID. */
    public void editLibrary(PdfEditorController pdf, PdfMaterial previous, String formula, String result)
            throws IOException {
        Objects.requireNonNull(pdf, "pdf");
        Objects.requireNonNull(previous, "previous");
        validate(formula, result);
        if (previous.formula().equals(formula) && previous.result().equals(result)) return;
        reviseHistory(previous.formula(), previous.result(), formula, result);
        pdf.refreshMaterial(previous.id(), formula, result);
        updateLibrary(previous, formula, result);
    }

    /** Called after editing an already placed Canvas History item via its own menu. */
    public void editCanvasHistory(PdfEditorController pdf, String oldFormula, String oldResult,
                                  String formula, String result) throws IOException {
        validate(formula, result);
        if (oldFormula.equals(formula) && oldResult.equals(result)) return;
        reviseHistory(oldFormula, oldResult, formula, result);
        // Canvas History assets do not carry Library IDs. Match only exact old content.
        for (MathLibrary.Material item : library.items()) {
            if (!item.isGraph() && item.formula().equals(oldFormula) && item.result().equals(oldResult))
                pdf.refreshMaterial(item.id(), formula, result);
        }
        if (updateLibrary(null, oldFormula, oldResult, formula, result) == 0) {
            if (result.isBlank()) library.addFormula(formula);
            else library.addCalculation(formula, result);
        }
    }

    /** Keep Library/History consistent when the user undoes or redoes a formula edit.
     * Position/layer/size Undo gestures do not produce revisions. */
    public void reconcileDocumentUndo(PdfDocument before, PdfDocument after) throws IOException {
        Objects.requireNonNull(before, "before");
        Objects.requireNonNull(after, "after");
        java.util.Map<String, PdfMaterial> now = new java.util.HashMap<>();
        for (var page : after.pages()) for (var placement : page) {
            if (!placement.isNote() && !placement.material().isGraph())
                now.put(placement.id(), placement.material());
        }
        java.util.Set<String> handled = new java.util.HashSet<>();
        for (var page : before.pages()) for (var placement : page) {
            if (placement.isNote() || placement.material().isGraph()) continue;
            PdfMaterial old = placement.material();
            PdfMaterial current = now.get(placement.id());
            if (current == null || !current.id().equals(old.id()) ||
                    !handled.add(old.id())) continue;
            if (old.formula().equals(current.formula()) && old.result().equals(current.result())) continue;
            reviseHistory(old.formula(), old.result(), current.formula(), current.result());
            updateLibrary(old, current.formula(), current.result());
        }
    }

    private void updateLibrary(PdfMaterial previous, String formula, String result) {
        int updated = updateLibrary(previous, previous.formula(), previous.result(), formula, result);
        if (updated == 0) {
            if (result.isBlank()) library.addFormula(formula);
            else library.addCalculation(formula, result);
        }
    }

    private int updateLibrary(PdfMaterial previous, String oldFormula, String oldResult,
                              String formula, String result) {
        return library.reviseFormula(previous == null ? "" : previous.id(),
                oldFormula, oldResult, formula, result);
    }

    /**
     * History is deliberately immutable. A revision is stored first, then its unique
     * previous version is retired via the existing public History API. This avoids
     * editing another team's repository implementation or bypassing its notifications.
     */
    private void reviseHistory(String oldFormula, String oldResult, String formula, String result)
            throws IOException {
        if (history == null) return;
        List<HistoryEntry> old = history.list().stream()
                .filter(e -> !e.kind().equals(HistoryEntry.Kind.GRAPH))
                .filter(e -> e.formula().equals(oldFormula.trim())
                        && e.result().equals(oldResult == null ? "" : oldResult.trim()))
                .toList();
        if (result.isBlank()) history.recordFormula(formula);
        else history.recordCalculation(formula, result);
        // Retire a previous revision only if uniquely identified. Ambiguous records
        // stay as historical items rather than being deleted speculatively.
        if (old.size() == 1 && !(oldFormula.trim().equals(formula.trim())
                && (oldResult == null ? "" : oldResult.trim()).equals(result.trim()))) {
            history.delete(old.get(0).id());
        }
    }

    private static void validate(String formula, String result) {
        Objects.requireNonNull(formula, "formula");
        Objects.requireNonNull(result, "result");
        if (formula.isBlank()) throw new IllegalArgumentException("Formula cannot be empty");
    }
}
