package use_case.import_photos;

import java.util.ArrayList;
import java.util.List;

public class ImportPhotosImportOutputData {
    private final int successes;
    private final int total;
    private final List<String> failedPaths;

    public ImportPhotosImportOutputData(int successes, List<String> failedPaths, int total) {
        this.successes = successes;
        this.failedPaths = failedPaths;
        this.total = total;
    }

    public ImportPhotosImportOutputData() {
        this(0, new ArrayList<>(), 0);
    }

    public int getSuccesses() {
        return successes;
    }

    public int getTotal() {
        return total;
    }

    public List<String> getFailedPaths() {
        return failedPaths;
    }
}
