package use_case.question_input;

/**
 * Output Data for the Question Input (Guess Input) Use Case.
 */
public class QuestionInputOutputData {
    private final Boolean correctAnswer;
    private final Integer pointsAwarded;
    // these 3 are for the log_input basically
    private final String teamName;
    private final String questionText;
    private final String guess;

    public QuestionInputOutputData(Boolean correctAnswer, Integer pointsAwarded, String teamName, String questionText, String guess) {
        this.correctAnswer = correctAnswer;
        this.pointsAwarded = pointsAwarded;
        this.teamName = teamName;
        this.questionText = questionText;
        this.guess = guess;
    }

    public Boolean getCorrectAnswer() {
        return correctAnswer;
    }
    public Integer getPointsAwarded() {
        return pointsAwarded;
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
}
