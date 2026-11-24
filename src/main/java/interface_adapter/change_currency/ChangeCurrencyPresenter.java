package interface_adapter.change_currency;

import use_case.change_currency.ChangeCurrencyOutputBoundary;
import use_case.change_currency.ChangeCurrencyOutputData;

public final class ChangeCurrencyPresenter implements ChangeCurrencyOutputBoundary {
    private final ChangeCurrencyViewModel viewModel;
    public ChangeCurrencyPresenter(ChangeCurrencyViewModel viewModel) {
        this.viewModel = viewModel;
    }
    @Override
    public void prepareSuccessView(ChangeCurrencyOutputData outputData) {
        viewModel.setSuccess(outputData.getAccountId(), outputData.getCurrencyCode(),
            outputData.getConvertedBalance());
    }
    @Override
    public void prepareFailView(String error) {
        viewModel.setError(error);
    }
}
