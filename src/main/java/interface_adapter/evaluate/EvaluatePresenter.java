package interface_adapter.evaluate;

import interface_adapter.AnswerKind;
import interface_adapter.MathLabState;
import interface_adapter.MathLabViewModel;
import interface_adapter.TextRun;
import use_case.evaluate.EvaluateOutputBoundary;
import use_case.evaluate.ExprOutputData;
import use_case.evaluate.FormulaShape;
import use_case.evaluate.IntervalOutputData;
import use_case.evaluate.InvalidOutputData;
import use_case.evaluate.UncalculableOutputData;
import use_case.evaluate.ValueOutputData;

import java.util.List;

// just fills the view model, text comes from AnswerFormatter
public class EvaluatePresenter implements EvaluateOutputBoundary {
    private static final int MAX_ANSWER_LENGTH = 60;  // derivative longer than this wont fit, numbers cant get there

    private final MathLabViewModel viewModel;
    private AnswerKind kind = AnswerKind.RIGHT;

    public EvaluatePresenter(MathLabViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareFormulaShape(FormulaShape shape) {
        kind = AnswerKind.of(shape);
    }

    @Override
    public void prepareValuesView(List<ValueOutputData> values) {
        showResult(AnswerFormatter.values(values));
    }

    @Override
    public void prepareSolutionsView(String variable, List<Double> solutions) {
        showResult(AnswerFormatter.solutions(variable, solutions));
    }

    @Override
    public void prepareIntervalsView(String variable, List<IntervalOutputData> intervals) {
        showResult(AnswerFormatter.intervals(variable, intervals));
    }

    @Override
    public void prepareExcludedView(String variable, List<Double> excluded) {
        showResult(AnswerFormatter.excluded(variable, excluded));
    }

    @Override
    public void prepareAlwaysTrueView(String variable) {
        showResult(AnswerFormatter.alwaysTrue(variable));
    }

    @Override
    public void prepareFunctionDefinedView(String definition) {
        publish(AnswerFormatter.defined(), AnswerFormatter.defined(), "", "", "", definition);
    }

    @Override
    public void prepareExpressionView(ExprOutputData expression) {
        List<TextRun> runs = ExprWriter.runs(expression);
        if (TextRun.plain(runs).length() > MAX_ANSWER_LENGTH) {
            fail(UncalculableText.TITLE, UncalculableText.answerTooLong());
            return;
        }
        String result = "= " + ExprWriter.latex(expression);
        publish(result, TextRun.plain(AnswerFormatter.canvasRuns(kind, runs)), CanvasLatex.ofLatex(kind, result), "",
                "", null);
    }

    @Override
    public void prepareUncalculableView(UncalculableOutputData why) {
        fail(UncalculableText.TITLE, UncalculableText.of(why.getReason(), why.getDetail()));
    }

    @Override
    public void prepareInvalidView(InvalidOutputData why) {
        fail(InvalidText.TITLE, InvalidText.of(why.getProblem(), why.getDetails()));
    }

    // title on top, reason under it
    private void fail(String title, String reason) {
        publish("", "", "", title, reason, null);
    }

    private void showResult(String text) {
        publish(text, AnswerFormatter.canvasText(kind, text), CanvasLatex.ofText(kind, text), "", "", null);
    }

    // answerLatex "" = nothing to typeset, definedLatex null = not a definition
    private void publish(String resultText, String answerText, String answerLatex, String errorTitle,
                         String errorText, String definedLatex) {
        MathLabState state = viewModel.getState();
        state.setResultText(resultText);
        state.setAnswerKind(kind);
        state.setAnswerText(answerText);
        state.setAnswerLatex(answerLatex);
        state.setErrorTitle(errorTitle);
        state.setErrorText(errorText);
        state.setDefinition(definedLatex != null);
        state.setDefinedLatex(definedLatex == null ? "" : definedLatex);
        viewModel.firePropertyChanged(MathLabViewModel.RESULT);
    }
}
