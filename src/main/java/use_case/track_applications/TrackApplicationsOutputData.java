package use_case.track_applications;

import java.util.List;

/**
 * Output Data for the Track Applications Use Case.
 */
public class TrackApplicationsOutputData {

    private final List<ApplicationSummary> applications;

    /**
     * Creates the output data.
     * @param applications the adopter's applications, possibly empty
     */
    public TrackApplicationsOutputData(List<ApplicationSummary> applications) {
        this.applications = applications;
    }

    public List<ApplicationSummary> getApplications() {
        return applications;
    }
}
