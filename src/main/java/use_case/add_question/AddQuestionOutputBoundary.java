package use_case.add_question;
/**
 * Output Boundary interface for the Add Question Use Case.
 */
public interface AddQuestionOutputBoundary {
    /**
     * Prepares the success view after question is successfully added and saved.
     * @param outputData the output data
     */
    void prepareSuccessView(AddQuestionOutputData outputData);
    /**
     * Prepares the failure view when adding the question fails.
     * @param errorMessage the explanation of why it failed
     */
    void prepareFailView(String errorMessage);
}
