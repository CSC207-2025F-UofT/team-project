package interface_adapter;

import java.util.List;

public class MathLabState {
    private String expression = "";

    private String resultText = "";

    private String errorTitle = "";  // notice heading, e.g. Can't calculate

    private String errorText = "";  // just the reason under it

    private AnswerKind answerKind = AnswerKind.RIGHT;

    private String answerText = "";  // what gets drawn

    private List<TextRun> answerRuns;  // same w/ raised exponents, null for plain answers

    private boolean definition;  // f(x)= got saved, nothing to draw

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getResultText() {
        return resultText;
    }

    public void setResultText(String resultText) {
        this.resultText = resultText;
        this.answerRuns = null;
    }

    public AnswerKind getAnswerKind() {
        return answerKind;
    }

    public void setAnswerKind(AnswerKind answerKind) {
        this.answerKind = answerKind;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public List<TextRun> getAnswerRuns() {
        return answerRuns;
    }

    public void setAnswerRuns(List<TextRun> answerRuns) {
        this.answerRuns = answerRuns;
    }

    public boolean isDefinition() {
        return definition;
    }

    public void setDefinition(boolean definition) {
        this.definition = definition;
    }

    public String getErrorTitle() {
        return errorTitle;
    }

    public void setErrorTitle(String errorTitle) {
        this.errorTitle = errorTitle;
    }

    public String getErrorText() {
        return errorText;
    }

    public void setErrorText(String errorText) {
        this.errorText = errorText;
    }
}
