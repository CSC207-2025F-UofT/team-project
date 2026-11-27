package interface_adapter.game_running;

/**
 * The state information representing the current game that is running.
 */
public class GameRunningState {
    private String questionText = "";
    private int answerCount = 0;
    private String loadQuestionError = null;
    private String questionInputError = null;

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public int getAnswerCount() {
        return answerCount;
    }

    public void setAnswerCount(int answerCount) {
        this.answerCount = answerCount;
    }

    public String getLoadQuestionError() {
        return loadQuestionError;
    }

    public void setLoadQuestionError(String loadQuestionError) {
        this.loadQuestionError = loadQuestionError;
    }

    public String getQuestionInputError() {return questionInputError;}

    public void setQuestionInputError(String questionInputError) {this.questionInputError = questionInputError;}
}
