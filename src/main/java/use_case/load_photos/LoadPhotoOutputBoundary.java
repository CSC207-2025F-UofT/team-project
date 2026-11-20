package use_case.load_photos;

public interface LoadPhotoOutputBoundary {
    void prepareSuccessView(LoadPhotoOutputData outputData);

    void prepareFailView(String photoId, String errorMessage);
}
