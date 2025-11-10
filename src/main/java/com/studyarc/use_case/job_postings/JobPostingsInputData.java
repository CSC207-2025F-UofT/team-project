package com.studyarc.use_case.job_postings;

/**
 * The Input Data for the Job Postings Use Case.
 */
public class JobPostingsInputData {
    private final String focus;
    private final String preferredLoc;
    private final int minSalary;

    public JobPostingsInputData(String focus, String preferredLoc, int minSalary) {
        this.focus = focus;
        this.preferredLoc = preferredLoc;
        this.minSalary = minSalary;
    }

    String getFocus() {
        return focus;
    }

    String getPreferredLoc() {
        return preferredLoc;
    }

    int getMinSalary() {
        return minSalary;
    }
}
