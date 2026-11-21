package use_case.import_photos;

import java.util.ArrayList;
import java.util.List;

public class ImportPhotosTripOutputData {
    private final List<String> tripNames;
    private final int sortedPhotoCount;

    public ImportPhotosTripOutputData(int sortedPhotoCount, List<String> tripNames) {
        this.sortedPhotoCount = sortedPhotoCount;
        this.tripNames = tripNames;
    }

    public ImportPhotosTripOutputData() {
        this(0,  new ArrayList<>());
    }

    public int getSortedPhotoCount() {
        return sortedPhotoCount;
    }

    public List<String> getTripNames() {
        return tripNames;
    }
}
