package use_case.open_pet_shop;

public interface OpenPetShopOutputBoundary {
    void prepareSuccessView(OpenPetShopOutputData outputData);

    void prepareFailView(String errorMessage);
}