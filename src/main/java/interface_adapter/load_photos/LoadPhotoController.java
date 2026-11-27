package interface_adapter.load_photos;

import use_case.load_photos.LoadPhotoInputBoundary;
import use_case.load_photos.LoadPhotoInputData;

public class LoadPhotoController {
    private final LoadPhotoInputBoundary interactor;

    public LoadPhotoController(LoadPhotoInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(String photoId) {
        interactor.execute(new LoadPhotoInputData(photoId));
    }
}