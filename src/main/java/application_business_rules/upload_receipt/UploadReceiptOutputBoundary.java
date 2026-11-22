package application_business_rules.upload_receipt;

public interface UploadReceiptOutputBoundary {
    void prepareSuccessView(UploadReceiptOutputData outputData);
    void prepareFailView(String error);
}
