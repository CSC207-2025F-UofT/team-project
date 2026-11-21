package use_case.import_photos;

public class ImportPhotosGeocodeOutputData {
    private final int geocodedCount;
    private final int failCount;
    private final int skipCount;

    public ImportPhotosGeocodeOutputData(int geocodedCount, int failCount, int skipCount) {
        this.geocodedCount = geocodedCount;
        this.failCount = failCount;
        this.skipCount = skipCount;
    }

    public ImportPhotosGeocodeOutputData() {
        this(0, 0, 0);
    }

    public int getGeocodedCount() {
        return geocodedCount;
    }

    public int getFailCount() {
        return failCount;
    }

    public int getSkipCount() {
        return skipCount;
    }
}
