package use_case.upload_receipt;

public interface UploadReceiptOutputBoundary {
    void prepareSuccessView(UploadReceiptOutputData outputData);
    void prepareFailView(String error);
}
