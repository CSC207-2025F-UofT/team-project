package use_case.import_photos;

import java.util.List;

public class ImportPhotosInputData {
    private final String excludeLocationDesc;
    private final int excludeDistance;
    private final List<String> paths;

    public ImportPhotosInputData(String excludeLocationDesc, int excludeDistance, List<String> paths) {
        this.excludeLocationDesc = excludeLocationDesc;
        this.excludeDistance = excludeDistance;
        this.paths = paths;
    }

    public String getExcludeLocationDesc() {
        return excludeLocationDesc;
    }

    public int getExcludeDistance() {
        return excludeDistance;
    }

    public List<String> getPaths() {
        return paths;
    }
}
