package interface_adapter.calculate_grade;

import interface_adapter.course_progress.AssessmentRow;
import use_case.calculate_grade.CalculateGradeInputBoundary;
import use_case.calculate_grade.CalculateGradeInputData;

import java.util.ArrayList;
import java.util.List;

/**
 * The controller for the Calculate Grade Use Case.
 */
public class CalculateGradeController {

    private final CalculateGradeInputBoundary calculateGradeUseCaseInteractor;

    public CalculateGradeController(CalculateGradeInputBoundary calculateGradeUseCaseInteractor) {
        this.calculateGradeUseCaseInteractor = calculateGradeUseCaseInteractor;
    }

    /**
     * Executes the Calculate Grade Use Case.
     * @param username the user whose course is being graded
     * @param courseCode the code of the course
     * @param rows the calculator rows, each holding the name, mark and weight as typed
     */
    public void execute(String username, String courseCode, List<AssessmentRow> rows) {
        final List<CalculateGradeInputData.AssessmentRow> inputRows = new ArrayList<>();
        for (AssessmentRow row : rows) {
            inputRows.add(new CalculateGradeInputData.AssessmentRow(
                    row.getName(), row.getMark(), row.getWeight()));
        }
        final CalculateGradeInputData inputData = new CalculateGradeInputData(
                username, courseCode, inputRows);
        calculateGradeUseCaseInteractor.execute(inputData);
    }
}
