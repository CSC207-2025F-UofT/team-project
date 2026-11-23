package use_case.review_applications;

import entity.Application;
import use_case.track_applications.ApplicationSummary;

import java.util.List;

public class ReviewApplicationsOutputData {

    private final List<ApplicationSummary> applications;

    public ReviewApplicationsOutputData(List<ApplicationSummary> applications) {
        this.applications = applications;
    }

    public List<ApplicationSummary> getApplications() {return applications;}
}
