package use_case.import_photos;

import java.lang.RuntimeException;
import java.nio.file.Path;

public class ImageImportException extends RuntimeException {
    private final Path offendingPath;

    public ImageImportException(Path offendingPath, String reason, Throwable cause) {
        super("Import failed for path \"" + offendingPath + "\": " + reason, cause);

        this.offendingPath = offendingPath;
    }

    public ImageImportException(Path offendingPath, String reason) {
        this(offendingPath, reason, null);
    }

    public Path getOffendingPath() {
        return offendingPath;
    }
}
