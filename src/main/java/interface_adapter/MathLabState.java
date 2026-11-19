package interface_adapter;

public class MathLabState {
    private String expression = "";

    private String resultText = "";

    private String errorTitle = "";  // notice heading, e.g. Can't calculate

    private String errorText = "";  // just the reason under it

    private AnswerKind answerKind = AnswerKind.RIGHT;

    private String answerText = "";  // what gets drawn

    private String answerLatex = "";  // same answer as tex, typeset on the canvas

    private boolean definition;  // f(x)= got saved, nothing to draw

    private String definedLatex = "";  // the f(x) = ... that got saved, typeset in the card

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
        this.answerLatex = "";
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

    public String getAnswerLatex() {
        return answerLatex;
    }

    public void setAnswerLatex(String answerLatex) {
        this.answerLatex = answerLatex;
    }

    public boolean isDefinition() {
        return definition;
    }

    public void setDefinition(boolean definition) {
        this.definition = definition;
    }

    public String getDefinedLatex() {
        return definedLatex;
    }

    public void setDefinedLatex(String definedLatex) {
        this.definedLatex = definedLatex;
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
