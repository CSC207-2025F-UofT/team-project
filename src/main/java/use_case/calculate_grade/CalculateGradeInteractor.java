package use_case.calculate_grade;

import entity.Assessment;
import entity.Course;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.OptionalDouble;
import java.util.Set;

/**
 * The Calculate Grade Interactor. It validates the marks and weights entered in the
 * grade calculator, saves them as the course's assessments and reports the new grade.
 * Submitting no rows clears the course's saved assessments.
 */
public class CalculateGradeInteractor implements CalculateGradeInputBoundary {

    private final CalculateGradeCourseDataAccessInterface courseDataAccessObject;
    private final CalculateGradeOutputBoundary calculateGradePresenter;

    public CalculateGradeInteractor(
            CalculateGradeCourseDataAccessInterface courseDataAccessInterface,
            CalculateGradeOutputBoundary calculateGradeOutputBoundary) {
        this.courseDataAccessObject = courseDataAccessInterface;
        this.calculateGradePresenter = calculateGradeOutputBoundary;
    }

    @Override
    public void execute(CalculateGradeInputData inputData) {
        final Course course = courseDataAccessObject.getCourse(
                inputData.getUsername(), inputData.getCourseCode());
        if (course == null) {
            calculateGradePresenter.prepareFailView(
                    "You are not enrolled in " + inputData.getCourseCode() + ".");
            return;
        }

        final List<Assessment> assessments = new ArrayList<>();
        final Set<String> seenNames = new HashSet<>();
        int rowNumber = 0;
        for (CalculateGradeInputData.AssessmentRow row : inputData.getRows()) {
            rowNumber++;
            final Assessment assessment;
            try {
                assessment = toAssessment(row);
            }
            catch (IllegalArgumentException ex) {
                calculateGradePresenter.prepareFailView(
                        "Row " + rowNumber + ": " + ex.getMessage() + ".");
                return;
            }
            if (!seenNames.add(assessment.getName().toLowerCase(Locale.ROOT))) {
                calculateGradePresenter.prepareFailView("Row " + rowNumber + ": \""
                        + assessment.getName() + "\" is entered more than once.");
                return;
            }
            assessments.add(assessment);
        }

        try {
            course.setAssessments(assessments);
        }
        catch (IllegalArgumentException ex) {
            calculateGradePresenter.prepareFailView(ex.getMessage() + ".");
            return;
        }
        courseDataAccessObject.saveCourse(inputData.getUsername(), course);

        calculateGradePresenter.prepareSuccessView(toOutputData(course));
    }

    private static Assessment toAssessment(CalculateGradeInputData.AssessmentRow row) {
        final double mark = parseNumber(row.getMark(), "Mark");
        final double weight = parseNumber(row.getWeight(), "Weight");
        return new Assessment(row.getName(), mark, weight);
    }

    private static double parseNumber(String text, String fieldName) {
        String trimmed = text == null ? "" : text.trim();
        // Accept "80%" as well as "80".
        if (trimmed.endsWith("%")) {
            trimmed = trimmed.substring(0, trimmed.length() - 1).trim();
        }
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty");
        }
        try {
            return Double.parseDouble(trimmed);
        }
        catch (NumberFormatException ex) {
            throw new IllegalArgumentException(fieldName + " must be a number");
        }
    }

    private static CalculateGradeOutputData toOutputData(Course course) {
        final List<String> names = new ArrayList<>();
        final List<Double> marks = new ArrayList<>();
        final List<Double> weights = new ArrayList<>();
        for (Assessment assessment : course.getAssessments()) {
            names.add(assessment.getName());
            marks.add(assessment.getMark());
            weights.add(assessment.getWeight());
        }
        final OptionalDouble percentage = course.getCurrentPercentage();
        final OptionalDouble requiredAverage = course.getRequiredAverageForTarget();
        final boolean targetReached = percentage.isPresent()
                && percentage.getAsDouble() >= course.getTargetGrade().getMinPercentage();
        return new CalculateGradeOutputData(
                course.getCode(),
                percentage.isPresent(),
                percentage.orElse(0),
                course.getEstimatedGrade().map(grade -> grade.getLabel()).orElse(""),
                course.getCompletedWeight(),
                course.getTargetGrade().getLabel(),
                !requiredAverage.isPresent(),
                targetReached,
                requiredAverage.orElse(0),
                names, marks, weights);
    }
}
