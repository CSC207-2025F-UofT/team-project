package use_case.flashcard_generator;

public interface FlashcardGeneratorOutputBoundary {

    public void prepareFailView(String errorMessage);
    public void prepareSuccessView(FlashcardGeneratorOutputData output);
    }

