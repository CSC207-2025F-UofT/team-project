package use_case.load_question;

public class LoadQuestionOutputData {
    private final String questionText;
    private final int answerCount;

    public LoadQuestionOutputData(String questionText, int answerCount) {
        this.questionText = questionText;
        this.answerCount = answerCount;
    }

    public String getQuestionText() { return questionText; }
    public int getAnswerCount() { return answerCount; }
}
