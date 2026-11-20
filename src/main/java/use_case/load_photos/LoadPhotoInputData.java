package use_case.load_photos;

public class LoadPhotoInputData {
    private final String photoId;

    public LoadPhotoInputData(String photId) { this.photoId = photId;}

    public String getPhotoId() { return photoId; }
}
