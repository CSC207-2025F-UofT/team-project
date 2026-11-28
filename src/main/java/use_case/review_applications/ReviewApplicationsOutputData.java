package use_case.review_applications;

import java.time.LocalDate;
import java.util.List;

/**
 * The applications the admin asked to see. Entry i of every list describes the same application.
 */
public class ReviewApplicationsOutputData {

    private final List<String> applicationIds;
    private final List<String> petNames;
    private final List<String> adopterNames;
    private final List<LocalDate> submittedDates;
    private final List<String> statuses;

    /**
     * Creates the output.
     * @param applicationIds the applications' ids
     * @param petNames the names of the pets applied for
     * @param adopterNames the names of the adopters who applied
     * @param submittedDates the days the applications were submitted
     * @param statuses the current statuses, for example PENDING
     * @throws IllegalArgumentException if the lists do not all have the same length
     */
    public ReviewApplicationsOutputData(List<String> applicationIds, List<String> petNames,
                                        List<String> adopterNames, List<LocalDate> submittedDates,
                                        List<String> statuses) {
        final int size = applicationIds.size();
        if (petNames.size() != size || adopterNames.size() != size || submittedDates.size() != size
                || statuses.size() != size) {
            throw new IllegalArgumentException("Every list must have one entry per application.");
        }
        this.applicationIds = List.copyOf(applicationIds);
        this.petNames = List.copyOf(petNames);
        this.adopterNames = List.copyOf(adopterNames);
        this.submittedDates = List.copyOf(submittedDates);
        this.statuses = List.copyOf(statuses);
    }

    /**
     * Returns how many applications there are.
     * @return the number of applications
     */
    public int getApplicationCount() {
        return applicationIds.size();
    }

    public List<String> getApplicationIds() {
        return applicationIds;
    }

    public List<String> getPetNames() {
        return petNames;
    }

    public List<String> getAdopterNames() {
        return adopterNames;
    }

    public List<LocalDate> getSubmittedDates() {
        return submittedDates;
    }

    public List<String> getStatuses() {
        return statuses;
    }
}
