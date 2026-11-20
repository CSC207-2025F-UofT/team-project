package interface_adapter.track_applications;

import java.util.ArrayList;
import java.util.List;

/**
 * The state for the Track Applications View Model.
 */
public class TrackApplicationsState {

    private String adopterId = "";
    private List<ApplicationRow> rows = new ArrayList<>();
    private String message = "";

    public String getAdopterId() {
        return adopterId;
    }

    public void setAdopterId(String adopterId) {
        this.adopterId = adopterId;
    }

    public List<ApplicationRow> getRows() {
        return rows;
    }

    public void setRows(List<ApplicationRow> rows) {
        this.rows = rows;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
