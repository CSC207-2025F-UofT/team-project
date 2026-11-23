package use_case.challenge;

import entity.ChallengeResult;
import java.util.List;

/** Question output omits the correct answer until feedback is requested. */
public final class ChallengeOutputData {
    public enum Stage { COURSES, CONFIRM, QUESTION, FEEDBACK, RESULT, LEFT }

    public final Stage stage;
    public final ChallengeOverview overview;
    public final ChallengeCourse course;
    public final String attemptId;
    public final int questionIndex;
    public final int questionCount;
    public final int energyCost;
    public final String questionText;
    public final List<String> options;
    public final String feedback;
    public final ChallengeResult result;

    public ChallengeOutputData(Stage stage, ChallengeOverview overview, ChallengeCourse course,
                               String attemptId, int questionIndex, int questionCount, int energyCost,
                               String questionText, List<String> options, String feedback,
                               ChallengeResult result) {
        this.stage = stage;
        this.overview = overview;
        this.course = course;
        this.attemptId = attemptId;
        this.questionIndex = questionIndex;
        this.questionCount = questionCount;
        this.energyCost = energyCost;
        this.questionText = questionText;
        this.options = List.copyOf(options);
        this.feedback = feedback;
        this.result = result;
    }
}
