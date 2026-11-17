package use_case.calculate_grade;

/**
 * The output boundary for the Calculate Grade Use Case.
 */
public interface CalculateGradeOutputBoundary {

    /**
     * Prepares the success view for the Calculate Grade Use Case.
     * @param outputData the output data
     */
    void prepareSuccessView(CalculateGradeOutputData outputData);

    /**
     * Prepares the failure view for the Calculate Grade Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);
}
