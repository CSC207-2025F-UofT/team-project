package use_case.manipulate_trips;

import java.time.LocalDateTime;

/** Read-only facts about one photo, for the trip details screen. */
public class PhotoDetails {
    private final String photoId;
    private final LocalDateTime time;

    public PhotoDetails(String photoId, LocalDateTime time) {
        this.photoId = photoId;
        this.time = time;
    }

    public String getPhotoId() { return photoId; }
    /** Null if the photo has no timestamp. */
    public LocalDateTime getTime() { return time; }
}