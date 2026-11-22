package application_business_rules.change_currency;

public interface ChangeCurrencyOutputBoundary {
    void prepareSuccessView(ChangeCurrencyOutputData outputData);
    void prepareFailView(String error);
}
