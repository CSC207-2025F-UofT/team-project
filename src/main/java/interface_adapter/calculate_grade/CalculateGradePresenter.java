package interface_adapter.calculate_grade;

import interface_adapter.course_progress.AssessmentRow;
import interface_adapter.course_progress.CourseProgressState;
import interface_adapter.course_progress.CourseProgressViewModel;
import interface_adapter.course_progress.TargetMessageFormatter;
import use_case.calculate_grade.CalculateGradeOutputBoundary;
import use_case.calculate_grade.CalculateGradeOutputData;

import java.util.ArrayList;
import java.util.List;

/**
 * The Presenter for the Calculate Grade Use Case. It shows the result in the calculator
 * pop-up and updates the estimated grade, target message and saved assessments of the
 * course progress page.
 */
public class CalculateGradePresenter implements CalculateGradeOutputBoundary {

    /** Shown as the estimated grade when no marks have been entered. */
    public static final String NO_GRADE = "N/A";

    private static final double MAX_PERCENTAGE = 100;

    private final GradeCalculatorViewModel gradeCalculatorViewModel;
    private final CourseProgressViewModel courseProgressViewModel;

    public CalculateGradePresenter(GradeCalculatorViewModel gradeCalculatorViewModel,
                                   CourseProgressViewModel courseProgressViewModel) {
        this.gradeCalculatorViewModel = gradeCalculatorViewModel;
        this.courseProgressViewModel = courseProgressViewModel;
    }

    @Override
    public void prepareSuccessView(CalculateGradeOutputData response) {
        final List<AssessmentRow> rows = new ArrayList<>();
        for (int i = 0; i < response.getAssessmentNames().size(); i++) {
            rows.add(new AssessmentRow(
                    response.getAssessmentNames().get(i),
                    TargetMessageFormatter.formatNumber(response.getAssessmentMarks().get(i)),
                    TargetMessageFormatter.formatNumber(response.getAssessmentWeights().get(i))));
        }
        final double remainingWeight = MAX_PERCENTAGE - response.getCompletedWeight();

        // Update the progress page first, so the calculator can read the saved rows from it.
        final CourseProgressState progressState = courseProgressViewModel.getState();
        progressState.setAssessments(rows);
        final String resultMessage;
        if (response.hasMarks()) {
            progressState.setEstimatedGrade(response.getEstimatedGrade());
            resultMessage = "Current grade: "
                    + TargetMessageFormatter.formatNumber(response.getCurrentPercentage()) + "% ("
                    + response.getEstimatedGrade() + "), based on "
                    + TargetMessageFormatter.formatNumber(response.getCompletedWeight())
                    + "% of the course.";
        }
        else {
            progressState.setEstimatedGrade(NO_GRADE);
            resultMessage = "All saved marks were cleared.";
        }
        progressState.setTargetMessage(TargetMessageFormatter.format(
                response.getTargetGrade(),
                response.isCourseCompleted(),
                response.isTargetReached(),
                response.getCurrentPercentage(),
                response.getEstimatedGrade(),
                response.getRequiredAverage(),
                remainingWeight));
        courseProgressViewModel.notifyStateChanged();

        final GradeCalculatorState calculatorState = gradeCalculatorViewModel.getState();
        calculatorState.setResultMessage(resultMessage);
        calculatorState.setCalculateError(null);
        gradeCalculatorViewModel.notifyStateChanged();
    }

    @Override
    public void prepareFailView(String error) {
        final GradeCalculatorState calculatorState = gradeCalculatorViewModel.getState();
        calculatorState.setCalculateError(error);
        gradeCalculatorViewModel.notifyStateChanged();
    }
}
