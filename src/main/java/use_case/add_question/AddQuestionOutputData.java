package use_case.add_question;

/**
 * Output Data object containing the result of adding a Question.
 */
public class AddQuestionOutputData {
    private final String questionText;
    private final int totalAnswersCount;
    private final String message;

    public AddQuestionOutputData(String questionText,
                                 int totalAnswersCount,
                                 String message) {
        this.questionText = questionText;
        this.totalAnswersCount = totalAnswersCount;
        this.message = message;
    }

    public String getQuestionText() {
        return questionText;
    }

    public int getTotalAnswersCount() {
        return totalAnswersCount;
    }

    public String getMessage() {
        return message;
    }
}
