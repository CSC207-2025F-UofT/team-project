package use_case.checkOutfit;

public interface CheckOutfitOutputBoundary {
    void prepareSuccessView(CheckOutfitOutputData outputData);
    void prepareFailView(String error);
    void goBack();
}
