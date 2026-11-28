package use_case.load_question;

public interface LoadQuestionOutputBoundary {
    void prepareSuccessView(LoadQuestionOutputData outputData);

    void prepareFailView(String errorMessage);
}
