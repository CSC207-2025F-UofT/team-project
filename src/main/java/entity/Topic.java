package entity;

/**
 * A main topic or chapter of a course, with the student's confidence in it.
 */
public class Topic {

    private final String name;
    private ConfidenceLevel confidence;

    /**
     * Creates a new topic with the given non-empty name and confidence level.
     * @param name the name of the topic
     * @param confidence how confident the student is with the topic
     * @throws IllegalArgumentException if the name is empty or the confidence is null
     */
    public Topic(String name, ConfidenceLevel confidence) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Topic name cannot be empty");
        }
        this.name = name.trim();
        setConfidence(confidence);
    }

    public String getName() {
        return name;
    }

    public ConfidenceLevel getConfidence() {
        return confidence;
    }

    /**
     * Changes the student's confidence in this topic.
     * @param confidence the new confidence level
     * @throws IllegalArgumentException if the confidence is null
     */
    public void setConfidence(ConfidenceLevel confidence) {
        if (confidence == null) {
            throw new IllegalArgumentException("Confidence level cannot be null");
        }
        this.confidence = confidence;
    }
}
