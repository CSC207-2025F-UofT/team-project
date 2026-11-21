package use_case.import_photos;

public interface ImportPhotosOutputBoundary {
    void prepareInvalidExclusionView(String error);
    void prepareImportFailureView(String error);
    void updateImportView(ImportPhotosImportOutputData outputData);
    void updateGeocodeView(ImportPhotosGeocodeOutputData outputData);
    void preparePhotoSavingView();
    void preparePhotoSaveFailureView(String error);
    void preparePhotoSortingView();
    void prepareTripGenerationView();
    void prepareTripSavingView();
    void prepareTripSaveFailureView(String error);
    void prepareTripSaveSuccessView(ImportPhotosTripOutputData outputData);
}
