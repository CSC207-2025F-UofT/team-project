package usecase.castspell;

public interface CastOutputBoundary {
    void prepareSuccessView(CastOutputData outputData);

    void prepareFailView(String errorMessage);
}
