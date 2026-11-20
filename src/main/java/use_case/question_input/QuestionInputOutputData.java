package use_case.question_input;

/**
 * Output Data for the Question Input (Guess Input) Use Case.
 */
public class QuestionInputOutputData {
    private Boolean correctAnswer;
    private Integer pointsAwarded;

    public QuestionInputOutputData(Boolean correctAnswer, Integer pointsAwarded) {
        this.correctAnswer = correctAnswer;
        this.pointsAwarded = pointsAwarded;
    }
}
