package use_case.review_applications;

import java.util.List;

/**
 * The applications the admin asked to see.
 */
public class ReviewApplicationsOutputData {

    private final List<ReviewApplicationSummary> applications;

    /**
     * Creates the output.
     * @param applications the matching applications
     */
    public ReviewApplicationsOutputData(List<ReviewApplicationSummary> applications) {
        this.applications = List.copyOf(applications);
    }

    public List<ReviewApplicationSummary> getApplications() {
        return applications;
    }
}
