package use_case.upload_statement;

public class UploadStatementInputData {
    private final String csvFilePath;
    public UploadStatementInputData(String csvFilePath) {
        this.csvFilePath = csvFilePath;
    }
    public String getCsvFilePath() {
        return csvFilePath;
    }
}

