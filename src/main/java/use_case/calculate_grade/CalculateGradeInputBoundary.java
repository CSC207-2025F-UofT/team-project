package use_case.calculate_grade;

/**
 * Input Boundary for the Calculate Grade Use Case.
 */
public interface CalculateGradeInputBoundary {

    /**
     * Executes the calculate grade use case.
     * @param calculateGradeInputData the input data
     */
    void execute(CalculateGradeInputData calculateGradeInputData);
}
