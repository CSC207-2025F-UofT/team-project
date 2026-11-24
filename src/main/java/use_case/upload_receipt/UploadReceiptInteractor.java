package use_case.upload_receipt;

/** Stores a receipt and asks the injected extraction service for candidate details. */
public final class UploadReceiptInteractor implements UploadReceiptInputBoundary {
    private final ReceiptDataAccessInterface dataAccess;
    private final UploadReceiptOutputBoundary presenter;

    public UploadReceiptInteractor(ReceiptDataAccessInterface dataAccess,
                                   UploadReceiptOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(UploadReceiptInputData inputData) {
        try {
            if (inputData.getFileContents() == null || inputData.getFileContents().length == 0) {
                presenter.prepareFailView("Choose a receipt file first.");
                return;
            }
            String reference = dataAccess.storeReceipt(inputData.getUserProfileId(),
                inputData.getFileName(), inputData.getFileContents());
            UploadReceiptOutputData extracted = dataAccess.extractReceiptDetails(reference);
            if (extracted == null) {
                presenter.prepareFailView("Receipt details could not be extracted.");
                return;
            }
            presenter.prepareSuccessView(new UploadReceiptOutputData(
                extracted.getMerchant(), extracted.getTotal(), extracted.getDate(), reference));
        } catch (RuntimeException exception) {
            presenter.prepareFailView(exception.getMessage());
        }
    }
}
