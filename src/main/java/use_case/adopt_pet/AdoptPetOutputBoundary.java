package use_case.adopt_pet;

public interface AdoptPetOutputBoundary {

    void prepareSuccessView(AdoptPetOutputData outputData);

    void prepareFailView(String errorMessage);
}