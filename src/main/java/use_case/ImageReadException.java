package use_case;

public class ImageReadException extends RuntimeException {
    private final String photoId;

    public ImageReadException(String photoId, String reason, Throwable cause) {
        super("Photo with id " + photoId + " unavailable: " + reason, cause);

        this.photoId = photoId;
    }

    public ImageReadException(String photoId, String reason) {
        this(photoId, reason, null);
    }

    public String getPhotoId() {
        return photoId;
    }
}
