package interface_adapter.calculate_grade;

import interface_adapter.course_progress.AssessmentRow;
import interface_adapter.course_progress.CourseProgressState;
import interface_adapter.course_progress.CourseProgressViewModel;
import org.junit.jupiter.api.Test;
import use_case.calculate_grade.CalculateGradeOutputData;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CalculateGradePresenterTest {

    @Test
    void successUpdatesCalculatorAndProgressPage() {
        final GradeCalculatorViewModel calculatorViewModel = new GradeCalculatorViewModel();
        final CourseProgressViewModel progressViewModel = new CourseProgressViewModel();
        calculatorViewModel.getState().setCalculateError("old error");
        final int[] notifications = {0, 0};
        calculatorViewModel.addListener(() -> notifications[0]++);
        progressViewModel.addListener(new CourseProgressListenerAdapter() {
            @Override
            public void stateChanged() {
                notifications[1]++;
            }
        });

        final CalculateGradeOutputData output = new CalculateGradeOutputData("CSC207", true, 83.3333,
                "A-", 30, "A-", false, true, 78.5714,
                Arrays.asList("Midterm", "Assignment 1"), Arrays.asList(80.0, 90.0), Arrays.asList(20.0, 10.0));
        new CalculateGradePresenter(calculatorViewModel, progressViewModel).prepareSuccessView(output);

        final GradeCalculatorState calculatorState = calculatorViewModel.getState();
        assertEquals("Current grade: 83.3% (A-), based on 30% of the course.",
                calculatorState.getResultMessage());
        assertNull(calculatorState.getCalculateError());

        final CourseProgressState progressState = progressViewModel.getState();
        assertEquals("A-", progressState.getEstimatedGrade());
        assertEquals("You need an average of 78.6% on the remaining 70% to reach A-.",
                progressState.getTargetMessage());
        final List<AssessmentRow> rows = progressState.getAssessments();
        assertEquals(2, rows.size());
        assertEquals("Midterm", rows.get(0).getName());
        assertEquals("80", rows.get(0).getMark());
        assertEquals("20", rows.get(0).getWeight());

        assertEquals(1, notifications[0]);
        assertEquals(1, notifications[1]);
    }

    @Test
    void clearingMarksShowsNoGrade() {
        final GradeCalculatorViewModel calculatorViewModel = new GradeCalculatorViewModel();
        final CourseProgressViewModel progressViewModel = new CourseProgressViewModel();
        progressViewModel.getState().setEstimatedGrade("B");
        progressViewModel.getState().setAssessments(java.util.Collections.singletonList(
                new AssessmentRow("Midterm", "80", "20")));

        final CalculateGradeOutputData output = new CalculateGradeOutputData("CSC207", false, 0, "",
                0, "A", false, false, 85, java.util.Collections.emptyList(),
                java.util.Collections.emptyList(), java.util.Collections.emptyList());
        new CalculateGradePresenter(calculatorViewModel, progressViewModel).prepareSuccessView(output);

        assertEquals("All saved marks were cleared.", calculatorViewModel.getState().getResultMessage());
        assertEquals(CalculateGradePresenter.NO_GRADE, progressViewModel.getState().getEstimatedGrade());
        assertEquals(0, progressViewModel.getState().getAssessments().size());
        assertEquals("You need an average of 85% on the remaining 100% to reach A.",
                progressViewModel.getState().getTargetMessage());
    }

    @Test
    void failureOnlyShowsErrorInCalculator() {
        final GradeCalculatorViewModel calculatorViewModel = new GradeCalculatorViewModel();
        final CourseProgressViewModel progressViewModel = new CourseProgressViewModel();
        progressViewModel.getState().setEstimatedGrade("B");

        new CalculateGradePresenter(calculatorViewModel, progressViewModel)
                .prepareFailView("Row 1: Mark must be a number.");

        assertEquals("Row 1: Mark must be a number.", calculatorViewModel.getState().getCalculateError());
        assertEquals("B", progressViewModel.getState().getEstimatedGrade());
    }

    /** Lets a test override only the listener method it cares about. */
    private abstract static class CourseProgressListenerAdapter
            implements interface_adapter.course_progress.CourseProgressViewModelListener {
        @Override
        public void stateChanged() {
        }

        @Override
        public void targetGradeChanged() {
        }
    }
}
