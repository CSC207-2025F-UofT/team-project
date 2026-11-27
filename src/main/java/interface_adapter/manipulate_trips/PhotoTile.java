package interface_adapter.manipulate_trips;

/** One photo in the trip details grid, formatted for display. */
public class PhotoTile {
    private final String photoId;
    private final String caption;

    public PhotoTile(String photoId, String caption) {
        this.photoId = photoId;
        this.caption = caption;
    }

    public String getPhotoId() { return photoId; }
    /** "12 Nov · 10:00", or "No date". */
    public String getCaption() { return caption; }
}