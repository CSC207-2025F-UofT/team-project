package interface_adapter.manipulate_trips;

import java.util.ArrayList;
import java.util.List;

/** Everything the trip details screen displays. */
public class TripDetailsState {
    private String tripId;
    private String name = "";
    private String dateRange = "";
    private String photoCount = "";
    private List<PhotoTile> photos = new ArrayList<>();
    private List<MergeOption> mergeOptions = new ArrayList<>();
    private String suggestedSplitName = "";
    private String errorMessage;
    private String noticeMessage;

    public String getTripId() { return tripId; }
    public void setTripId(String tripId) { this.tripId = tripId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDateRange() { return dateRange; }
    public void setDateRange(String dateRange) { this.dateRange = dateRange; }

    /** "5 photos". */
    public String getPhotoCount() { return photoCount; }
    public void setPhotoCount(String photoCount) { this.photoCount = photoCount; }

    /** Chronological. */
    public List<PhotoTile> getPhotos() { return photos; }
    public void setPhotos(List<PhotoTile> photos) { this.photos = List.copyOf(photos); }

    public List<MergeOption> getMergeOptions() { return mergeOptions; }
    public void setMergeOptions(List<MergeOption> mergeOptions) { this.mergeOptions = List.copyOf(mergeOptions); }

    /** Prefilled in the split dialog, e.g. "Montreal Weekend (2)". */
    public String getSuggestedSplitName() { return suggestedSplitName; }
    public void setSuggestedSplitName(String suggestedSplitName) { this.suggestedSplitName = suggestedSplitName; }

    /** Null when the last action succeeded. */
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    /** Confirmation of the last action, e.g. "Merged 2 trips.". Null when there is nothing to say. */
    public String getNoticeMessage() { return noticeMessage; }
    public void setNoticeMessage(String noticeMessage) { this.noticeMessage = noticeMessage; }
}