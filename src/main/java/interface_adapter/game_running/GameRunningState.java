package interface_adapter.game_running;

/**
 * The state information representing the current game that is running.
 */
public class GameRunningState {
    private String questionText = "";
    private int answerCount = 0;
    private String loadQuestionError = null;
    private String questionInputError = null;
    private String team1Name = "";
    private String team2Name = "";

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
  
    public String getTeam1Name() { return team1Name; }

    public String getTeam2Name() { return team2Name; }

    public void setTeam1Name(String team1Name) {this.team1Name = team1Name; }

    public void setTeam2Name(String team2Name) {this.team2Name = team2Name; }
}
