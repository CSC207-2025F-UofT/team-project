package com.studyarc.entity.job_postings;

public class JobSearchRequest {
    private final String focus;
    private final String preferredLoc;
    private final int minSalary;

    public JobSearchRequest(String focus, String preferredLoc, int minSalary) {
        this.focus = focus;
        this.preferredLoc = preferredLoc;
        this.minSalary = minSalary;
    }
}
