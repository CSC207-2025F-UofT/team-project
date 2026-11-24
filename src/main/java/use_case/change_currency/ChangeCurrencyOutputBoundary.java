package use_case.change_currency;

public interface ChangeCurrencyOutputBoundary {
    void prepareSuccessView(ChangeCurrencyOutputData outputData);
    void prepareFailView(String error);
}
