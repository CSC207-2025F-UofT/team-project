package entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

/**
 * A course a student is enrolled in, with their target grade, topics and assessment marks.
 */
public class Course {

    private static final double MAX_PERCENTAGE = 100;
    /** Tolerance so that weights such as 33.3 + 33.3 + 33.4 are not rejected by rounding. */
    private static final double EPSILON = 1e-9;

    private final String code;
    private final String name;
    private LetterGrade targetGrade;
    private final List<Topic> topics = new ArrayList<>();
    private final List<Assessment> assessments = new ArrayList<>();

    /**
     * Creates a new course with no topics and no assessments.
     * @param code the non-empty course code, such as "CSC207"
     * @param name the non-empty course name
     * @param targetGrade the grade the student is aiming for
     * @throws IllegalArgumentException if the code or name is empty, or the target grade is null
     */
    public Course(String code, String name, LetterGrade targetGrade) {
        if (code == null || code.trim().isEmpty()) {
            throw new IllegalArgumentException("Course code cannot be empty");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Course name cannot be empty");
        }
        this.code = code.trim();
        this.name = name.trim();
        setTargetGrade(targetGrade);
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public LetterGrade getTargetGrade() {
        return targetGrade;
    }

    /**
     * Changes the grade the student is aiming for.
     * @param targetGrade the new target grade
     * @throws IllegalArgumentException if the target grade is null
     */
    public void setTargetGrade(LetterGrade targetGrade) {
        if (targetGrade == null) {
            throw new IllegalArgumentException("Target grade cannot be null");
        }
        this.targetGrade = targetGrade;
    }

    /**
     * Returns the topics of this course, in the order they were added.
     * @return a read-only view of the topics
     */
    public List<Topic> getTopics() {
        return Collections.unmodifiableList(topics);
    }

    /**
     * Adds a topic to this course.
     * @param topic the topic to add
     * @throws IllegalArgumentException if the topic is null or the course
     *                                  already has a topic with the same name
     */
    public void addTopic(Topic topic) {
        if (topic == null) {
            throw new IllegalArgumentException("Topic cannot be null");
        }
        if (findTopic(topic.getName()) != null) {
            throw new IllegalArgumentException("Topic already exists: " + topic.getName());
        }
        topics.add(topic);
    }

    /**
     * Changes the student's confidence in one of this course's topics.
     * @param topicName the name of the topic
     * @param confidence the new confidence level
     * @throws IllegalArgumentException if the course has no topic with that name
     */
    public void setTopicConfidence(String topicName, ConfidenceLevel confidence) {
        final Topic topic = findTopic(topicName);
        if (topic == null) {
            throw new IllegalArgumentException("No such topic: " + topicName);
        }
        topic.setConfidence(confidence);
    }

    private Topic findTopic(String topicName) {
        if (topicName != null) {
            for (Topic topic : topics) {
                if (topic.getName().equalsIgnoreCase(topicName.trim())) {
                    return topic;
                }
            }
        }
        return null;
    }

    /**
     * Returns the assessments entered for this course.
     * @return a read-only view of the assessments
     */
    public List<Assessment> getAssessments() {
        return Collections.unmodifiableList(assessments);
    }

    /**
     * Replaces the assessments entered for this course.
     * @param newAssessments the assessments completed so far
     * @throws IllegalArgumentException if the list or an assessment in it is null,
     *                                  or the weights add up to more than 100
     */
    public void setAssessments(List<Assessment> newAssessments) {
        if (newAssessments == null || newAssessments.contains(null)) {
            throw new IllegalArgumentException("Assessments cannot be null");
        }
        double totalWeight = 0;
        for (Assessment assessment : newAssessments) {
            totalWeight += assessment.getWeight();
        }
        if (totalWeight > MAX_PERCENTAGE + EPSILON) {
            throw new IllegalArgumentException("Assessment weights cannot add up to more than 100");
        }
        assessments.clear();
        assessments.addAll(newAssessments);
    }

    /**
     * Returns how much of the course grade has been completed so far.
     * @return the sum of the assessment weights, from 0 to 100
     */
    public double getCompletedWeight() {
        double totalWeight = 0;
        for (Assessment assessment : assessments) {
            totalWeight += assessment.getWeight();
        }
        return Math.min(totalWeight, MAX_PERCENTAGE);
    }

    /**
     * Returns the student's grade so far: the average of the assessment marks, weighted
     * by how much each assessment is worth.
     * @return the current percentage, or empty if no assessments have been entered
     */
    public OptionalDouble getCurrentPercentage() {
        if (assessments.isEmpty()) {
            return OptionalDouble.empty();
        }
        double weightedMarks = 0;
        double totalWeight = 0;
        for (Assessment assessment : assessments) {
            weightedMarks += assessment.getMark() * assessment.getWeight();
            totalWeight += assessment.getWeight();
        }
        final double percentage = weightedMarks / totalWeight;
        return OptionalDouble.of(Math.max(0, Math.min(percentage, MAX_PERCENTAGE)));
    }

    /**
     * Returns the letter grade for the student's grade so far.
     * @return the estimated grade, or empty if no assessments have been entered
     */
    public Optional<LetterGrade> getEstimatedGrade() {
        final OptionalDouble percentage = getCurrentPercentage();
        if (!percentage.isPresent()) {
            return Optional.empty();
        }
        return Optional.of(LetterGrade.fromPercentage(percentage.getAsDouble()));
    }

    /**
     * Returns the average mark the student needs on the rest of the course to finish
     * with the target grade. The result is above 100 if the target is out of reach,
     * and 0 or less if the target is already secured.
     * @return the required average on the remaining work, or empty if the whole
     *         course grade has already been completed
     */
    public OptionalDouble getRequiredAverageForTarget() {
        final double remainingWeight = MAX_PERCENTAGE - getCompletedWeight();
        if (remainingWeight <= EPSILON) {
            return OptionalDouble.empty();
        }
        double weightedMarks = 0;
        for (Assessment assessment : assessments) {
            weightedMarks += assessment.getMark() * assessment.getWeight();
        }
        final double required =
                (targetGrade.getMinPercentage() * MAX_PERCENTAGE - weightedMarks) / remainingWeight;
        return OptionalDouble.of(required);
    }
}
