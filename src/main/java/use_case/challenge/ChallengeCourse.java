package use_case.challenge;

import entity.ChallengeResult;
import java.util.Objects;
import java.util.Optional;
import use_case.question.QuestionTopic;

/** A read-only projection of a teammate-owned Course and CourseProgress. */
public final class ChallengeCourse {
    private final String id;
    private final String name;
    private final QuestionTopic topic;
    private final ChallengeResult result;

    public ChallengeCourse(String id, String name, QuestionTopic topic, ChallengeResult result) {
        if (id == null || id.isBlank() || name == null || name.isBlank()) {
            throw new IllegalArgumentException("A course requires its stable ID and display name");
        }
        this.id = id;
        this.name = name;
        this.topic = Objects.requireNonNull(topic);
        this.result = result;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public QuestionTopic getTopic() { return topic; }
    public Optional<ChallengeResult> getResult() { return Optional.ofNullable(result); }
}
