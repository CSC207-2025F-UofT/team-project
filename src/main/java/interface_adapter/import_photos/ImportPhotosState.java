package interface_adapter.import_photos;

import java.util.EnumMap;
import java.util.Map;

import use_case.import_photos.ImportPhotosImportOutputData;
import use_case.import_photos.ImportPhotosGeocodeOutputData;
import use_case.import_photos.ImportPhotosTripOutputData;

public class ImportPhotosState {
    ImportPhotosStage stage;
    Map<ImportPhotosStage, String> stageErrors;
    ImportPhotosImportOutputData importData;
    ImportPhotosGeocodeOutputData geocodeData;
    ImportPhotosTripOutputData tripData;

    public ImportPhotosState() {
        stageErrors = new EnumMap<>(ImportPhotosStage.class);
    }

    public ImportPhotosStage getStage() {
        return stage;
    }

    public void setStage(ImportPhotosStage stage) {
        this.stage = stage;
    }

    public String getStageError() {
        return stageErrors.get(stage);
    }

    public void setStageAndError(ImportPhotosStage stage, String error) {
        this.stage = stage;
        stageErrors.put(stage, error);
    }

    public ImportPhotosImportOutputData getImportData() { return importData; }
    public void setImportData(ImportPhotosImportOutputData importData) { this.importData = importData; }

    public ImportPhotosGeocodeOutputData getGeocodeData() { return geocodeData; }
    public void setGeocodeData(ImportPhotosGeocodeOutputData geocodeData) { this.geocodeData = geocodeData; }

    public ImportPhotosTripOutputData getTripData() { return tripData; }
    public void setTripData(ImportPhotosTripOutputData tripData) { this.tripData = tripData; }
}
