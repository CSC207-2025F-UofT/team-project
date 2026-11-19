package pdf_editor.adapter;

import interface_adapter.MathLabState;
import interface_adapter.MathLabViewModel;
import interface_adapter.evaluate.EvaluateController;
import java.util.Objects;

/** Reuses US2's existing evaluate use case for editing a saved formula. */
public final class HistoryFormulaCalculator {
    private final EvaluateController controller;
    private final MathLabViewModel viewModel;

    public HistoryFormulaCalculator(EvaluateController controller, MathLabViewModel viewModel) {
        this.controller = Objects.requireNonNull(controller);
        this.viewModel = Objects.requireNonNull(viewModel);
    }

    /** Called by SwingWorker, never by an EDT painting or mouse handler. */
    public synchronized String calculate(String latex) {
        if (latex == null || latex.isBlank()) throw new IllegalArgumentException("Formula required");
        controller.execute(latex);
        MathLabState state = viewModel.getState();
        if (state.getErrorTitle() != null && !state.getErrorTitle().isBlank()) {
            throw new IllegalArgumentException(state.getErrorTitle()
                    + (state.getErrorText() == null || state.getErrorText().isBlank()
                       ? "" : ": " + state.getErrorText()));
        }
        return formattedResult(state);
    }

    public static String formattedResult(MathLabState state) {
        Objects.requireNonNull(state);
        if ((state.getErrorTitle() != null && !state.getErrorTitle().isBlank()) || state.isDefinition())
            return "";
        String latex = state.getAnswerLatex();
        if (latex != null && !latex.isBlank()) return latex.trim().replaceFirst("^=\\s*", "");
        String result = state.getResultText();
        return result == null ? "" : result.trim().replaceFirst("^=\\s*", "");
    }
}
