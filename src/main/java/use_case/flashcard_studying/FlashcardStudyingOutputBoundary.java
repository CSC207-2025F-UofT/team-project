package use_case.flashcard_studying;

public interface FlashcardStudyingOutputBoundary {
    /**
     * Prepares the success view for the FlashcardStudying Use Case.
     * @param outputData the output data
     */
    void prepareSuccessView(FlashcardStudyingOutputData outputData);

    void prepareFailView(String message);
}
