package application_business_rules.upload_receipt;

public final class UploadReceiptInputData {
    private final int userProfileId;
    private final String fileName;
    private final byte[] fileContents;

    public UploadReceiptInputData(int userProfileId, String fileName, byte[] fileContents) {
        this.userProfileId = userProfileId;
        this.fileName = fileName;
        this.fileContents = fileContents.clone();
    }
    public int getUserProfileId() { return userProfileId; }
    public String getFileName() { return fileName; }
    public byte[] getFileContents() { return fileContents.clone(); }
}
