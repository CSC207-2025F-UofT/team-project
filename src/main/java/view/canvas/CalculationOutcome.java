package view.canvas;

import interface_adapter.AnswerKind;

// what one Calculate came back with
final class CalculationOutcome {
    final String result;
    final AnswerKind kind;
    final String answerText;
    final String answerLatex;  // typeset on the canvas, "" = nothing to draw
    final String errorTitle;
    final String error;  // reason only, empty = no error
    final boolean definition;
    final String definedLatex;  // f(x) = ... when definition

    CalculationOutcome(String result, AnswerKind kind, String answerText, String answerLatex,
                       String errorTitle, String error, boolean definition, String definedLatex) {
        this.result = result;
        this.kind = kind;
        this.answerText = answerText;
        this.answerLatex = answerLatex;
        this.errorTitle = errorTitle;
        this.error = error;
        this.definition = definition;
        this.definedLatex = definedLatex;
    }

    static CalculationOutcome failed(String errorTitle, String error) {
        return new CalculationOutcome("", AnswerKind.RIGHT, "", "", errorTitle, error, false, "");
    }
}
