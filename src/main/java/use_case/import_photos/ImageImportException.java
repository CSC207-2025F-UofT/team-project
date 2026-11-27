package use_case.import_photos;

import java.lang.RuntimeException;

public class ImageImportException extends RuntimeException {
    private final String offendingPath;

    public ImageImportException(String offendingPath, String reason, Throwable cause) {
        super("Import failed for path \"" + offendingPath + "\": " + reason, cause);

        this.offendingPath = offendingPath;
    }

    public ImageImportException(String offendingPath, String reason) {
        this(offendingPath, reason, null);
    }

    public String getOffendingPath() {
        return offendingPath;
    }
}
