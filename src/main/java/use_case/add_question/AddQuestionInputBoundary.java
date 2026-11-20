package use_case.add_question;
/**
 * Input Boundary interface for the Add Question Use Case.
 */
public interface AddQuestionInputBoundary {
    /**
     * Executes the Add Question use case.
     * @param inputData the data needed to create and save the question
     */
    void execute(AddQuestionInputData inputData);
}