package use_case.calculate_grade;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Output Data for the Calculate Grade Use Case.
 */
public class CalculateGradeOutputData {

    private final String courseCode;
    private final boolean hasMarks;
    private final double currentPercentage;
    private final String estimatedGrade;
    private final double completedWeight;
    private final String targetGrade;
    private final boolean courseCompleted;
    private final boolean targetReached;
    private final double requiredAverage;
    private final List<String> assessmentNames;
    private final List<Double> assessmentMarks;
    private final List<Double> assessmentWeights;

    /**
     * Creates the output data.
     * @param courseCode the code of the course
     * @param hasMarks false if the course now has no saved assessments
     * @param currentPercentage the weighted grade so far, from 0 to 100; 0 if there are no marks
     * @param estimatedGrade the label of the letter grade so far, or "" if there are no marks
     * @param completedWeight the total weight of the saved assessments, from 0 to 100
     * @param targetGrade the label of the course's target grade
     * @param courseCompleted true if the saved weights add up to 100
     * @param targetReached true if the grade so far is at least the target grade
     * @param requiredAverage the average needed on the remaining weight to reach the target;
     *                        meaningless if the course is completed
     * @param assessmentNames the names of the saved assessments
     * @param assessmentMarks the marks of the saved assessments, in the same order
     * @param assessmentWeights the weights of the saved assessments, in the same order
     */
    public CalculateGradeOutputData(String courseCode, boolean hasMarks, double currentPercentage,
                                    String estimatedGrade, double completedWeight,
                                    String targetGrade, boolean courseCompleted,
                                    boolean targetReached, double requiredAverage,
                                    List<String> assessmentNames, List<Double> assessmentMarks,
                                    List<Double> assessmentWeights) {
        this.courseCode = courseCode;
        this.hasMarks = hasMarks;
        this.currentPercentage = currentPercentage;
        this.estimatedGrade = estimatedGrade;
        this.completedWeight = completedWeight;
        this.targetGrade = targetGrade;
        this.courseCompleted = courseCompleted;
        this.targetReached = targetReached;
        this.requiredAverage = requiredAverage;
        this.assessmentNames = Collections.unmodifiableList(new ArrayList<>(assessmentNames));
        this.assessmentMarks = Collections.unmodifiableList(new ArrayList<>(assessmentMarks));
        this.assessmentWeights = Collections.unmodifiableList(new ArrayList<>(assessmentWeights));
    }

    public String getCourseCode() {
        return courseCode;
    }

    public boolean hasMarks() {
        return hasMarks;
    }

    public double getCurrentPercentage() {
        return currentPercentage;
    }

    public String getEstimatedGrade() {
        return estimatedGrade;
    }

    public double getCompletedWeight() {
        return completedWeight;
    }

    public String getTargetGrade() {
        return targetGrade;
    }

    public boolean isCourseCompleted() {
        return courseCompleted;
    }

    public boolean isTargetReached() {
        return targetReached;
    }

    public double getRequiredAverage() {
        return requiredAverage;
    }

    public List<String> getAssessmentNames() {
        return assessmentNames;
    }

    public List<Double> getAssessmentMarks() {
        return assessmentMarks;
    }

    public List<Double> getAssessmentWeights() {
        return assessmentWeights;
    }
}
