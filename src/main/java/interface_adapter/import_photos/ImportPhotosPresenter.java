package interface_adapter.import_photos;

import use_case.import_photos.ImportPhotosGeocodeOutputData;
import use_case.import_photos.ImportPhotosImportOutputData;
import use_case.import_photos.ImportPhotosOutputBoundary;
import use_case.import_photos.ImportPhotosTripOutputData;

public class ImportPhotosPresenter implements ImportPhotosOutputBoundary {
    private final ImportPhotosViewModel viewModel;

    public ImportPhotosPresenter(ImportPhotosViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareInvalidExclusionView(String error) {
        viewModel.getState().setStageAndError(ImportPhotosStage.INVALID_EXCLUSION, error);
        viewModel.firePropertyChanged();
    }

    @Override
    public void prepareImportFailureView(String error) {
        viewModel.getState().setStageAndError(ImportPhotosStage.IMPORT_FAILURE, error);
        viewModel.firePropertyChanged();
    }

    @Override
    public void updateImportView(ImportPhotosImportOutputData outputData) {
        ImportPhotosState state = viewModel.getState();
        state.setStage(ImportPhotosStage.IMPORTING);
        state.setImportData(outputData);
        viewModel.firePropertyChanged();
    }

    @Override
    public void updateGeocodeView(ImportPhotosGeocodeOutputData outputData) {
        ImportPhotosState state = viewModel.getState();
        state.setStage(ImportPhotosStage.GEOCODING);
        state.setGeocodeData(outputData);
        viewModel.firePropertyChanged();
    }

    @Override
    public void preparePhotoSavingView() {
        viewModel.getState().setStage(ImportPhotosStage.PHOTO_SAVING);
        viewModel.firePropertyChanged();
    }

    @Override
    public void preparePhotoSaveFailureView(String error) {
        viewModel.getState().setStageAndError(ImportPhotosStage.PHOTO_SAVE_FAILURE, error);
        viewModel.firePropertyChanged();
    }

    @Override
    public void preparePhotoSortingView() {
        viewModel.getState().setStage(ImportPhotosStage.PHOTO_SORTING);
        viewModel.firePropertyChanged();
    }

    @Override
    public void prepareTripGenerationView() {
        viewModel.getState().setStage(ImportPhotosStage.TRIP_GENERATION);
        viewModel.firePropertyChanged();
    }

    @Override
    public void prepareTripSavingView() {
        viewModel.getState().setStage(ImportPhotosStage.TRIP_SAVING);
        viewModel.firePropertyChanged();
    }

    @Override
    public void prepareTripSaveFailureView(String error) {
        viewModel.getState().setStageAndError(ImportPhotosStage.TRIP_SAVE_FAILURE, error);
        viewModel.firePropertyChanged();
    }

    @Override
    public void prepareTripSaveSuccessView(ImportPhotosTripOutputData outputData) {
        ImportPhotosState state = viewModel.getState();
        state.setStage(ImportPhotosStage.TRIP_SAVE_SUCCESS);
        state.setTripData(outputData);
        viewModel.firePropertyChanged();
    }
}
