package use_case.purchase_pet_item;

public interface PurchasePetItemOutputBoundary {
    void prepareSuccessView(PurchasePetItemOutputData outputData);

    void prepareFailView(String errorMessage);
}