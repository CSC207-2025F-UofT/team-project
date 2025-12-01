package entity;

public final class Quiz {
    private final int quizId;
    private final Question question;
    private boolean completed;
    private Integer userAnswerOptionId; // null until user selects something
    private Boolean answeredCorrectly;  // null until quiz is submitted

    public Quiz(int quizId, Question question) {
        this.quizId = quizId;
        this.question = question;
        this.completed = false;
        this.userAnswerOptionId = null;
        this.answeredCorrectly = null;
    }

    public int getQuizId() { return quizId; }
    public Question getQuestion() { return question; }
    public boolean isCompleted() { return completed; }
    public Integer getUserAnswerOptionId() { return userAnswerOptionId; }
    public Boolean getAnsweredCorrectly() { return answeredCorrectly; }

    // when user selects answer option
    public void recordAnswer(int optionId) {
        // // for checking during programming process
        assert question.getOptionById(optionId) != null : "Option does not belong to this question.";

        this.userAnswerOptionId = optionId;
    }

    // unfinished or finished quiz
    public QuizResult submit() {
        if (userAnswerOptionId == null) {
            return QuizResult.warning("Question Not Answered");
        }

        boolean correct = (userAnswerOptionId == question.getCorrectOptionId());
        completed = true;
        answeredCorrectly = correct;

        return correct
                ? QuizResult.correct("Correct Answer")
                : QuizResult.incorrect("Incorrect Answer");
    }
}
