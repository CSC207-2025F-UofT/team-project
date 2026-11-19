package pdf_editor.use_case;

import java.util.Objects;
import java.util.function.Function;

/**
 * Application policy for a user-authored formula revision.
 * CALCULATE delegates to the injected evaluator; MANUAL stores the expression
 * verbatim without evaluation or asserting that the equation is true.
 */
public final class FormulaEditSubmission {
    public enum Mode { CALCULATE, MANUAL }
    public record Revision(String formula, String result) { }

    private FormulaEditSubmission() { }

    public static Revision submit(String input, Mode mode, Function<String, String> evaluator) {
        Objects.requireNonNull(input, "input");
        Objects.requireNonNull(mode, "mode");
        String formula = input.trim();
        if (formula.isEmpty()) throw new IllegalArgumentException("Formula cannot be empty");
        if (mode == Mode.MANUAL) return new Revision(formula, "");
        String evaluated = Objects.requireNonNull(evaluator, "evaluator").apply(formula);
        return new Revision(formula, evaluated == null ? "" : evaluated.trim());
    }
}
