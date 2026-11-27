package use_case.log_input;

/**
 * Output Data for the Log Input Use Case.
 */
public class LogInputOutputData {
    private final int round;
    private final String questionText;
    private final String teamName;
    private final String guess;
    private final boolean correct;
    private final int pointsAwarded;

    public LogInputOutputData(int round, String questionText, String teamName, String guess,
                              boolean correct, int pointsAwarded) {
        this.round = round;
        this.questionText = questionText;
        this.teamName = teamName;
        this.guess = guess;
        this.correct = correct;
        this.pointsAwarded = pointsAwarded;
    }

    public int getRound() {
        return round;
    }
    public String getQuestionText() {
        return questionText;
    }
    public String getTeamName() {
        return teamName;
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
