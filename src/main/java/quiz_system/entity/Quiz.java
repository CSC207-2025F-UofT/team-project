package quiz_system.entity;

public final class Quiz {
    private final int quizId;
    private final Question question;
    private boolean completed;
    private Integer userAnswerOptionId; // null until user selects something

    public Quiz(int quizId, Question question) {
        this.quizId = quizId;
        this.question = question;
        this.completed = false;
        this.userAnswerOptionId = null;
    }

    public int getQuizId() { return quizId; }
    public Question getQuestion() { return question; }
    public boolean isCompleted() { return completed; }
    public Integer getUserAnswerOptionId() { return userAnswerOptionId; }

    /** Called when the user selects an answer option. */
    public void recordAnswer(int optionId) {
        // Option must belong to this question — this prevents bugs
        if (question.getOptionById(optionId) == null) {
            throw new IllegalArgumentException("Option does not belong to this question.");
        }
        this.userAnswerOptionId = optionId;
    }

    /** Applies the business rules: correct / incorrect / unanswered. */
    public QuizResult submit() {
        if (userAnswerOptionId == null) {
            return QuizResult.warning("Question Not Answered");
        }

        boolean correct = (userAnswerOptionId == question.getCorrectOptionId());
        completed = true;

        return correct
                ? QuizResult.correct("Correct Answer")
                : QuizResult.incorrect("Incorrect Answer");
    }
}
