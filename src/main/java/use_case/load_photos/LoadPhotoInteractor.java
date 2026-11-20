package use_case.load_photos;

import entity.Photo;
import use_case.ImageReadException;
import use_case.ImageReaderInterface;
import use_case.PhotoAccessException;
import use_case.PhotoAccessInterface;

public class LoadPhotoInteractor implements LoadPhotoInputBoundary {
    private final PhotoAccessInterface photoAccess;
    private final ImageReaderInterface imageReader;
    private final LoadPhotoOutputBoundary presenter;

    public LoadPhotoInteractor(PhotoAccessInterface photoAccess, ImageReaderInterface imageReader,
                               LoadPhotoOutputBoundary presenter) {
        this.photoAccess = photoAccess;
        this.imageReader = imageReader;
        this.presenter = presenter;
    }

    @Override
    public void execute(LoadPhotoInputData inputData) {
        String id = inputData.getPhotoId();
        byte[] bytes;
        try {
            Photo photo = photoAccess.getPhotoById(id);
            bytes = imageReader.readFullImage(photo);
        } catch (PhotoAccessException | ImageReadException e) {
            presenter.prepareFailView(id, e.getMessage());
            return;
        }
        presenter.prepareSuccessView(new LoadPhotoOutputData(id, bytes));
    }
}