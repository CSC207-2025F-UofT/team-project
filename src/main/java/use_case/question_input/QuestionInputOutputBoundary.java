package use_case.question_input;
/**
 * The output boundary for the question input use case.
 */
public interface QuestionInputOutputBoundary {
    /**
     * Prepares the success view for the Question input Use Case.
     * @param outputData the output data
     */
    void prepareSuccessView(QuestionInputOutputData outputData);

    /**
     * Prepares the failure view for the Question input Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);
}
