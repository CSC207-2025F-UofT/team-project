package com.studyarc.entity.job_postings;

public class JobListing {
    private final String title;
    private final int jobId;
    private final String companyName;
    private final int salaryMin;
    private final int salaryMax;
    private final String jobDesc;
    private final String jobLoc;
    private final String redirectUrl;

    public JobListing(String title, int jobId, String companyName, int salaryMin, int salaryMax, String jobDesc, String jobLoc, String redirectUrl, int jobId1, String companyName1, int salaryMin1, int salaryMax1, String jobDesc1, String jobLoc1, String redirectUrl1) {
        this.title = title;
        this.jobId = jobId1;
        this.companyName = companyName1;
        this.salaryMin = salaryMin1;
        this.salaryMax = salaryMax1;
        this.jobDesc = jobDesc1;
        this.jobLoc = jobLoc1;
        this.redirectUrl = redirectUrl1;
    }
}
