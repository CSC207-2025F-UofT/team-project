package use_case.load_photos;

public class LoadPhotoOutputData {
    private final String photoId;
    private final byte[] imageBytes;

    public LoadPhotoOutputData(String photoId, byte[] imageBytes) {
        this.photoId = photoId;
        this.imageBytes = imageBytes;
    }

    public String getPhotoId() { return photoId; }

    /** Encoded image bytes. READ-ONLY! */
    public byte[] getImageBytes() { return imageBytes; }
}
