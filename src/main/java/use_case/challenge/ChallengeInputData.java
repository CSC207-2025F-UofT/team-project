package use_case.challenge;

/** Answer and Next requests carry the displayed attempt and question index to reject stale clicks. */
public final class ChallengeInputData {
    public enum Action { OPEN, SELECT, START, ANSWER, NEXT, LEAVE }

    public final Action action;
    public final String courseId;
    public final String attemptId;
    public final int questionIndex;
    public final int answerIndex;
    public final boolean confirmed;

    public ChallengeInputData(Action action, String courseId, String attemptId,
                              int questionIndex, int answerIndex, boolean confirmed) {
        this.action = java.util.Objects.requireNonNull(action);
        this.courseId = courseId;
        this.attemptId = attemptId;
        this.questionIndex = questionIndex;
        this.answerIndex = answerIndex;
        this.confirmed = confirmed;
    }
}
