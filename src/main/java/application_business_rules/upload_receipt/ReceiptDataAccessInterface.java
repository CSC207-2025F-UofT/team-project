package application_business_rules.upload_receipt;

/** Use-case-owned boundary to receipt storage and extraction details. */
public interface ReceiptDataAccessInterface {
    String storeReceipt(int userProfileId, String fileName, byte[] contents);
    UploadReceiptOutputData extractReceiptDetails(String storedReceiptReference);
}
