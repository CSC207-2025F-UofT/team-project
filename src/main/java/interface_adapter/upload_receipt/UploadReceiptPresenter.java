package interface_adapter.upload_receipt;

import use_case.upload_receipt.UploadReceiptOutputBoundary;
import use_case.upload_receipt.UploadReceiptOutputData;

public final class UploadReceiptPresenter implements UploadReceiptOutputBoundary {
    private final UploadReceiptViewModel viewModel;
    public UploadReceiptPresenter(UploadReceiptViewModel viewModel) { this.viewModel = viewModel; }
    @Override
    public void prepareSuccessView(UploadReceiptOutputData outputData) {
        viewModel.setSuccess(outputData.getMerchant(), outputData.getTotal(),
            outputData.getDate(), outputData.getStoredReceiptReference());
    }
    @Override
    public void prepareFailView(String error) { viewModel.setFailure(error); }
}
