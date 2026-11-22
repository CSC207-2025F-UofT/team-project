package interface_adapter.upload_receipt;

import application_business_rules.upload_receipt.UploadReceiptInputBoundary;
import application_business_rules.upload_receipt.UploadReceiptInputData;

public final class UploadReceiptController {
    private final UploadReceiptInputBoundary inputBoundary;
    public UploadReceiptController(UploadReceiptInputBoundary inputBoundary) {
        this.inputBoundary = inputBoundary;
    }
    public void execute(int userProfileId, String fileName, byte[] fileContents) {
        inputBoundary.execute(new UploadReceiptInputData(userProfileId, fileName, fileContents));
    }
}
