package view.canvas;

import interface_adapter.TextRun;
import interface_adapter.AnswerKind;

import java.util.List;

// what one Calculate came back with
final class CalculationOutcome {
    final String result;
    final AnswerKind kind;
    final String answerText;
    final List<TextRun> answerRuns;  // null = plain text
    final String errorTitle;
    final String error;  // reason only, empty = no error
    final boolean definition;

    CalculationOutcome(String result, AnswerKind kind, String answerText, List<TextRun> answerRuns,
                       String errorTitle, String error, boolean definition) {
        this.result = result;
        this.kind = kind;
        this.answerText = answerText;
        this.answerRuns = answerRuns;
        this.errorTitle = errorTitle;
        this.error = error;
        this.definition = definition;
    }

    static CalculationOutcome failed(String errorTitle, String error) {
        return new CalculationOutcome("", AnswerKind.RIGHT, "", null, errorTitle, error, false);
    }
}
