package entity;

/** Completed preparation work for one course; it does not contain an exam grade. */
public final class ChallengeResult {
    private final int correctCount;
    private final int questionCount;

    public ChallengeResult(int correctCount, int questionCount) {
        if (questionCount <= 0 || correctCount < 0 || correctCount > questionCount) {
            throw new IllegalArgumentException("Invalid challenge result");
        }
        this.correctCount = correctCount;
        this.questionCount = questionCount;
    }

    public int getCorrectCount() { return correctCount; }
    public int getQuestionCount() { return questionCount; }
    public int getPreparationPoints() { return correctCount; }
}
