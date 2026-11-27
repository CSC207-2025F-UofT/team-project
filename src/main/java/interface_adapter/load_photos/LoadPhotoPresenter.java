package interface_adapter.load_photos;

import use_case.load_photos.LoadPhotoOutputBoundary;
import use_case.load_photos.LoadPhotoOutputData;

public class LoadPhotoPresenter implements LoadPhotoOutputBoundary {
    private final PhotoImageViewModel viewModel;

    public LoadPhotoPresenter(PhotoImageViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(LoadPhotoOutputData outputData) {
        publish(new PhotoImageState(outputData.getPhotoId(), outputData.getImageBytes()));
    }

    @Override
    public void prepareFailView(String photoId, String errorMessage) {
        publish(new PhotoImageState(photoId, null));
    }

    private void publish(PhotoImageState state) {
        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }
}