package interface_adapter.load_photos;

/** Immutable: one per load result. imageBytes == null means the load failed. */
public class PhotoImageState {
    private final String photoId;
    private final byte[] imageBytes;

    public PhotoImageState(String photoId, byte[] imageBytes) {
        this.photoId = photoId;
        this.imageBytes = imageBytes;
    }

    public String getPhotoId() { return photoId; }
    public byte[] getImageBytes() { return imageBytes; }
}