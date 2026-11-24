package use_case.log_input;

/**
 * Input Data for the Log Input Use Case.
 */
public class LogInputInputData {
    private final String teamName;
    private final String questionText;
    private final String guess;
    private final boolean correct;
    private final int pointsAwarded;

    public LogInputInputData(String teamName, String questionText, String guess, boolean correct, int pointsAwarded) {
        this.teamName = teamName;
        this.questionText = questionText;
        this.guess = guess;
        this.correct = correct;
        this.pointsAwarded = pointsAwarded;
    }
    public String getTeamName() {
        return teamName;
    }
    public String getQuestionText() {
        return questionText;
    }
    public String getGuess() {
        return guess;
    }
    public boolean isCorrect() {
        return correct;
    }
    public int getPointsAwarded() {
        return pointsAwarded;
    }
}
