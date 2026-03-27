package use_case.upload_statement;

public class UploadStatementInputData {
    private final String csvFilePath;
    private final String username;

    public UploadStatementInputData(String csvFilePath, String username) {
        this.csvFilePath = csvFilePath;
        this.username = username;
    }
    public String getCsvFilePath() {
        return csvFilePath;
    }
    public String getUsername() { return  username; }

}

