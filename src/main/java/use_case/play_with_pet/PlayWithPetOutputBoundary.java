package use_case.play_with_pet;

public interface PlayWithPetOutputBoundary {

    void prepareSuccessView(PlayWithPetOutputData outputData);

    void prepareFailView(String errorMessage);
}