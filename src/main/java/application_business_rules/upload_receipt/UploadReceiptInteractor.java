package application_business_rules.upload_receipt;

/** TODO: Mashac implements receipt storage/extraction orchestration here. */
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
        throw new UnsupportedOperationException("Upload Receipt use case not implemented yet.");
    }
}
